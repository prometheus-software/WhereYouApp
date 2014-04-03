package com.whereyouapp.ufl.edu;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
 
public class MainSplashScreen extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);  
         
         /****** Create Thread that will sleep for 2 seconds *************/        
        Thread background = new Thread() {
            public void run() {
                 
                try {
                    // Thread will sleep for 2 seconds
                    sleep(3000);
                     
                    // After 2 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(), SavedRoutesScreen.class);
                    startActivity(i);
                     
                    //Remove activity
                    finish();
                     
                } catch (Exception e) {
                 
                }
            }
        };
         
        // start thread
        background.start();
    }
     
    @Override
    protected void onDestroy() {
         
        super.onDestroy();
         
    }
    @Override
    public void onBackPressed()
    {
    	return;
    }
}
