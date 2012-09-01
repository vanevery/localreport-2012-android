package com.mobvcasting.localreport2012;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationTracker extends Service implements LocationListener {

	public static String LOGTAG = "LocationTrackerService";
	public Location currentLocation = null;
    
	LocationManager locationManager;
	boolean gpsProviderReady = false;
	
	 @Override
    public void onCreate() {
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// 5 minutes, 50 meters
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000l, 50.0f, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000l, 50.0f, this);
	 
    }

	@Override
	public void onLocationChanged(Location location) {
		//location.getLatitude() + " " + location.getLongitude()); 		
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER) || !gpsProviderReady) {
			Log.v(LOGTAG,location.getProvider() + " " + location.getLatitude() + " " + location.getLongitude());
			
			currentLocation = location;
		}
	}

	@Override
	public void onProviderDisabled(String locationProvider) {
	}

	@Override
	public void onProviderEnabled(String locationProvider) {
	}

	@Override
	public void onStatusChanged(String locationProvider, int status, Bundle extras) {
		if (locationProvider.equals(LocationManager.GPS_PROVIDER)) {
			if (status == LocationProvider.AVAILABLE) {
				Log.v(LOGTAG,"GPS Available");

				gpsProviderReady = true;
			} else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE || status == LocationProvider.OUT_OF_SERVICE) {
				Log.v(LOGTAG,"GPS Unavailable");
								
				gpsProviderReady = false;
			}
		}
	}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        LocationTracker getService() {
            return LocationTracker.this;
        }
    }
}
