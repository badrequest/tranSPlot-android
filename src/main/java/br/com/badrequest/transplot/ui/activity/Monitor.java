package br.com.badrequest.transplot.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import br.com.badrequest.transplot.R;
import br.com.badrequest.transplot.service.ActivityRecognitionIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import org.androidannotations.annotations.EActivity;

/**
 * Created by gmarques on 3/23/14.
 */
@EActivity(R.layout.monitor)
public class Monitor extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    LocationClient mLocationClient;

    // Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 5;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    private boolean mInProgress;

    /*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     */
    private PendingIntent mActivityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient mActivityRecognitionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //mLocationClient = new LocationClient(this, this, this);
        //mLocationClient.connect();
        mInProgress = false;
        mActivityRecognitionClient =
                new ActivityRecognitionClient(this, this, this);

        Intent intent = new Intent(this, ActivityRecognitionIntentService.class);

        mActivityRecognitionPendingIntent =
                PendingIntent.getService(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        startUpdates();

        super.onCreate(savedInstanceState);
    }

    /**
     * Request activity recognition updates based on the current
     * detection interval.
     *
     */
    public void startUpdates() {
        // Check for Google Play services

//        if (!servicesConnected()) {
//            return;
//        }
        // If a request is not already underway
        if (!mInProgress) {
            // Indicate that a request is in progress
            mInProgress = true;
            // Request a connection to Location Services
            mActivityRecognitionClient.connect();
            //
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mActivityRecognitionClient.requestActivityUpdates(
                DETECTION_INTERVAL_MILLISECONDS,
                mActivityRecognitionPendingIntent);
        mInProgress = false;
        //mActivityRecognitionClient.disconnect();

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

    @Override
    public void onLocationChanged(Location location) {

    }
}