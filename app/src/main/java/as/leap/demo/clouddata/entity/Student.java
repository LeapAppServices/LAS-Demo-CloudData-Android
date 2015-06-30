package as.leap.demo.clouddata.entity;

import java.util.Collection;
import java.util.Date;

import as.leap.LASClassName;
import as.leap.LASObject;

@LASClassName(value = "tbStudent")
public class Student extends LASObject {

    public void setName(String name) {
        put("name", name);
    }

    public void setAge(int age) {
        put("age", age);
    }

    public void addHobby(String hobby) {
        addUnique("hobbies", hobby);
    }

    public void addAllHobby(Collection<String> hobbies) {
        addAllUnique("hobbies", hobbies);
    }

    public void setIsMale(boolean isMale) {
        put("isMale", isMale);
    }

    public void setBirthday(Date date) {
        put("birthday", date);
    }

    public String getName() {
        return getString("name");
    }

    public void setNickName(String nickName) {
        put("nickName", nickName);
    }

    public void setScore(int score) {
        put("score", score);
    }

    public int getScore() {
        return getInt("score");
    }

}
