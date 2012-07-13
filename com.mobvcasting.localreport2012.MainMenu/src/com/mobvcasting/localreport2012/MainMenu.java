package com.mobvcasting.localreport2012;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener {

	Button videoButton, audioButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        videoButton = (Button) this.findViewById(R.id.button1);
        audioButton = (Button) this.findViewById(R.id.button2);
        
        videoButton.setOnClickListener(this);
        audioButton.setOnClickListener(this);
    }


	@Override
	public void onClick(View v) {
		if (v == audioButton) {
    		//startActivity(new Intent(this, AudioCapture.class));
		} else if (v == videoButton) {
    		startActivity(new Intent(this, VideoCapture.class));			
		}
	}

    
}
