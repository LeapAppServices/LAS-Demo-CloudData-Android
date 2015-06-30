package as.leap.demo.clouddata.entity;

import as.leap.LASClassName;
import as.leap.LASObject;
import as.leap.LASRelation;

@LASClassName(value = "tbTeacher")
public class Teacher extends LASObject {

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

	public LASRelation<Teacher> getFriends() {
		return getRelation("friends");
	}

	public void setBestFriend(Teacher teacher) {
		put("bestFriend", teacher);
	}

	public Teacher getBestFriend() {
		return (Teacher) getLASObject("bestFriend");
	}
}
