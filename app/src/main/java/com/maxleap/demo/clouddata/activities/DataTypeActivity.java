package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Date;

import com.maxleap.MLDataManager;
import com.maxleap.MLGeoPoint;
import com.maxleap.MLLog;
import com.maxleap.MLObject;
import com.maxleap.MLRelation;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

public class DataTypeActivity extends LogActivity {

    private static final String TAG = DataTypeActivity.class.getSimpleName();

    private MLObject mSavedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_type);

        initializeLogging();

        findViewById(R.id.primitive_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSavedObject = new MLObject("TestDataType");
                mSavedObject.put("foo", "bar");
                mSavedObject.put("x", 1);
                MLLog.d("x is ", "" + mSavedObject.getInt("x"));
                MLLog.d("foo is ", "" + mSavedObject.getString("bar"));
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

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

        findViewById(R.id.date_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Date now = new Date();
                final MLObject dateType = new MLObject("TestDataType");
                dateType.put("now", now);
                MLLog.d("foo is ", "" + dateType.getDate("noew"));
                MLDataManager.saveInBackground(dateType, new SaveCallback() {

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


        findViewById(R.id.pointer_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null
                        || TextUtils.isEmpty(mSavedObject.getObjectId())) {
                    MLLog.t("Please click primitive button first.");
                    return;
                }

                final MLObject pointerType = new MLObject("TestDataType");
                pointerType.put("pointer", mSavedObject);
                MLDataManager.saveInBackground(pointerType, new SaveCallback() {

                    @Override
                    public void done(MLException exception) {
                        if (exception == null) {
                            MLLog.i(TAG, "finish saving");

                            MLObject pointerObject = pointerType.getMLObject("pointer");
                            MLLog.i(TAG, "x is " + pointerObject.getInt("x"));

                        } else {
                            MLLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.relation_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null
                        || TextUtils.isEmpty(mSavedObject.getObjectId())) {
                    MLLog.t("Please click primitive button first.");
                    return;
                }

                final MLObject dataType = new MLObject("TestDataType");
                dataType.getRelation("relation").add(mSavedObject);
                MLDataManager.saveInBackground(dataType, new SaveCallback() {

                    @Override
                    public void done(MLException exception) {
                        if (exception == null) {
                            MLLog.i(TAG, "finish saving");

                            MLRelation<MLObject> relation = dataType.getRelation("relation");
                            relation.remove(mSavedObject);
                            MLDataManager.saveInBackground(dataType);
                        } else {
                            MLLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });


        findViewById(R.id.geo_point_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final MLObject dataType = new MLObject("TestDataType");
                MLGeoPoint geoPoint = new MLGeoPoint(30, 25.2);
                dataType.put("location", geoPoint);
                MLDataManager.saveInBackground(dataType, new SaveCallback() {

                    @Override
                    public void done(MLException exception) {
                        if (exception == null) {
                            MLLog.d(TAG, "finish saving");

                            MLGeoPoint location = dataType.getMLGeoPoint("location");
                            MLLog.i(TAG, "lat is " + location.getLatitude());
                            MLLog.i(TAG, "long is " + location.getLongitude());

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
