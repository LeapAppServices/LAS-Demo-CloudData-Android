package as.leap.demo.clouddata.activities;

import java.util.Arrays;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import as.leap.LASACL;
import as.leap.LASDataManager;
import as.leap.LASLog;
import as.leap.LASObject;
import as.leap.LASRole;
import as.leap.LASRoleManager;
import as.leap.LASUser;
import as.leap.LASUserManager;
import as.leap.callback.GetCallback;
import as.leap.callback.SaveCallback;

import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.entity.Hero;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

public class RoleActivity extends LogActivity {

    public static final String TAG = RoleActivity.class.getSimpleName();

    private LASRole mRole;
    private Hero mHeroReadOnly;
    private Hero mHeroAccessible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        initializeLogging();

        findViewById(R.id.create_role_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASACL acl = new LASACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(true);

                final LASRole admin = new LASRole("Administrator", acl);

                LASRoleManager.saveInBackground(admin, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            LASLog.i(TAG, "finish saving");
                            mRole = admin;
                        } else {
                            LASLog.e(TAG, exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        findViewById(R.id.get_role_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mRole == null) {
                    LASLog.t("Please create a role first.");
                    return;
                }
                LASRoleManager.fetchInBackground(mRole,
                        new GetCallback<LASRole>() {

                            @Override
                            public void done(LASRole role,
                                             LASException exception) {
                                if (exception != null) {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                } else {
                                    LASLog.i(TAG, "finish fetching");
                                }
                            }
                        });
            }
        });

        findViewById(R.id.create_acl_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASUser user = LASUser.getCurrentUser();
                if (user == null) {
                    LASLog.e(TAG, "Please signup or login an user first in UserActivity.");
                    return;
                }
                mHeroReadOnly = new Hero();
                mHeroReadOnly.setName("ReadHero");
                LASACL acl = new LASACL();
                acl.setPublicReadAccess(true);
                acl.setRoleWriteAccess("Administrator", true);
                acl.setWriteAccess(user, true);
                acl.setReadAccess(user, true);
                mHeroReadOnly.setACL(acl);

                mHeroAccessible = new Hero();
                mHeroAccessible.setName("WriteHero");
                LASACL writeACL = new LASACL();
                writeACL.setPublicReadAccess(true);
                writeACL.setPublicWriteAccess(true);
                mHeroAccessible.setACL(writeACL);

                LASDataManager.saveAllInBackground(
                        Arrays.asList(mHeroReadOnly, mHeroAccessible),
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish creating acl");
                                } else {
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });

            }
        });

        findViewById(R.id.test_acl_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mHeroReadOnly == null || mHeroAccessible == null) {
                    LASLog.e(TAG, "Please create acl first.");
                    return;
                }
                mHeroReadOnly.setPower(100);
                mHeroAccessible.setPower(100);
                LASDataManager.saveAllInBackground(
                        Arrays.asList(mHeroReadOnly, mHeroAccessible),
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish testing acl");
                                } else {
                                    LASLog.i(TAG, "power of HeroReadOnly is "
                                            + mHeroReadOnly.getPower());
                                    LASLog.i(
                                            TAG,
                                            "power of HeroAccessable is "
                                                    + mHeroAccessible
                                                    .getPower());
                                    LASLog.e(TAG, exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.default_acl_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASUser user = LASUser.getCurrentUser();
                if (user == null) {
                    LASLog.e(TAG, "Please signup or login an user first in UserActivity.");
                    return;
                }
                LASACL defaultACL = new LASACL(user);
                LASACL.setDefaultACL(defaultACL, true);
                LASUserManager.saveInBackground(user, new SaveCallback() {

                    @Override
                    public void done(LASException exception) {
                        if (exception == null) {
                            final LASObject apple = new LASObject("Fruits");
                            apple.put("name", "apple");
                            LASDataManager.saveInBackground(apple,
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
