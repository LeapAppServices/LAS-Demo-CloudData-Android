package as.leap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import as.leap.LASDataManager;
import as.leap.LASLog;
import as.leap.LASObject;
import as.leap.callback.DeleteCallback;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

public class OperationActivity extends LogActivity {

    private static final String TAG = OperationActivity.class.getSimpleName();
    private LASObject mSavedObject;
    private List<LASObject> mRelations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        initializeLogging();

        findViewById(R.id.create_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSavedObject = new LASObject("GamePlayer");
                mSavedObject.put("score", 1201);
                mSavedObject.put("name", "Peter");
                mSavedObject.put("isMale", true);
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish saving");
                                    LASLog.i(TAG, "score is " + mSavedObject.getInt("score"));
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.increment("score");
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish saving");
                                    LASLog.i(TAG, "score is " + mSavedObject.getInt("score"));
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.addUnique("uniqueArray", "a");
                mSavedObject.addAllUnique("uniqueArray",
                        Arrays.asList("a", "b", "c"));
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish adding unique");
                                    LASLog.i(TAG, "uniqueArray is " + mSavedObject.getList("uniqueArray"));
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.add("normalArray", "a");
                mSavedObject.addAll("normalArray", Arrays.asList("a", "b", "c"));
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish adding");
                                    LASLog.i(TAG, "normalArray is " + mSavedObject.getList("normalArray"));
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }

                mSavedObject.removeAll("normalArray", Arrays.asList("a"));
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish removing");
                                    LASLog.i(TAG, "normalArray is " + mSavedObject.getList("normalArray"));
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }

                LASDataManager.deleteInBackground(mSavedObject,
                        new DeleteCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish deleting");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }

                final LASObject sub01 = new LASObject("sub");
                sub01.put("name", "sub01");
                final LASObject sub02 = new LASObject("sub");
                sub02.put("name", "sub02");
                final LASObject sub03 = new LASObject("sub");
                sub03.put("name", "sub03");
                if (mRelations == null) {
                    mRelations = new ArrayList<LASObject>();
                }
                mRelations.clear();
                mRelations.add(sub01);
                mRelations.add(sub02);
                mRelations.add(sub03);

                LASDataManager.saveAllInBackground(mRelations,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    mSavedObject.getRelation("Subs").add(sub01);
                                    mSavedObject.getRelation("Subs").add(sub02);
                                    LASDataManager.saveInBackground(
                                            mSavedObject, new SaveCallback() {

                                                @Override
                                                public void done(
                                                        LASException exception) {
                                                    if (exception == null) {
                                                        LASLog.d(TAG,
                                                                "finish adding relation");
                                                    } else {
                                                        LASLog.e(TAG, exception.getMessage());
                                                        exception.printStackTrace();
                                                    }
                                                }
                                            });
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first.");
                    return;
                }
                if (mRelations == null || mRelations.isEmpty()) {
                    LASLog.d(TAG, "Please add the relation first.");
                    return;
                }
                mSavedObject.getRelation("Subs").remove(mRelations.get(0));
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish removing relation");
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
