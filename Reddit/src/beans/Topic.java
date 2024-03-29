package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class Topic implements Serializable {

	private String name;
	private String author;
	private String content;
	private String type;
	private String creationDate;
	private int likes;
	private int dislikes;
	
	private int clicks;

	private String parentSubforumName;
	
	private List<Comment> comments;
	
	public Topic() {
		
	}

	public Topic(String name, String content, String author, String parentSubforumName) {
		super();
		this.name = name;
		this.author = author;
		this.creationDate = (new Date()).toString();
		this.likes = 0;
		this.dislikes = 0;
		this.clicks = 0;
		this.parentSubforumName = parentSubforumName;
		this.comments = new ArrayList<Comment>();
		this.content = content;
		this.type = "text";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setParentSubforumName(String parentSubforumName) {
		this.parentSubforumName = parentSubforumName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	
	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public String getParentSubforumName() {
		return parentSubforumName;
	}

	public void setParentSubforum(String parentSubforumName) {
		this.parentSubforumName = parentSubforumName;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void like() {
		this.likes++;
	}

	public void dislike() {
		this.dislikes++;
	}

	@Override
	public String toString() {
		return "Topic [name=" + name + ", author=" + author;
	}

	public void removeLike() {
		this.likes--;
	}
	
	public void removeDislike() {
		this.dislikes--;
	}

	public void click() {
		clicks++;
	}
	
}