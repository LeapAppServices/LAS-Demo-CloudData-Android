package com.maxleap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.maxleap.CountCallback;
import com.maxleap.FindCallback;
import com.maxleap.GetCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLLog;
import com.maxleap.MLQuery;
import com.maxleap.MLQueryManager;
import com.maxleap.MLRelation;
import com.maxleap.SaveCallback;
import com.maxleap.demo.clouddata.R;
import com.maxleap.demo.clouddata.entity.Student;
import com.maxleap.demo.clouddata.entity.Teacher;
import com.maxleap.demo.clouddata.log.LogActivity;
import com.maxleap.exception.MLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryActivity extends LogActivity {

    public static final String TAG = QueryActivity.class.getSimpleName();

    private static Student createStudent(String name, int age, int score,
                                         Boolean isMale) {
        return createStudent(name, name, age, score, isMale);
    }

    private static Student createStudent(String name, String nickName, int age,
                                         int score, Boolean isMale) {
        Student stu = new Student();
        stu.setName(name);
        stu.setNickName(nickName);
        stu.setAge(age);
        stu.setScore(score);
        if (isMale != null) {
            stu.setIsMale(isMale);
        }
        return stu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        initializeLogging();

        findViewById(R.id.create_query_objects_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Student peter = createStudent("Peter", 14, 50, true);
                Student jack = createStudent("Jack", 15, 80, true);
                Student andy = createStudent("Andy", 20, 92, true);
                Student tom = createStudent("Tom", 12, 90, true);
                Student tony = createStudent("Tony", 11, 78, true);
                Student tina = createStudent("Tina", 10, 50, false);
                Student jane = createStudent("Jane", 15, 92, false);
                Student mary = createStudent("Mary", "Little Mary", 9, 40,
                        false);
                Student robot = createStudent("Robot", 9, 92, null);
                jack.addHobby("swimming");
                andy.addHobby("swimming");
                andy.addHobby("running");
                tony.addAllHobby(Arrays
                        .asList("swimming", "running", "walking"));

                List<Student> students = new ArrayList<Student>();
                Collections.addAll(students, peter, jack, andy, tom, tony,
                        tina, jane, mary, robot);

                MLDataManager.saveAllInBackground(students,
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG,
                                            "finish creating query objects");
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });

            }
        });

        findViewById(R.id.equals_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                query.whereEqualTo("isMale", true);
                query.whereGreaterThan("score", 70);
                query.whereLessThan("age", 15);
                query.orderByDescending("score");
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.scope_button).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                query.whereContainedIn("score", Arrays.asList(50, 80, 90, 92));
                query.whereDoesNotExist("isMale");
                query.orderByAscending("score");
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.or_button).setOnClickListener(new OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {

                MLQuery<Student> sub01 = new MLQuery<Student>(Student.class);
                sub01.whereGreaterThanOrEqualTo("age", 15);

                MLQuery<Student> sub02 = new MLQuery<Student>(Student.class);
                sub02.whereLessThanOrEqualTo("age", 10);

                MLQuery<Student> query = MLQuery.or(Arrays.asList(sub01,
                        sub02));
                query.addDescendingOrder("score");
                query.addAscendingOrder("age");
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.counter_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                MLQueryManager.countInBackground(query, new CountCallback() {

                    @Override
                    public void done(int count, MLException exception) {
                        if (exception == null) {
                            MLLog.i(TAG, "finish counting");
                            MLLog.i(TAG, "count is " + count);
                        } else {
                            exception.printStackTrace();
                            MLLog.e(TAG, exception.getMessage());
                        }
                    }
                });
            }
        });

        findViewById(R.id.limit_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                query.setLimit(5);
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.regular_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                // query.whereStartsWith("name", "T");
                // query.whereEndsWith("name", "y");
                // query.whereMatches("name", "/\\wA\\w{2}/", "i");
                query.whereContains("name", "o");
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.query_all_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                query.whereContainsAll("hobbies",
                        Arrays.asList("swimming", "running"));
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.sub_query_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Student> subQuery = MLQuery.getQuery(Student.class);
                subQuery.whereEqualTo("isMale", false);

                MLQuery<Student> query = MLQuery.getQuery(Student.class);
                query.whereMatchesKeyInQuery("name", "nickName", subQuery);
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        MLLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.create_relation_testdata_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Teacher louis = new Teacher();
                louis.setName("Louis");

                Teacher kent = new Teacher();
                kent.setName("Kent");

                louis.setBestFriend(kent);

                final Teacher peris = new Teacher();
                peris.setName("Peris");

                final Teacher henry = new Teacher();
                henry.setName("Henry");

                MLDataManager.saveAllInBackground(
                        Arrays.asList(louis, kent, peris, henry),
                        new SaveCallback() {

                            @Override
                            public void done(MLException exception) {
                                if (exception == null) {
                                    louis.addFriend(peris);
                                    louis.addFriend(henry);
                                    MLDataManager.saveInBackground(louis,
                                            new SaveCallback() {

                                                @Override
                                                public void done(
                                                        MLException exception) {
                                                    if (exception == null) {
                                                        MLLog.i(TAG,
                                                                "finish updating");
                                                    } else {
                                                        exception.printStackTrace();
                                                        MLLog.e(TAG, exception.getMessage());
                                                    }
                                                }
                                            });
                                } else {
                                    exception.printStackTrace();
                                }
                            }
                        });

            }
        });

        findViewById(R.id.one2one_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Teacher> query = MLQuery.getQuery(Teacher.class);
                query.whereEqualTo("name", "Louis");
                query.include("bestFriend");
                MLQueryManager.getFirstInBackground(query,
                        new GetCallback<Teacher>() {

                            @Override
                            public void done(Teacher louis,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    MLLog.i(TAG, louis.getName());
                                    MLLog.i(TAG, louis.getBestFriend().getName());
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.one2many_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MLQuery<Teacher> query = MLQuery.getQuery(Teacher.class);
                query.whereEqualTo("name", "Louis");
                query.include("bestFriend");
                MLQueryManager.getFirstInBackground(query,
                        new GetCallback<Teacher>() {

                            @Override
                            public void done(Teacher louis,
                                             MLException exception) {
                                if (exception == null) {
                                    MLRelation<Teacher> friendsRelation = louis
                                            .getFriends();
                                    MLQuery<Teacher> relationQuery = friendsRelation
                                            .getQuery();
                                    MLQueryManager.findAllInBackground(
                                            relationQuery,
                                            new FindCallback<Teacher>() {

                                                @Override
                                                public void done(
                                                        List<Teacher> friends,
                                                        MLException exception) {
                                                    if (exception == null) {
                                                        MLLog.i(TAG, "finish querying");
                                                        for (Teacher friend : friends) {
                                                            MLLog.i(TAG, "friend-->" + friend.getName());
                                                        }
                                                    } else {
                                                        exception.printStackTrace();
                                                        MLLog.e(TAG, exception.getMessage());
                                                    }
                                                }
                                            });
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.in_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                MLQuery<Teacher> inQuery = MLQuery.getQuery(Teacher.class);
                // inQuery.whereExists("name");
                MLQuery<Teacher> query = MLQuery.getQuery(Teacher.class);
                query.whereMatchesQuery("bestFriend", inQuery);
                MLQueryManager.findAllInBackground(query,
                        new FindCallback<Teacher>() {

                            @Override
                            public void done(List<Teacher> friends,
                                             MLException exception) {
                                if (exception == null) {
                                    MLLog.i(TAG, "finish querying");
                                    for (Teacher friend : friends) {
                                        MLLog.i(TAG, "friend-->" + friend.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    MLLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

    }
}
