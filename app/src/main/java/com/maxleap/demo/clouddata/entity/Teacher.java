package com.maxleap.demo.clouddata.entity;

import com.maxleap.MLClassName;
import com.maxleap.MLObject;
import com.maxleap.MLRelation;

@MLClassName(value = "tbTeacher")
public class Teacher extends MLObject {

    public Teacher() {
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void addFriend(Teacher teacher) {
        getFriends().add(teacher);
    }

    public MLRelation<Teacher> getFriends() {
        return getRelation("friends");
    }

    public void setBestFriend(Teacher teacher) {
        put("bestFriend", teacher);
    }

    public Teacher getBestFriend() {
        return (Teacher) getMLObject("bestFriend");
    }
}
