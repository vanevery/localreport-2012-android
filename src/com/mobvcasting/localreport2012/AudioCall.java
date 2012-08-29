package com.mobvcasting.localreport2012;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AudioCall extends Activity implements View.OnClickListener
{
    private static final String LOGTAG = "AudioCall";

    private TwilioPhone phone;
    Button dialButton, hangupButton, backupButton;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    	initUI();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (phone == null) {
    		phone = new TwilioPhone(getApplicationContext());
        	Log.v(LOGTAG,"TwilioPhone Created");
    	}
    	else {
    		Log.v(LOGTAG,"Already have a TwilioPhone");
    	}
    }
    
    @Override
    public void onPause() {
    	Log.v(LOGTAG,"On Pause Called");
    	phone.disconnect();
    	super.onPause();
    }
        
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      initUI();
    }
    
    public void initUI() {
        setContentView(R.layout.activity_audio_call);
    	
    	dialButton = (Button) findViewById(R.id.dialButton);
    	dialButton.setEnabled(true);
        dialButton.setOnClickListener(this);
        
        hangupButton = (Button) findViewById(R.id.hangupButton);
        hangupButton.setEnabled(false);
        hangupButton.setOnClickListener(this);
        
        backupButton = (Button)findViewById(R.id.backupButton);
        backupButton.setOnClickListener(this);
        //backupButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view)
    {
        if (view == dialButton) {
        	dialButton.setEnabled(false);
            phone.connect();
            hangupButton.setEnabled(true);
        } else if (view == hangupButton) {
        	hangupButton.setEnabled(false);
            phone.disconnect();
            dialButton.setEnabled(true);
        	finish();
        } else if (view == backupButton) {
        	startActivity(new Intent(this, AudioCapture.class));
        	finish();
        }
    }
}