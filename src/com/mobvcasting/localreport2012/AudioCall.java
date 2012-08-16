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
    	phone = new TwilioPhone(getApplicationContext());
    	Log.v(LOGTAG,"TwilioPhone Created");
    
    }
    
    @Override
    public void onPause() {
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
        dialButton.setOnClickListener(this);

        hangupButton = (Button) findViewById(R.id.hangupButton);
        hangupButton.setOnClickListener(this);
        
        backupButton = (Button)findViewById(R.id.backupButton);
        backupButton.setOnClickListener(this);
        backupButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view)
    {
        if (view == dialButton) {
            phone.connect();
        } else if (view == hangupButton) {
            phone.disconnect();
        	finish();
        } else if (view == backupButton) {
        	startActivity(new Intent(this, AudioCapture.class));
        	finish();
        }
    }
    
}