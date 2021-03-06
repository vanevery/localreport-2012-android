package com.mobvcasting.localreport2012;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AudioCall extends Activity implements View.OnClickListener
{
    private static final String LOGTAG = "AudioCall";

    private TwilioPhone phone;
    Button dialButton, hangupButton, backupButton;
    TextView callStatus;
    
    PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
                 
        setContentView(R.layout.activity_audio_call);
    	
    	dialButton = (Button) findViewById(R.id.dialButton);
    	dialButton.setEnabled(true);
        dialButton.setOnClickListener(this);
        
        hangupButton = (Button) findViewById(R.id.hangupButton);
        hangupButton.setEnabled(false);
        hangupButton.setOnClickListener(this);
        
        callStatus = (TextView) findViewById(R.id.callStatus);
        
        
        //backupButton = (Button)findViewById(R.id.backupButton);
        //backupButton.setOnClickListener(this);
        //backupButton.setVisibility(View.GONE);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (phone == null) {
    		phone = new TwilioPhone(getApplicationContext());
    		callStatus.setText("Ready");
        	Log.v(LOGTAG,"TwilioPhone Created");
    	}
    	else {
    		Log.v(LOGTAG,"Already have a TwilioPhone");
    		callStatus.setText("Ready");
    	}
    	
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "wakelock");
        // This will make the screen and power stay on
        // This will release the wakelook after 15 minutes
        wakeLock.acquire(1000 * 60 * 15);
    	
    }
    
    @Override
    public void onPause() {
    	Log.v(LOGTAG,"On Pause Called");
    	phone.disconnect();
    	callStatus.setText("Disconnecting");
    	wakeLock.release();
    	super.onPause();
    }
        
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view)
    {
        if (view == dialButton) {
        	dialButton.setEnabled(false);
            phone.connect();
            callStatus.setText("Connecting");
            hangupButton.setEnabled(true);
        } else if (view == hangupButton) {
        	hangupButton.setEnabled(false);
            phone.disconnect();
            callStatus.setText("Disconnecting");
            dialButton.setEnabled(true);
        	finish();
        } else if (view == backupButton) {
        	startActivity(new Intent(this, AudioCapture.class));
        	finish();
        }
    }
}