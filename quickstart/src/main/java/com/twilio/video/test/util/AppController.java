package com.twilio.video.test.util;

import android.app.Application;

public class AppController extends Application {

    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        new UserPref(getApplicationContext());
    }

    public static AppController getInstance(){

      /*  if (instance==null){
            instance=this;
        }*/

        return  instance;
    }
}
