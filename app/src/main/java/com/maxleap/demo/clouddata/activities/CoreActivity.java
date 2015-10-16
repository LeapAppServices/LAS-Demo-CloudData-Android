package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.DeleteCallback;
import com.maxleap.GetCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLLog;
import com.maxleap.MLObject;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

public class CoreActivity extends LogActivity {

    public static final String TAG = CoreActivity.class.getSimpleName();

    private MLObject mSavedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        initializeLogging();

        findViewById(R.id.create_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSavedObject = new MLObject("GamePlayer");
                mSavedObject.put("score", 1200);
                mSavedObject.put("name", "Peter");
                mSavedObject.put("isMale", true);
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception != null) {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    MLLog.i(TAG, "finish saving");
                                }
                            }
                        });

            }
        });

        findViewById(R.id.get_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first");
                    return;
                }
                MLObject gamePlayer = new MLObject("GamePlayer");
                gamePlayer.setObjectId(mSavedObject.getObjectId());
                MLDataManager.fetchInBackground(gamePlayer,
                        new GetCallback<MLObject>() {

                            @Override
                            public void done(MLObject returnValue,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish fetching");
                                    MLLog.i(TAG, "name of getObject is " + returnValue.getString("name"));
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.update_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first");
                    return;
                }

                mSavedObject.put("name", "Jane");
                mSavedObject.put("isMale", false);
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish updating");
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.delete_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first");
                    return;
                }

                MLDataManager.deleteInBackground(mSavedObject,
                        new DeleteCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish deleting");
                                    mSavedObject = null;
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