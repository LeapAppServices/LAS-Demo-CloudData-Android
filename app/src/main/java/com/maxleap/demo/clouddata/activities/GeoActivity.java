package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.FindCallback;
import com.maxleap.LocationCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLGeoPoint;
import com.maxleap.MLLocationManager;
import com.maxleap.MLLog;
import com.maxleap.MLObject;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

import java.util.Arrays;
import java.util.List;

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
                MLObject obj1 = new MLObject("PlaceObject");
                MLGeoPoint point = new MLGeoPoint(40, -30);
                obj1.put("location", point);

                MLObject obj2 = new MLObject("PlaceObject");
                MLGeoPoint point2 = new MLGeoPoint(80, -110);
                obj2.put("location", point2);

                MLObject obj3 = new MLObject("PlaceObject");
                MLGeoPoint point3 = new MLGeoPoint(38, -31);
                obj3.put("location", point3);

                MLDataManager.saveAllInBackground(
                        Arrays.asList(obj1, obj2, obj3), new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish saving");
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.query_geo_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLGeoPoint point = new MLGeoPoint(38, 30);
                MLQuery<MLObject> query = MLQuery.getQuery("PlaceObject");
                query.whereNear("location", point);
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<MLObject>() {

                            @Override
                            public void done(List<MLObject> objects,
                                             MLException exception) {
                                if (exception != null) {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    MLLog.i(TAG, "finish querying");
                                    for (MLObject object : objects) {
                                        MLLog.i(TAG, "location-->" + object.getMLGeoPoint("location"));
                                    }
                                }
                            }
                        });
            }
        });

        findViewById(R.id.query_box_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<MLObject> query = MLQuery.getQuery("PlaceObject");
                MLGeoPoint southwest = new MLGeoPoint(39, -40);
                MLGeoPoint northeast = new MLGeoPoint(-39, 40);
                query.whereWithinGeoBox("location", southwest, northeast);

                MLQueryManager.findAllInBackground(query,
                        new FindCallback<MLObject>() {

                            @Override
                            public void done(List<MLObject> objects,
                                             MLException exception) {
                                if (exception != null) {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    MLLog.i(TAG, "finish querying");
                                    for (MLObject object : objects) {
                                        MLLog.i(TAG, "location-->" + object.getMLGeoPoint("location"));
                                    }
                                }
                            }
                        });
            }
        });

        findViewById(R.id.get_location_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLLocationManager.getCurrentLocationInBackground(30000,
                        new LocationCallback() {

                            @Override
                            public void done(MLGeoPoint geoPoint,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish getting location");
                                    MLLog.i(
                                            TAG,
                                            "latitude is "
                                                    + geoPoint.getLatitude());
                                    MLLog.i(
                                            TAG,
                                            "longitude is "
                                                    + geoPoint.getLongitude());
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });
    }
}
