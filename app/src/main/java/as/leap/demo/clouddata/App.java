package as.leap.demo.clouddata;

import android.app.Application;

import as.leap.LASConfig;
import as.leap.LASObject;
import as.leap.demo.clouddata.entity.Hero;
import as.leap.demo.clouddata.entity.Student;
import as.leap.demo.clouddata.entity.Teacher;

public class App extends Application {

    public static final String APP_ID = "Replace this with your App Id";
    public static final String API_KEY = "Replace this with your Rest Key";

    @Override
    public void onCreate() {
        super.onCreate();

        if (APP_ID.startsWith("Replace") || API_KEY.startsWith("Replace")) {
            throw new IllegalArgumentException("Please replace with your app id and api key first before" +
                    "using LAS SDK.");
        }

        LASConfig.setLogLevel(LASConfig.LOG_LEVEL_INFO);
        LASConfig.initialize(this, APP_ID, API_KEY);

        LASObject.registerSubclass(Student.class);
        LASObject.registerSubclass(Teacher.class);
        LASObject.registerSubclass(Hero.class);
    }
}