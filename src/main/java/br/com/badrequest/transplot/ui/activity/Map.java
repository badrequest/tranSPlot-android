package br.com.badrequest.transplot.ui.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.badrequest.transplot.R;
import br.com.badrequest.transplot.integration.bean.Point;
import br.com.badrequest.transplot.integration.bean.Region;
import br.com.badrequest.transplot.integration.bean.Traffic;
import br.com.badrequest.transplot.integration.service.handler.ServiceErrorHandler;
import br.com.badrequest.transplot.integration.service.mapper.TransplotServiceMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import uk.me.jstott.jcoord.UTMRef;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gmarques on 3/22/14.
 */
@EActivity(R.layout.map)
public class Map extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    @RestService
    TransplotServiceMapper transplotServiceMapper;

    @Bean
    ServiceErrorHandler serviceErrorHandler;

    @ViewById(R.id.left_drawer)
    ListView mDrawerList;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    GoogleMap map;

    LocationClient mLocationClient;

    private Lock lock = new ReentrantLock();

    //TODO: Deixar externo a remocao das barras
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
        if (id != 0) { //KitKat or higher
            boolean enabled = getResources().getBoolean(id);
            // enabled = are translucent bars supported on this device
            if (enabled) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        mLocationClient = new LocationClient(this, this, this);

        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews() {
        mLocationClient.connect();
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.setMyLocationEnabled(true);

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                fetchTransit(map.getProjection().getVisibleRegion().latLngBounds.northeast,
                        map.getProjection().getVisibleRegion().latLngBounds.southwest);
            }
        });

        List<String> nomes = new ArrayList<String>();
        nomes.add(" ");
        nomes.add("  ");
        nomes.add("Transito");
        nomes.add("Transporte Público");
        nomes.add("Bicicleta");

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , nomes));

    }


    @UiThread
    void addHeatMap(List<WeightedLatLng> list) {

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        TileProvider mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider).fadeIn(true));
    }



    @Override
    public void onConnected(Bundle bundle) {

        Location location = mLocationClient.getLastLocation();
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        if(location != null) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15f);

            map.moveCamera(center);
            map.animateCamera(zoom);
        }
    }

    @Background
    void fetchTransit(LatLng northeast, LatLng southwest) {
        if (lock.tryLock()) {
            try {
                UTMRef utmRefOrigin = new uk.me.jstott.jcoord.LatLng(-23.805917, -46.862748).toUTMRef();

                UTMRef utmRefNorth = new uk.me.jstott.jcoord.LatLng(northeast.latitude, northeast.longitude).toUTMRef();
                UTMRef utmRefSouth = new uk.me.jstott.jcoord.LatLng(southwest.latitude, southwest.longitude).toUTMRef();
                double x1 = utmRefSouth.getEasting() - utmRefOrigin.getEasting();
                double y1 = utmRefSouth.getNorthing() - utmRefOrigin.getNorthing();

                double x2 = utmRefNorth.getEasting() - utmRefOrigin.getEasting();
                double y2 = utmRefNorth.getNorthing() - utmRefOrigin.getNorthing();

                Point p1 = new Point((int) Math.floor(x1/10), (int) Math.floor(y1/10));
                Point p2 = new Point((int) Math.floor(x2/10), (int) Math.floor(y2/10));

                Traffic traffic = transplotServiceMapper.getTraffic(new Region(p1, p2, 1));

                List<WeightedLatLng> list = new ArrayList<WeightedLatLng>();

                if(traffic == null || traffic.getData() == null) return;

                for (Traffic.Velocity velocity : traffic.getData()) {
                     uk.me.jstott.jcoord.LatLng latLngVel = new UTMRef( (velocity.getPosition().getX() * 10) + utmRefOrigin.getEasting(),
                             (velocity.getPosition().getY() * 10) + utmRefOrigin.getNorthing(),
                             UTMRef.getUTMLatitudeZoneLetter(northeast.latitude), 23).toLatLng();

                    list.add(new WeightedLatLng(new LatLng(latLngVel.getLat(),latLngVel.getLng()), 100-velocity.getMeanVelocity().doubleValue()));
                    Log.d("TRANSPLOT", "new google.maps.LatLng(" + latLngVel.getLat() + ", " + latLngVel.getLng() + ");");


                }
                if(!list.isEmpty()) addHeatMap(list);

            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

}