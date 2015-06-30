package as.leap.demo.clouddata.entity;

import as.leap.LASClassName;
import as.leap.LASObject;

@LASClassName(value = "tbHero")
public class Hero extends LASObject {

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
