package as.leap.demo.clouddata.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import as.leap.LASAnonymousUtils;
import as.leap.LASLog;
import as.leap.LASUser;
import as.leap.LASUserManager;
import as.leap.callback.LogInCallback;
import as.leap.callback.RequestEmailVerifyCallback;
import as.leap.callback.RequestPasswordResetCallback;
import as.leap.callback.SaveCallback;
import as.leap.callback.SignUpCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

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
                final LASUser user = new LASUser();
                user.setUserName("AndroidUser");
                user.setPassword("password");
                user.setEmail("android@example.com");
                LASUserManager.signUpInBackground(user, new SignUpCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            LASLog.i(TAG, "finish signing up");
                        } else {
                            LASLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASUserManager.logInInBackground("AndroidUser",
                        "password", new LogInCallback<LASUser>() {

                            @Override
                            public void done(LASUser user,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish logging in");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.anonymous_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASAnonymousUtils.loginInBackground(new LogInCallback<LASUser>() {

                    @Override
                    public void done(LASUser user, LASException exception) {
                        if (exception == null) {
                            Log.d(TAG, "finish signing up anonymous user");
                        } else {
                            LASLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.log_out_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASUser user = LASUser.getCurrentUser();
                if (user != null) {
                    LASUser.logOut();
                    if (LASUser.getCurrentUser() == null) {
                        LASLog.i(TAG, "finish logging out");
                    } else {
                        LASLog.e(TAG, "fail to logout");
                    }
                } else {
                    LASLog.i(TAG,
                            "current user not exist, please signup a new user first");
                }
            }
        });

        findViewById(R.id.become_user_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASUser user = LASUser.getCurrentUser();
                if (user == null) {
                    LASLog.e(TAG, "Please signup a user first");
                } else if (!user.isAuthenticated()) {
                    LASLog.e(TAG, "Please login first");
                } else {
                    LASUserManager.becomeInBackground(user.getSessionToken(),
                            new LogInCallback<LASUser>() {

                                @Override
                                public void done(LASUser becomeUser,
                                                 LASException exception) {
                                    if (exception == null) {
                                        LASLog.i(TAG, "finish becoming");
                                    } else {
                                        LASLog.e(TAG, exception.getMessage());
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
                LASUserManager.requestEmailVerifyInBackground(
                        "android@example.com",
                        new RequestEmailVerifyCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG,
                                            "finish requesting email verify");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }

                        });
            }
        });

        findViewById(R.id.request_password_reset_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASUserManager.requestPasswordResetInBackground(
                        "android@example.com",
                        new RequestPasswordResetCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG,
                                            "finsh requesting password");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }

                        });
            }
        });

        findViewById(R.id.anony2normal_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASAnonymousUtils.loginInBackground(new LogInCallback<LASUser>() {

                    @Override
                    public void done(LASUser user, LASException exception) {
                        if (exception == null) {
                            user.setUserName("Foo");
                            user.setPassword("bar");
                            LASUserManager.saveInBackground(user,
                                    new SaveCallback() {

                                        @Override
                                        public void done(LASException exception) {
                                            if (exception == null) {
                                                LASLog.i(TAG,
                                                        "finish saving");
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
    }
}
