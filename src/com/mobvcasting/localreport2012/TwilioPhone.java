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

    public TwilioPhone(Context context)
    {
        Twilio.initialize(context, this /* Twilio.InitListener */);
    }

    /* Twilio.InitListener method */
    @Override
    public void onInitialized()
    {
        Log.d(LOGTAG, "Twilio SDK is ready");

        try {
            String capabilityToken = HttpHelper.httpGet("http://23.23.89.21:8080/twilio_auth.php");
            device = Twilio.createDevice(capabilityToken, null /* DeviceListener */);
        } catch (Exception e) {
            Log.e(LOGTAG, "Failed to obtain capability token: " + e.getLocalizedMessage());
        }
    }

    /* Twilio.InitListener method */
    @Override
    public void onError(Exception e)
    {
        Log.e(LOGTAG, "Twilio SDK couldn't start: " + e.getLocalizedMessage());
    }

    public void connect()
    {
        connection = device.connect(null /* parameters */, null /* ConnectionListener */);
        if (connection == null)
            Log.w(LOGTAG, "Failed to create new connection");
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
