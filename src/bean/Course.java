package bean;

import java.io.Serializable;

public class Course implements Serializable {

	// 学号、学期 ———这三个属性重要（为什么没有密码呢，因为很简单，学号就是密码，哈哈哈）
	// 课程id、课程名、老师、地点、周数----课程id对应着上课的周与节
	private String account;
	private int courseId;
	private String name;
	private String teacher;
	private String place;
	private int term;
	private String week;



	public Course(String account, int courseId, String name, String teacher,
			String place, int term, String week) {
		super();
		this.account = account;
		this.courseId = courseId;
		this.name = name;
		this.teacher = teacher;
		this.place = place;
		this.term = term;
		this.week = week;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", name=" + name + ", teacher="
				+ teacher + ", place=" + place + ", term=" + term + ", week="
				+ week + "]";
	}

}
