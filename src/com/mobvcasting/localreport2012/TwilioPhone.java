/*
 * Based on Twilio's HelloMonkey Android SDK Examples
 */

package com.mobvcasting.localreport2012;

import android.content.Context;
import android.util.Log;

import com.twilio.client.Device;
import com.twilio.client.Twilio;
import com.twilio.client.Connection;

public class TwilioPhone implements Twilio.InitListener
{
    private static final String LOGTAG = "TwilioPhone";

    private Device device;
    private Connection connection;
    private Context appContext;

    public TwilioPhone(Context context)
    {
    	Log.v(LOGTAG,"Calling Initialize on Twilio");
        Twilio.initialize(context, this /* Twilio.InitListener */);
        appContext = context;
    }

    /* Twilio.InitListener method */
    @Override
    public void onInitialized()
    {
        Log.v(LOGTAG, "Twilio SDK is Initialized");

        try {
            String capabilityToken = HttpHelper.httpGet(appContext.getString(R.string.twilio_auth_url));
            Log.v(LOGTAG, appContext.getString(R.string.twilio_auth_url) + " Auth Token: " + capabilityToken);
            
            device = Twilio.createDevice(capabilityToken, null /* DeviceListener */);
            Log.v(LOGTAG,"Created Twilio Device");
        } catch (Exception e) {
            Log.e(LOGTAG, "Failed to obtain capability token: " + e.getLocalizedMessage());
        }
    }

    /* Twilio.InitListener method */
    @Override
    public void onError(Exception e)
    {
        Log.v(LOGTAG, "Twilio SDK couldn't start: " + e.getLocalizedMessage());
    }

    public void connect()
    {
        connection = device.connect(null /* parameters */, null /* ConnectionListener */);
        if (connection == null)
            Log.v(LOGTAG, "Failed to create new connection");
    }
 
    public void disconnect()
    {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }   
    
    @Override
    protected void finalize()
    {
        if (connection != null)
            connection.disconnect();
        if (device != null)
            device.release();
    }    
}
