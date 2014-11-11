package com.manichord.hhgttg_home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
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
    private View playButton;
    private MediaPlayer player;
    private View pauseButton;

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
        
        findViewById(R.id.dont_panic_text).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.SETTINGS"));
            }
        });
        
        rebootButton = findViewById(R.id.reboot_button);
        rebootButton.setOnClickListener(this);
        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        pauseButton = findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(this);
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
        } else if (v == playButton) {
            try {
                if (player != null && !player.isPlaying()) {
                    Log.d(LOGTAG, "PLAY AUDIO");
                    AssetFileDescriptor afd = getAssets().openFd("test.ogg");
                    player = new MediaPlayer();
                    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    player.prepare();
                    player.start();
                }
            } catch (Exception e) {
                Log.e(LOGTAG, "error playing audio", e);
            }
        } else if (v == pauseButton) {
            if (player != null && player.isPlaying()) {
                player.pause();
            }
        }
    }
}
