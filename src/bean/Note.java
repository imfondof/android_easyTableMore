package bean;

import java.io.Serializable;

public class Note implements Serializable {

	// id自增 文章标题 课程 正文 时间
	private int id;
	private String account;
	private String title;
	private String course;
	private String content;
	private String time;

	public Note(int id, String account, String title, String course,
			String content, String time) {
		super();
		this.id = id;
		this.account = account;
		this.title = title;
		this.course = course;
		this.content = content;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
