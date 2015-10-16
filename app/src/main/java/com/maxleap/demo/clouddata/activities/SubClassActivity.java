package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.MLDataManager;
import com.maxleap.MLLog;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.entity.Student;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

import java.util.Calendar;

public class SubClassActivity extends LogActivity {

    private static final String TAG = SubClassActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_class);

        initializeLogging();

        findViewById(R.id.create_object_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Student student = new Student();
                student.setAge(15);
                student.setIsMale(true);
                student.setName("Peter");
                Calendar cal = Calendar.getInstance();
                cal.set(2000, 8, 4);
                student.setBirthday(cal.getTime());
                MLDataManager.saveInBackground(student, new SaveCallback() {

                    @Override
                    public void done(MLException exception) {
                        if (exception == null) {
                            MLLog.i(TAG, "finish saving student");
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
