package as.leap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import as.leap.LASDataManager;
import as.leap.LASLog;
import as.leap.LASObject;
import as.leap.callback.DeleteCallback;
import as.leap.callback.GetCallback;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

public class CoreActivity extends LogActivity {

    public static final String TAG = CoreActivity.class.getSimpleName();

    private LASObject mSavedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        initializeLogging();

        findViewById(R.id.create_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSavedObject = new LASObject("GamePlayer");
                mSavedObject.put("score", 1200);
                mSavedObject.put("name", "Peter");
                mSavedObject.put("isMale", true);
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception != null) {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    LASLog.i(TAG, "finish saving");
                                }
                            }
                        });

            }
        });

        findViewById(R.id.get_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedObject == null) {
                    LASLog.t("Please create an object first");
                    return;
                }
                LASObject gamePlayer = new LASObject("GamePlayer");
                gamePlayer.setObjectId(mSavedObject.getObjectId());
                LASDataManager.fetchInBackground(gamePlayer,
                        new GetCallback<LASObject>() {

                            @Override
                            public void done(LASObject returnValue,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish fetching");
                                    LASLog.i(TAG, "name of getObject is " + returnValue.getString("name"));
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first");
                    return;
                }

                mSavedObject.put("name", "Jane");
                mSavedObject.put("isMale", false);
                LASDataManager.saveInBackground(mSavedObject,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish updating");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
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
                    LASLog.t("Please create an object first");
                    return;
                }

                LASDataManager.deleteInBackground(mSavedObject,
                        new DeleteCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish deleting");
                                    mSavedObject = null;
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