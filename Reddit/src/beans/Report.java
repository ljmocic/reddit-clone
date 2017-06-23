package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Report implements Serializable {

	private String text;
	private String date;
	private Topic enitityOfComplaint;
	private String userId;

	public Report(String text, Topic enitityOfComplaint, String userId) {
		super();
		this.text = text;
		this.date = null;
		this.enitityOfComplaint = enitityOfComplaint;
		this.userId = userId;
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

	public Topic getEnitityOfComplaint() {
		return enitityOfComplaint;
	}

	public void setEnitityOfComplaint(Topic enitityOfComplaint) {
		this.enitityOfComplaint = enitityOfComplaint;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}