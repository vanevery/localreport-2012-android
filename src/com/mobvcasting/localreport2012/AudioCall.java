package com.mobvcasting.localreport2012;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AudioCall extends Activity implements View.OnClickListener
{
    private TwilioPhone phone;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_audio_capture);

        phone = new TwilioPhone(getApplicationContext());

        ImageButton dialButton = (ImageButton)findViewById(R.id.dialButton);
        dialButton.setOnClickListener(this);

        ImageButton hangupButton = (ImageButton)findViewById(R.id.hangupButton);
        hangupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.dialButton)
            phone.connect();
        else if (view.getId() == R.id.hangupButton)
            phone.disconnect();
    }
}