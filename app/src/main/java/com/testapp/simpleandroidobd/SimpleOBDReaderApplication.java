package com.testapp.simpleandroidobd;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by PIERRE-LOUIS Antonio on 07/08/2015.
 */
public class SimpleOBDReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "YO3OwgDtN6CmoUhwnA2ykyJGXqXwiZsG0nNTbTdH", "NnAzJixJV1renn5g2V5n6ZZmZOH9HUMRRGWDXFhQ");
    }
}
