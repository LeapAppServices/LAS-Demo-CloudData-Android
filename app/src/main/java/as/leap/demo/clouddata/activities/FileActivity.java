package as.leap.demo.clouddata.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import as.leap.LASDataManager;
import as.leap.LASFile;
import as.leap.LASFileManager;
import as.leap.LASLog;
import as.leap.LASObject;
import as.leap.LASQueryManager;
import as.leap.callback.GetCallback;
import as.leap.callback.GetDataCallback;
import as.leap.callback.ProgressCallback;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

public class FileActivity extends LogActivity {

    public static final String TAG = FileActivity.class.getSimpleName();

    private LASObject mSavedTextObject;

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(FileActivity.this);
        progressDialog.setProgress(0);
        progressDialog.setMessage("Load...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                LASFileManager.cancel();
                dialog.cancel();
            }
        });
        progressDialog.show();
        return progressDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        initializeLogging();

        findViewById(R.id.upload_text_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                byte[] data = ("My name is LAS!" + System
                        .currentTimeMillis()).getBytes();
                final LASFile file = new LASFile("resume.txt", data);
                final ProgressDialog progressDialog = createProgressDialog();
                LASFileManager.saveInBackground(file, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        progressDialog.dismiss();
                        if (exception != null) {
                            LASLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                            return;
                        }
                        putTextIntoObject(file);
                    }
                }, new ProgressCallback() {

                    @Override
                    public void done(int percentDone) {
                        progressDialog.setProgress(percentDone);
                    }
                });
            }
        });

        findViewById(R.id.download_text_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSavedTextObject == null) {
                    LASLog.t("Please upload the text first.");
                    return;
                }

                LASQueryManager.getInBackground(
                        mSavedTextObject.getClassName(),
                        mSavedTextObject.getObjectId(),
                        new GetCallback<LASObject>() {

                            @Override
                            public void done(LASObject objectAgain,
                                             LASException exception) {
                                downloadText(objectAgain);
                            }
                        });

            }
        });

    }

    private void putTextIntoObject(LASFile file) {
        mSavedTextObject = new LASObject("JobApplication");
        mSavedTextObject.put("applicationName", "Joe Smith");
        mSavedTextObject.put("attachment", file);
        LASDataManager.saveInBackground(mSavedTextObject, new SaveCallback() {

            @Override
            public void done(LASException exception) {
                if (exception != null) {
                    LASLog.e(TAG, exception.getMessage());
                    exception.printStackTrace();
                    return;
                }
                LASLog.i(TAG, "finish saving");
            }
        });
    }

    private void downloadText(final LASObject obj) {
        final ProgressDialog progressDialog = createProgressDialog();
        LASFile file = obj.getLASFile("attachment");

        LASFileManager.getDataInBackground(file, new GetDataCallback() {

            @Override
            public void done(final byte[] object, LASException exception) {
                progressDialog.dismiss();
                if (exception != null) {
                    LASLog.e(TAG, exception.getMessage());
                    exception.printStackTrace();
                    return;
                }
                LASLog.i(TAG, "finish downloading");
                LASLog.i(TAG, new String(object));
            }
        }, new ProgressCallback() {

            @Override
            public void done(int percentDone) {
                progressDialog.setProgress(percentDone);
            }
        });
    }

}