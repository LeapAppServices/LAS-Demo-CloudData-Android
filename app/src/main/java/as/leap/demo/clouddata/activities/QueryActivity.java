package as.leap.demo.clouddata.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import as.leap.LASDataManager;
import as.leap.LASLog;
import as.leap.LASQuery;
import as.leap.LASQueryManager;
import as.leap.LASRelation;
import as.leap.callback.CountCallback;
import as.leap.callback.FindCallback;
import as.leap.callback.GetCallback;
import as.leap.callback.SaveCallback;
import as.leap.demo.clouddata.R;
import as.leap.demo.clouddata.entity.Student;
import as.leap.demo.clouddata.entity.Teacher;
import as.leap.demo.clouddata.log.LogActivity;
import as.leap.exception.LASException;

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

                LASDataManager.saveAllInBackground(students,
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG,
                                            "finish creating query objects");
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });

            }
        });

        findViewById(R.id.equals_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                query.whereEqualTo("isMale", true);
                query.whereGreaterThan("score", 70);
                query.whereLessThan("age", 15);
                query.orderByDescending("score");
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.scope_button).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                query.whereContainedIn("score", Arrays.asList(50, 80, 90, 92));
                query.whereDoesNotExist("isMale");
                query.orderByAscending("score");
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.or_button).setOnClickListener(new OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {

                LASQuery<Student> sub01 = new LASQuery<Student>(Student.class);
                sub01.whereGreaterThanOrEqualTo("age", 15);

                LASQuery<Student> sub02 = new LASQuery<Student>(Student.class);
                sub02.whereLessThanOrEqualTo("age", 10);

                LASQuery<Student> query = LASQuery.or(Arrays.asList(sub01,
                        sub02));
                query.addDescendingOrder("score");
                query.addAscendingOrder("age");
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.counter_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                LASQueryManager.countInBackground(query, new CountCallback() {

                    @Override
                    public void done(int count, LASException exception) {
                        if (exception == null) {
                            LASLog.i(TAG, "finish counting");
                            LASLog.i(TAG, "count is " + count);
                        } else {
                            exception.printStackTrace();
                            LASLog.e(TAG, exception.getMessage());
                        }
                    }
                });
            }
        });

        findViewById(R.id.limit_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                query.setLimit(5);
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.regular_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                // query.whereStartsWith("name", "T");
                // query.whereEndsWith("name", "y");
                // query.whereMatches("name", "/\\wA\\w{2}/", "i");
                query.whereContains("name", "o");
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.query_all_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                query.whereContainsAll("hobbies",
                        Arrays.asList("swimming", "running"));
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.sub_query_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Student> subQuery = LASQuery.getQuery(Student.class);
                subQuery.whereEqualTo("isMale", false);

                LASQuery<Student> query = LASQuery.getQuery(Student.class);
                query.whereMatchesKeyInQuery("name", "nickName", subQuery);
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Student>() {

                            @Override
                            public void done(List<Student> students,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Student stu : students) {
                                        LASLog.i(TAG, "student-->" + stu.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
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

                LASDataManager.saveAllInBackground(
                        Arrays.asList(louis, kent, peris, henry),
                        new SaveCallback() {

                            @Override
                            public void done(LASException exception) {
                                if (exception == null) {
                                    louis.addFriend(peris);
                                    louis.addFriend(henry);
                                    LASDataManager.saveInBackground(louis,
                                            new SaveCallback() {

                                                @Override
                                                public void done(
                                                        LASException exception) {
                                                    if (exception == null) {
                                                        LASLog.i(TAG,
                                                                "finish updating");
                                                    } else {
                                                        exception.printStackTrace();
                                                        LASLog.e(TAG, exception.getMessage());
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
                LASQuery<Teacher> query = LASQuery.getQuery(Teacher.class);
                query.whereEqualTo("name", "Louis");
                query.include("bestFriend");
                LASQueryManager.getFirstInBackground(query,
                        new GetCallback<Teacher>() {

                            @Override
                            public void done(Teacher louis,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    LASLog.i(TAG, louis.getName());
                                    LASLog.i(TAG, louis.getBestFriend().getName());
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.one2many_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LASQuery<Teacher> query = LASQuery.getQuery(Teacher.class);
                query.whereEqualTo("name", "Louis");
                query.include("bestFriend");
                LASQueryManager.getFirstInBackground(query,
                        new GetCallback<Teacher>() {

                            @Override
                            public void done(Teacher louis,
                                             LASException exception) {
                                if (exception == null) {
                                    LASRelation<Teacher> friendsRelation = louis
                                            .getFriends();
                                    LASQuery<Teacher> relationQuery = friendsRelation
                                            .getQuery();
                                    LASQueryManager.findAllInBackground(
                                            relationQuery,
                                            new FindCallback<Teacher>() {

                                                @Override
                                                public void done(
                                                        List<Teacher> friends,
                                                        LASException exception) {
                                                    if (exception == null) {
                                                        LASLog.i(TAG, "finish querying");
                                                        for (Teacher friend : friends) {
                                                            LASLog.i(TAG, "friend-->" + friend.getName());
                                                        }
                                                    } else {
                                                        exception.printStackTrace();
                                                        LASLog.e(TAG, exception.getMessage());
                                                    }
                                                }
                                            });
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

        findViewById(R.id.in_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                LASQuery<Teacher> inQuery = LASQuery.getQuery(Teacher.class);
                // inQuery.whereExists("name");
                LASQuery<Teacher> query = LASQuery.getQuery(Teacher.class);
                query.whereMatchesQuery("bestFriend", inQuery);
                LASQueryManager.findAllInBackground(query,
                        new FindCallback<Teacher>() {

                            @Override
                            public void done(List<Teacher> friends,
                                             LASException exception) {
                                if (exception == null) {
                                    LASLog.i(TAG, "finish querying");
                                    for (Teacher friend : friends) {
                                        LASLog.i(TAG, "friend-->" + friend.getName());
                                    }
                                } else {
                                    exception.printStackTrace();
                                    LASLog.e(TAG, exception.getMessage());
                                }
                            }
                        });
            }
        });

    }
}
