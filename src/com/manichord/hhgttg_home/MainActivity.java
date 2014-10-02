package com.manichord.hhgttg_home;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // to disable the title bar of the application...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win = getWindow();
        win.addFlags(LayoutParams.FLAG_FULLSCREEN
                | LayoutParams.FLAG_DISMISS_KEYGUARD
                | LayoutParams.FLAG_TURN_SCREEN_ON);
        
        setContentView(R.layout.main);
        
        // System/Nav-bar off
        hideSystemBar();
        
    }

    private void hideSystemBar() {
        // TODO Auto-generated method stub
        
    }
}
