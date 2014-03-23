package br.com.badrequest.transplot.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import br.com.badrequest.transplot.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by gmarques on 3/22/14.
 */
@EActivity(R.layout.map)
public class Map extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    GoogleMap map;

    LocationClient mLocationClient;

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
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setCompassEnabled(false);

        //FIXME: Corrigir para funcionar automaticamente
        map.setMyLocationEnabled(true);

        addHeatMap();
    }


    private void addHeatMap() {
//        List<LatLng> list = new ArrayList<LatLng>();
//
//        list.add(new LatLng(-37.1886, 145.708));
//        list.add(new LatLng(-37.8361, 144.845));
//        list.add(new LatLng(-38.4034, 144.192));
//        list.add(new LatLng(-38.7597, 143.67));
//        list.add(new LatLng(-36.9672, 141.083));
//
//        // Create a heat map tile provider, passing it the latlngs of the police stations.
//        TileProvider mProvider = new HeatmapTileProvider.Builder()
//                .data(list)
//                .build();
//        // Add a tile overlay to the map, using the heat map tile provider.
//        map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }



    @Override
    public void onConnected(Bundle bundle) {

//        Location location = mLocationClient.getLastLocation();
//        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//
//        CameraUpdate center=
//                CameraUpdateFactory.newLatLng(new LatLng(-37.1886, 145.708));
//        CameraUpdate zoom= CameraUpdateFactory.zoomTo(13.2f);
//
//        map.moveCamera(center);
//        map.animateCamera(zoom);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

}