package com.manichord.hhgttg_home;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends Activity implements OnClickListener
{
    protected static final String LOGTAG = MainActivity.class.getSimpleName();
    private View rebootButton;
    private View dontpanicButton;
    private View fiveghzButton;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // to disable the title bar of the application...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win = getWindow();
        win.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD
                | LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.main);
        
        dontpanicButton = findViewById(R.id.dont_panic_text);
        dontpanicButton.setOnClickListener(this);
        rebootButton = findViewById(R.id.reboot_button);
        rebootButton.setOnClickListener(this);
        fiveghzButton = findViewById(R.id.fiveghz_button);
        fiveghzButton.setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.main_view).setSystemUiVisibility(
        		View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v == rebootButton) {
            Log.d(LOGTAG, "reboot NOW");
            PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
            powerManager.reboot(null);
        } else if (v == dontpanicButton) {
            startActivity(new Intent("android.settings.SETTINGS"));
        } else if (v == fiveghzButton) {
            setWifiFreqBand(v.getContext().getApplicationContext());
        }
    }
    
    /**
     * Example of calling a hidden method in WifiManager
     * @param cxt
     */
    private void setWifiFreqBand(Context cxt) {
        // Copy/paste hiddens from WifiManager class
        /**
         * Auto settings in the driver. The driver could choose to operate on both
         * 2.4 GHz and 5 GHz or make a dynamic decision on selecting the band.
         */
        final int WIFI_FREQUENCY_BAND_AUTO = 0;
        final int WIFI_FREQUENCY_BAND_5GHZ = 1;
        final int WIFI_FREQUENCY_BAND_2GHZ = 2;
        final String hiddenSetFreqMethodName = "setFrequencyBand";
        int band = WIFI_FREQUENCY_BAND_5GHZ;
        boolean persist = true;
        
        WifiManager wifiMgr = (WifiManager) cxt.getSystemService(Context.WIFI_SERVICE);
        Method method;
        try {
            method = wifiMgr.getClass().getMethod(hiddenSetFreqMethodName, int.class, boolean.class);
            method.invoke(wifiMgr, band, persist);
            Log.i(LOGTAG, "set Wifi band to 5Ghz");
        } catch (Exception e) {
            Log.e(LOGTAG, "", e);
        }
    }
}
