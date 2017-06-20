package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Report implements Serializable {

	private String text;
	private String date;
	private int enitityOfComplaint;
	private User user;

	public Report(String text, String date, int enitityOfComplaint, User user) {
		super();
		this.text = text;
		this.date = date;
		this.enitityOfComplaint = enitityOfComplaint;
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getEnitityOfComplaint() {
		return enitityOfComplaint;
	}

	public void setEnitityOfComplaint(int enitityOfComplaint) {
		this.enitityOfComplaint = enitityOfComplaint;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}