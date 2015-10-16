package com.maxleap.demo.clouddata.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.GetCallback;
import com.maxleap.GetDataCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLFile;
import com.maxleap.MLFileManager;
import com.maxleap.MLLog;
import com.maxleap.MLObject;
import com.maxleap.MLQueryManager;
import com.maxleap.ProgressCallback;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

public class FileActivity extends LogActivity {

    public static final String TAG = FileActivity.class.getSimpleName();

    private MLObject mSavedTextObject;

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(FileActivity.this);
        progressDialog.setProgress(0);
        progressDialog.setMessage("Load...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                MLFileManager.cancel();
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
                byte[] data = ("My name is ML!" + System
                        .currentTimeMillis()).getBytes();
                final MLFile file = new MLFile("resume.txt", data);
                final ProgressDialog progressDialog = createProgressDialog();
                MLFileManager.saveInBackground(file, new SaveCallback() {

                    @Override
                    public void done(MLException exception) {
                        progressDialog.dismiss();
                        if (exception != null) {
                            MLLog.e(TAG, exception.getMessage());
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
                    MLLog.t("Please upload the text first.");
                    return;
                }

                MLQueryManager.getInBackground(
                        mSavedTextObject.getClassName(),
                        mSavedTextObject.getObjectId(),
                        new GetCallback<MLObject>() {

                            @Override
                            public void done(MLObject objectAgain,
                                             MLException exception) {
                                downloadText(objectAgain);
                            }
                        });

            }
        });

    }

    private void putTextIntoObject(MLFile file) {
        mSavedTextObject = new MLObject("JobApplication");
        mSavedTextObject.put("applicationName", "Joe Smith");
        mSavedTextObject.put("attachment", file);
        MLDataManager.saveInBackground(mSavedTextObject, new SaveCallback() {

            @Override
            public void done(MLException exception) {
                if (exception != null) {
                    MLLog.e(TAG, exception.getMessage());
                    exception.printStackTrace();
                    return;
                }
                MLLog.i(TAG, "finish saving");
            }
        });
    }

    private void downloadText(final MLObject obj) {
        final ProgressDialog progressDialog = createProgressDialog();
        MLFile file = obj.getMLFile("attachment");

        MLFileManager.getDataInBackground(file, new GetDataCallback() {

            @Override
            public void done(final byte[] object, MLException exception) {
                progressDialog.dismiss();
                if (exception != null) {
                    MLLog.e(TAG, exception.getMessage());
                    exception.printStackTrace();
                    return;
                }
                MLLog.i(TAG, "finish downloading");
                MLLog.i(TAG, new String(object));
            }
        }, new ProgressCallback() {

            @Override
            public void done(int percentDone) {
                progressDialog.setProgress(percentDone);
            }
        });
    }

}