package com.maxleap.demo.clouddata;

import android.app.Application;

import com.maxleap.MLObject;
import com.maxleap.MaxLeap;
import com.maxleap.demo.clouddata.entity.Hero;
import com.maxleap.demo.clouddata.entity.Student;
import com.maxleap.demo.clouddata.entity.Teacher;

public class App extends Application {

    public static final String APP_ID = "Replace this with your App Id";
    public static final String API_KEY = "Replace this with your Rest Key";

    @Override
    public void onCreate() {
        super.onCreate();

        if (APP_ID.startsWith("Replace") || API_KEY.startsWith("Replace")) {
            throw new IllegalArgumentException("Please replace with your app id and api key first before" +
                    "using MaxLeap SDK.");
        }

        MaxLeap.setLogLevel(MaxLeap.LOG_LEVEL_INFO);
        MaxLeap.initialize(this, APP_ID, API_KEY);

        MLObject.registerSubclass(Student.class);
        MLObject.registerSubclass(Teacher.class);
        MLObject.registerSubclass(Hero.class);
    }
}