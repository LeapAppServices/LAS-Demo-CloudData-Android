package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.DeleteCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLLog;
import com.maxleap.MLObject;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationActivity extends LogActivity {

    private static final String TAG = OperationActivity.class.getSimpleName();
    private MLObject mSavedObject;
    private List<MLObject> mRelations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        initializeLogging();

        findViewById(R.id.create_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSavedObject = new MLObject("GamePlayer");
                mSavedObject.put("score", 1201);
                mSavedObject.put("name", "Peter");
                mSavedObject.put("isMale", true);
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish saving");
                                    MLLog.i(TAG, "score is " + mSavedObject.getInt("score"));
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.increment_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.increment("score");
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish saving");
                                    MLLog.i(TAG, "score is " + mSavedObject.getInt("score"));
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.add_unique_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.addUnique("uniqueArray", "a");
                mSavedObject.addAllUnique("uniqueArray",
                        Arrays.asList("a", "b", "c"));
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish adding unique");
                                    MLLog.i(TAG, "uniqueArray is " + mSavedObject.getList("uniqueArray"));
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.add("normalArray", "a");
                mSavedObject.addAll("normalArray", Arrays.asList("a", "b", "c"));
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish adding");
                                    MLLog.i(TAG, "normalArray is " + mSavedObject.getList("normalArray"));
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.remove_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.removeAll("normalArray", Arrays.asList("a"));
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish removing");
                                    MLLog.i(TAG, "normalArray is " + mSavedObject.getList("normalArray"));
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.delete_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }

                MLDataManager.deleteInBackground(mSavedObject,
                        new DeleteCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish deleting");
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.add_relation).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }

                final MLObject sub01 = new MLObject("sub");
                sub01.put("name", "sub01");
                final MLObject sub02 = new MLObject("sub");
                sub02.put("name", "sub02");
                final MLObject sub03 = new MLObject("sub");
                sub03.put("name", "sub03");
                if (mRelations == null) {
                    mRelations = new ArrayList<MLObject>();
                }
                mRelations.clear();
                mRelations.add(sub01);
                mRelations.add(sub02);
                mRelations.add(sub03);

                MLDataManager.saveAllInBackground(mRelations,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    mSavedObject.getRelation("Subs").add(sub01);
                                    mSavedObject.getRelation("Subs").add(sub02);
                                    MLDataManager.saveInBackground(
                                            mSavedObject, new SaveCallback() {

                                                @Override
                                                public void done(
                                                        MLException exception) {
                                                    if (exception == null) {
                                                        MLLog.d(TAG,
                                                                "finish adding relation");
                                                    } else {
                                                        MLLog.e(TAG, exception.getMessage());
                                                        exception.printStackTrace();
                                                    }
                                                }
                                            });
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.remove_relation).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    MLLog.t("Please create an object first.");
                    return;
                }
                if (mRelations == null || mRelations.isEmpty()) {
                    MLLog.d(TAG, "Please add the relation first.");
                    return;
                }
                mSavedObject.getRelation("Subs").remove(mRelations.get(0));
                MLDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish removing relation");
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
