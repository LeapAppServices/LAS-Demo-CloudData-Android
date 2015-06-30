package as.leap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

import as.leap.LASDataManager;
import as.leap.LASLog;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.entity.Student;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

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
                LASDataManager.saveInBackground(student, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            LASLog.i(TAG, "finish saving student");
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
