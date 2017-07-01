package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Report implements Serializable {

	private String text;
	private String date;
	private String subforumId;
	private String topicId;
	private String userId;
	
	public Report() {
		
	}
	
	public Report(String text, String subforumId, String topicId, String userId) {
		super();
		this.text = text;
		this.date = null;
		this.subforumId = subforumId;
		this.topicId = topicId;
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

	public String getSubforumId() {
		return subforumId;
	}

	public void setSubforum(String subforumId) {
		this.subforumId = subforumId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

}