package com.mobvcasting.localreport2012;

import java.util.UUID;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener, LocationListener {

    private static String uniqueId = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public static Location currentLocation = null;
    
	Button videoButton, audioButton;
	TextView messageView;
	
	LocationManager locationManager;
	boolean gpsProviderReady = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main_menu);
        
        videoButton = (Button) this.findViewById(R.id.button1);
        audioButton = (Button) this.findViewById(R.id.button2);
        
        videoButton.setOnClickListener(this);
        audioButton.setOnClickListener(this);
        
        messageView = (TextView) this.findViewById(R.id.messageView);        
        
		messageView.setText("UUID: " + MainMenu.getUniqueId(this));
                
        if (noNetworkConnection()) {
        	Context context = getApplicationContext();
        	CharSequence text = "No network data connection!  This app requires a network data connection.  Please connect via WiFi or enable data.";
        	int duration = Toast.LENGTH_LONG;

        	Toast.makeText(context, text, duration).show();
        	
        	finish();
        }
        
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// 5 minutes, 50 meters
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000l, 50.0f, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000l, 50.0f, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
    }
	
	/*
	 Saving Battery life..  Not sure we want to do this
	 */
	public void onPause() {
		super.onPause(); 
		//locationManager.removeUpdates(this);
	}
    
	public void onResume() {
		super.onResume();
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000l, 5.0f, this);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000l, 5.0f, this);
	}
	
	public void onDestroy() {
		
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
		
		super.onDestroy();
	}
	
    private boolean noNetworkConnection(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return false;
		}
		return true;
    }

	@Override
	public void onClick(View v) {
		if (v == audioButton) {
    		startActivity(new Intent(this, AudioCall.class));
		} else if (v == videoButton) {
    		startActivity(new Intent(this, VideoCapture.class));			
		}
	}
    	
    
    public synchronized static String getUniqueId(Context context) {
        if (uniqueId == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueId = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueId == null) {
            	uniqueId = UUID.randomUUID().toString();
                Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueId);
                editor.commit();
            }
        }
        return uniqueId;
    }

	@Override
	public void onLocationChanged(Location location) {
		//location.getLatitude() + " " + location.getLongitude()); 		
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER) || !gpsProviderReady) {
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
				gpsProviderReady = true;
			} else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE || status == LocationProvider.OUT_OF_SERVICE) {
				gpsProviderReady = false;
			}
		}
	}
}
