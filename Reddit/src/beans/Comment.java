package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("serial")
public class Comment implements Serializable {
	
	private String id;

	private String author;
	private String date;
	private String text;
	private int likes;
	private int dislikes;
	private boolean modified;
	private boolean deleted;

	private String parentSubforum;
	private String parentTopic;
	private Comment parentComment;
	private List<Comment> childComments;
	
	public Comment() {
		this.id = UUID.randomUUID().toString();
		this.likes = 0;
		this.dislikes = 0;
		this.modified = false;
		this.deleted = false;
		this.parentSubforum = null;
		this.parentTopic = null;
		this.parentComment = null;
		this.childComments = new ArrayList<Comment>();
		this.modified = false;
		this.deleted = false;
		this.childComments = new ArrayList<Comment>();
	}
	
	public Comment(String author, String text, String parentSubforum, String parentTopic, Comment parentComment) {
		this.id = UUID.randomUUID().toString();
		this.author = author;
		this.date = "";
		this.text = text;
		this.likes = 0;
		this.dislikes = 0;
		this.modified = false;
		this.deleted = false;
		this.parentSubforum = parentSubforum;
		this.parentTopic = parentTopic;
		this.parentComment = parentComment;
		this.childComments = new ArrayList<Comment>();
	}

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public String getParentSubforum() {
		return parentSubforum;
	}

	public void setParentSubforum(String parentSubforum) {
		this.parentSubforum = parentSubforum;
	}

	public String getParentTopic() {
		return parentTopic;
	}

	public void setParentTopic(String parentTopic) {
		this.parentTopic = parentTopic;
	}

	public Comment getParentComment() {
		return parentComment;
	}

	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}

	public List<Comment> getChildComments() {
		return childComments;
	}

	public void setChildComments(List<Comment> childComments) {
		this.childComments = childComments;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Comment [author=" + author + ", date=" + date + ", text=" + text + ", likes=" + likes + ", dislikes="
				+ dislikes + ", modified=" + modified + "]";
	}

	public void removeDislike() {
		likes++;
	}

	public void like() {
		likes++;
	}

	public void removeLike() {
		likes--;
	}

	public void dislike() {
		dislikes++;
	}
	
}