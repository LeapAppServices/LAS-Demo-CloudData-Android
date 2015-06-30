package as.leap.demo.clouddata.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Date;

import as.leap.LASDataManager;
import as.leap.LASGeoPoint;
import as.leap.LASLog;
import as.leap.LASObject;
import as.leap.LASRelation;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

public class DataTypeActivity extends LogActivity {

    private static final String TAG = DataTypeActivity.class.getSimpleName();

    private LASObject mSavedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_type);

        initializeLogging();

        findViewById(R.id.primitive_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSavedObject = new LASObject("TestDataType");
                mSavedObject.put("foo", "bar");
                mSavedObject.put("x", 1);
                LASLog.d("x is ", "" + mSavedObject.getInt("x"));
                LASLog.d("foo is ", "" + mSavedObject.getString("bar"));
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

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

        findViewById(R.id.date_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Date now = new Date();
                final LASObject dateType = new LASObject("TestDataType");
                dateType.put("now", now);
                LASLog.d("foo is ", "" + dateType.getDate("noew"));
                LASDataManager.saveInBackground(dateType, new SaveCallback() {

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


        findViewById(R.id.pointer_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null
                        || TextUtils.isEmpty(mSavedObject.getObjectId())) {
                    LASLog.t("Please click primitive button first.");
                    return;
                }

                final LASObject pointerType = new LASObject("TestDataType");
                pointerType.put("pointer", mSavedObject);
                LASDataManager.saveInBackground(pointerType, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            LASLog.i(TAG, "finish saving");

                            LASObject pointerObject = pointerType.getLASObject("pointer");
                            LASLog.i(TAG, "x is " + pointerObject.getInt("x"));

                        } else {
                            LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please click primitive button first.");
                    return;
                }

                final LASObject dataType = new LASObject("TestDataType");
                dataType.getRelation("relation").add(mSavedObject);
                LASDataManager.saveInBackground(dataType, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            LASLog.i(TAG, "finish saving");

                            LASRelation<LASObject> relation = dataType.getRelation("relation");
                            relation.remove(mSavedObject);
                            LASDataManager.saveInBackground(dataType);
                        } else {
                            LASLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });


        findViewById(R.id.geo_point_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final LASObject dataType = new LASObject("TestDataType");
                LASGeoPoint geoPoint = new LASGeoPoint(30, 25.2);
                dataType.put("location", geoPoint);
                LASDataManager.saveInBackground(dataType, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            LASLog.d(TAG, "finish saving");

                            LASGeoPoint location = dataType.getLASGeoPoint("location");
                            LASLog.i(TAG, "lat is " + location.getLatitude());
                            LASLog.i(TAG, "long is " + location.getLongitude());

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
