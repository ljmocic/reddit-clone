package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Report implements Serializable {

	private String text;
	private String date;
	private Subforum subforum;
	private Topic topic;
	private String userId;
	
	public Report(String text, Subforum subforum, String userId) {
		super();
		this.text = text;
		this.date = null;
		this.subforum = subforum;
		this.topic = null;
		this.userId = userId;
	}
	
	public Report(String text, Topic topic, String userId) {
		super();
		this.text = text;
		this.date = null;
		this.subforum = null;
		this.topic = topic;
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

	public Subforum getSubforum() {
		return subforum;
	}

	public void setSubforum(Subforum subforum) {
		this.subforum = subforum;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

}