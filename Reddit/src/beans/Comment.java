package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Comment implements Serializable {

	private User author;
	private String date;
	private String text;
	private int likes;
	private int dislikes;
	private boolean modified;
	private boolean deleted;

	private Topic parentTopic;
	private Comment parentComment;
	private List<Comment> childComments;
	
	public Comment(User author, String text, Topic parentTopic, Comment parentComment) {
		this.author = author;
		this.date = "";
		this.text = text;
		this.likes = 0;
		this.dislikes = 0;
		this.modified = false;
		this.deleted = false;
		this.parentTopic = parentTopic;
		this.parentComment = parentComment;
		this.childComments = new ArrayList<Comment>();
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
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

	public Topic getParentTopic() {
		return parentTopic;
	}

	public void setParentTopic(Topic parentTopic) {
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
	
}