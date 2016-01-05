package in.movies.niranjan.com.freelance1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Niranjan on 12/2/2015.
 */
public class LocationSenderService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = LocationSenderService.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext = null;
    SharedPreferences prefs;
    static Location mLocation;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;//10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 20 * 1;//1000 * 10 * 1; // 10 seconds

    public LocationSenderService() {

    }

    public LocationSenderService(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        init();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    public void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(MIN_TIME_BW_UPDATES)
                .setFastestInterval(MIN_TIME_BW_UPDATES)
                .setSmallestDisplacement(0);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("DEBUGG", "Location services connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

        if (location != null) {
            mLocation = location;
            sendLocationData();
        } else {
            mGoogleApiClient.connect();
        }
    }

    public Location getLastLocation() {
       // Log.d("DEBUG_T", "get Last Location called " + mLocation);
        return mLocation;
    }

    public void sendLocationData() {
     //   Log.d("DEBUG_T2", locationInfo.toString() + "   " + getLastLocation());
        //TODO
        // Send locationd data to server
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        Log.d("DEBUG_T1", ""+mLocation);
        sendLocationData();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed : " + connectionResult);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        Log.d("DEBUG_NEWW","On Destroy called");
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}
