package beans;

public class TopicSearchRequest {
	
	private String subforumId;
	private String topicId;
	private String content;
	private String author;
	
	public TopicSearchRequest() {
		
	}
	
	public TopicSearchRequest(String subforumId, String topicId, String content, String author) {
		super();
		this.subforumId = subforumId;
		this.topicId = topicId;
		this.content = content;
		this.author = author;
	}

	public String getSubforumId() {
		return subforumId;
	}

	public void setSubforumId(String subforumId) {
		this.subforumId = subforumId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
