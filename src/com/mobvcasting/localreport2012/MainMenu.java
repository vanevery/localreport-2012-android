package com.mobvcasting.localreport2012;

import java.util.UUID;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener {

    private static String uniqueId = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

	Button videoButton, audioButton;
	TextView messageView;
	
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
        	CharSequence text = "No network connection!  This app requires a network connection.";
        	int duration = Toast.LENGTH_LONG;

        	Toast.makeText(context, text, duration).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
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
}
