package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.LogInCallback;
import com.maxleap.MLAnonymousUtils;
import com.maxleap.MLLog;
import com.maxleap.MLUser;
import com.maxleap.MLUserManager;
import com.maxleap.RequestEmailVerifyCallback;
import com.maxleap.RequestPasswordResetCallback;
import com.maxleap.SaveCallback;
import com.maxleap.SignUpCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

public class UserActivity extends LogActivity {

    public static final String TAG = UserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initializeLogging();

        findViewById(R.id.sign_up_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final MLUser user = new MLUser();
                user.setUserName("AndroidUser");
                user.setPassword("password");
                user.setEmail("android@example.com");
                MLUserManager.signUpInBackground(user, new SignUpCallback() {

                    @Override
                    public void done(MLException exception) {
                        if (exception == null) {
                            MLLog.i(TAG, "finish signing up");
                        } else {
                            MLLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLUserManager.logInInBackground("AndroidUser",
                        "password", new LogInCallback<MLUser>() {

                            @Override
                            public void done(MLUser user,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish logging in");
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.anonymous_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLAnonymousUtils.loginInBackground(new LogInCallback<MLUser>() {

                    @Override
                    public void done(MLUser user, MLException exception) {
                        if (exception == null) {
                            Log.d(TAG, "finish signing up anonymous user");
                        } else {
                            MLLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.log_out_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLUser user = MLUser.getCurrentUser();
                if (user != null) {
                    MLUser.logOut();
                    if (MLUser.getCurrentUser() == null) {
                        MLLog.i(TAG, "finish logging out");
                    } else {
                        MLLog.e(TAG, "fail to logout");
                    }
                } else {
                    MLLog.i(TAG,
                            "current user not exist, please signup a new user first");
                }
            }
        });

        findViewById(R.id.become_user_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLUser user = MLUser.getCurrentUser();
                if (user == null) {
                    MLLog.e(TAG, "Please signup a user first");
                } else if (!user.isAuthenticated()) {
                    MLLog.e(TAG, "Please login first");
                } else {
                    MLUserManager.becomeInBackground(user.getSessionToken(),
                            new LogInCallback<MLUser>() {

                                @Override
                                public void done(MLUser becomeUser,
                                                 MLException exception) {
                                    if (exception == null) {
                                        MLLog.i(TAG, "finish becoming");
                                    } else {
                                        MLLog.e(TAG, exception.getMessage());
                                        exception.printStackTrace();
                                    }
                                }

                            });
                }
            }
        });

        findViewById(R.id.request_email_verify_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLUserManager.requestEmailVerifyInBackground(
                        "android@example.com",
                        new RequestEmailVerifyCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG,
                                            "finish requesting email verify");
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }

                        });
            }
        });

        findViewById(R.id.request_password_reset_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLUserManager.requestPasswordResetInBackground(
                        "android@example.com",
                        new RequestPasswordResetCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG,
                                            "finsh requesting password");
                                } else {
                                    MLLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }

                        });
            }
        });

        findViewById(R.id.anony2normal_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLAnonymousUtils.loginInBackground(new LogInCallback<MLUser>() {

                    @Override
                    public void done(MLUser user, MLException exception) {
                        if (exception == null) {
                            user.setUserName("Foo");
                            user.setPassword("bar");
                            MLUserManager.saveInBackground(user,
                                    new SaveCallback() {

                                        @Override
                                        public void done(MLException exception) {
                                            if (exception == null) {
                                                MLLog.i(TAG,
                                                        "finish saving");
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
    }
}
