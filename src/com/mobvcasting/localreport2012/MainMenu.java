package com.mobvcasting.localreport2012;

import java.util.UUID;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener {

	public static boolean TESTING = true;
	
	public static String LOGTAG = "LocalReportMain";
	
    private static String uniqueId = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    
	Button videoButton, audioButton;
	TextView messageView;
	
    private LocationTracker locationTracker;
	
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
        	Toast.makeText(getApplicationContext(), "No network data connection!  This app requires a network data connection.  Please connect via WiFi or enable data.", Toast.LENGTH_LONG).show();
        	finish();
        }
        
        bindService(new Intent(MainMenu.this, 
                LocationTracker.class), locationTrackerConnection, Context.BIND_AUTO_CREATE);        
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
		unbindService(locationTrackerConnection);
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

    private ServiceConnection locationTrackerConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            locationTracker = ((LocationTracker.LocalBinder)service).getService();

            if (TESTING) {
                // Tell the user about this for our demo.
            	Toast.makeText(MainMenu.this, "Connected", Toast.LENGTH_SHORT).show();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
        	locationTracker = null;
        	
        	if (TESTING) {
        		Toast.makeText(MainMenu.this, "Disconnected", Toast.LENGTH_SHORT).show();
        	}
        }
    };
}
