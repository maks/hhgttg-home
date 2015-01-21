package com.manichord.hhgttg_home;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends Activity implements OnClickListener
{
    protected static final String LOGTAG = MainActivity.class.getSimpleName();
    private View rebootButton;
    private View dontpanicButton;
    private View fiveghzButton;
    private View dimScreen;
    private boolean dimmed = false;
    private View testMaxVol;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // to disable the title bar of the application...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win = getWindow();
        win.addFlags(
                // dont show lockscreen aka keyguard if not using secure keyguard
                LayoutParams.FLAG_DISMISS_KEYGUARD
                // turn screen on if its not on
                | LayoutParams.FLAG_TURN_SCREEN_ON
                //stop screen from turning off
                | LayoutParams.FLAG_KEEP_SCREEN_ON);
        // clear the keep screen with:
        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        setContentView(R.layout.main);
        
        dontpanicButton = findViewById(R.id.dont_panic_text);
        dontpanicButton.setOnClickListener(this);
        rebootButton = findViewById(R.id.reboot_button);
        rebootButton.setOnClickListener(this);
        fiveghzButton = findViewById(R.id.fiveghz_button);
        fiveghzButton.setOnClickListener(this);
        dimScreen = findViewById(R.id.dimscreen_button);
        dimScreen.setOnClickListener(this);
        testMaxVol = findViewById(R.id.max_vol_test_button);
        testMaxVol.setOnClickListener(this);
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
        
        NTPTest();
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
        } else if (v == dimScreen) {
            int dimBy = dimmed ? 100 : 0;
            dimTheScreen(dimBy);
            dimmed = !dimmed;
        } else if (v == testMaxVol) {
           testSetMaxVolume(v.getContext().getApplicationContext());
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
    
    /**
     * Dim screen
     * @param dimAmount how much to dim screen by 0 = fully dim, 100 = fully bright
     */
    private void dimTheScreen(int dimAmount) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness =  0.01f + dimAmount / 100.0f; //add 0.1 because 0 would turn screen fully off
        getWindow().setAttributes(lp);
    }
    
    private void testSetMaxVolume(Context cxt) {
        AudioManager audioManager = (AudioManager) cxt.getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(LOGTAG, "setting MAX volume to:"+maxVol);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
        Log.d(LOGTAG, "volume now:"+audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }
    
    private void NTPTest() {
        final Resources res = this.getResources();
        final int id = Resources.getSystem().getIdentifier(
                           "config_ntpServer", "string","android");
        final String defaultServer = res.getString(id);
        Log.i(LOGTAG, "USING NTP:"+defaultServer);
    }
}
