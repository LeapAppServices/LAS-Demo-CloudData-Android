package as.leap.demo.clouddata.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import as.leap.LASDataManager;
import as.leap.LASGeoPoint;
import as.leap.LASLocationManager;
import as.leap.LASLog;
import as.leap.LASObject;
import as.leap.LASQuery;
import as.leap.LASQueryManager;
import as.leap.callback.FindCallback;
import as.leap.callback.LocationCallback;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

public class GeoActivity extends LogActivity {

    public static final String TAG = GeoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        initializeLogging();

        findViewById(R.id.create_geo_objects_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASObject obj1 = new LASObject("PlaceObject");
                LASGeoPoint point = new LASGeoPoint(40, -30);
                obj1.put("location", point);

                LASObject obj2 = new LASObject("PlaceObject");
                LASGeoPoint point2 = new LASGeoPoint(80, -110);
                obj2.put("location", point2);

                LASObject obj3 = new LASObject("PlaceObject");
                LASGeoPoint point3 = new LASGeoPoint(38, -31);
                obj3.put("location", point3);

                LASDataManager.saveAllInBackground(
                        Arrays.asList(obj1, obj2, obj3), new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish saving");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.query_geo_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASGeoPoint point = new LASGeoPoint(38, 30);
                LASQuery<LASObject> query = LASQuery.getQuery("PlaceObject");
                query.whereNear("location", point);
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<LASObject>() {

                            @Override
                            public void done(List<LASObject> objects,
                                             LASException exception) {
                                if (exception != null) {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    LASLog.i(TAG, "finish querying");
                                    for (LASObject object : objects) {
                                        LASLog.i(TAG, "location-->" + object.getLASGeoPoint("location"));
                                    }
                                }
                            }
                        });
            }
        });

        findViewById(R.id.query_box_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<LASObject> query = LASQuery.getQuery("PlaceObject");
                LASGeoPoint southwest = new LASGeoPoint(39, -40);
                LASGeoPoint northeast = new LASGeoPoint(-39, 40);
                query.whereWithinGeoBox("location", southwest, northeast);

                LASQueryManager.findAllInBackground(query,
                        new FindCallback<LASObject>() {

                            @Override
                            public void done(List<LASObject> objects,
                                             LASException exception) {
                                if (exception != null) {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    LASLog.i(TAG, "finish querying");
                                    for (LASObject object : objects) {
                                        LASLog.i(TAG, "location-->" + object.getLASGeoPoint("location"));
                                    }
                                }
                            }
                        });
            }
        });

        findViewById(R.id.get_location_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASLocationManager.getCurrentLocationInBackground(30000,
                        new LocationCallback() {

                            @Override
                            public void done(LASGeoPoint geoPoint,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish getting location");
                                    LASLog.i(
                                            TAG,
                                            "latitude is "
                                                    + geoPoint.getLatitude());
                                    LASLog.i(
                                            TAG,
                                            "longitude is "
                                                    + geoPoint.getLongitude());
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });
    }
}
