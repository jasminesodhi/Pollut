package com.jasminesodhi.randomhacksofkindness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jasminesodhi on 16/07/17.
 */

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread timerThread = new Thread(){
            public void run(){
                try{

                    //timer for the splash
                    sleep(1200);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    //intent from splash to main activity
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        timerThread.start();
    }

    @Override
    protected void onPause() {

        //write background services here
        super.onPause();
        finish();
    }
}
