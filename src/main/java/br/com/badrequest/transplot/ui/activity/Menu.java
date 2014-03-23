package br.com.badrequest.transplot.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import br.com.badrequest.transplot.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import org.androidannotations.annotations.*;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

/**
 * Created by gmarques on 3/22/14.
 */
@EActivity(R.layout.menu)
public class Menu extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    @ViewById
    TextView tvTwitter;

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
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);

        //FIXME: Corrigir para funcionar automaticamente
        //map.setMyLocationEnabled(true);

        getTwitter();
    }

    @Click(R.id.ibMap)
    void openMapActivity() {
        startActivity(new Intent(this, Map_.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Click(R.id.ibMonitor)
    void openMonitorActivity() {
        startActivity(new Intent(this, Monitor_.class));
    }

    @Background
    void getTwitter() {
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("tyVFkClEMvNhk9wXOzRFA")
                    .setOAuthConsumerSecret("Idp6VGJ0Fo5ASZ1qjrOlDyi8AdkPr5OAGnjeKKDVZQ")
                    .setOAuthAccessToken("43277931-5DGgSrw2wICiKEACrXLzOsCIVj979VQDtNUXOyuM1")
                    .setOAuthAccessTokenSecret("b2q8V8XAK45MI0f9cQcJmuOwch5jNbaAKjZyoq9BvQLjC");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            List<Status> statuses = twitter.getUserTimeline("cetsp_");
            updateFeeds(statuses);
        } catch (TwitterException e) {
            //FIXME: Melhorar tratamento de erros
            e.printStackTrace();
        }
    }

    @UiThread
    void updateFeeds(List<Status> statuses) {
        tvTwitter.setText(statuses.get(0).getText());
    }


    @Override
    public void onConnected(Bundle bundle) {

        Location location = mLocationClient.getLastLocation();
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

//        CameraUpdate center=
//                CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
//                        location.getLongitude()));
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