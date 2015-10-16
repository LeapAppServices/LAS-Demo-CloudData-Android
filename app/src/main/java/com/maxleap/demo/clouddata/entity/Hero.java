package com.maxleap.demo.clouddata.entity;

import com.maxleap.MLClassName;
import com.maxleap.MLObject;

@MLClassName(value = "tbHero")
public class Hero extends MLObject {

    public void setPower(int power) {
        put("power", power);
    }

    public int getPower() {
        return getInt("power");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

}
