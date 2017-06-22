package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Config;

@SuppressWarnings("serial")
public class User implements Serializable {

	private String username;
	private String password;
	private String email;
	private String name;
	private String surname;
	private String phoneNumber;
	private String registrationDate;
	
	private String role;

	private List<String> followedSubforums;
	private List<Topic> savedTopics;
	private List<Comment> savedComments;
	private List<Topic> likedTopics;
	private List<Topic> dislikedTopics;
	private List<Comment> comments;
	private List<Message> messages;

	public User(String username, String password, String email, String name, String surname, String phoneNumber) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.registrationDate = (new Date()).toString();
		this.role = Config.USER;
		this.followedSubforums = new ArrayList<String>();
		this.savedTopics = new ArrayList<Topic>();
		this.savedComments = new ArrayList<Comment>();
		this.likedTopics = new ArrayList<Topic>();
		this.dislikedTopics = new ArrayList<Topic>();
		this.comments = new ArrayList<Comment>();
		this.messages = new ArrayList<Message>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getFollowedSubforums() {
		return followedSubforums;
	}

	public void setFollowedSubforums(List<String> followedSubforums) {
		this.followedSubforums = followedSubforums;
	}

	public List<Topic> getSavedTopics() {
		return savedTopics;
	}

	public void setSavedTopics(List<Topic> savedTopics) {
		this.savedTopics = savedTopics;
	}

	public List<Comment> getSavedComments() {
		return savedComments;
	}

	public void setSavedComments(List<Comment> savedComments) {
		this.savedComments = savedComments;
	}

	public List<Topic> getLikedTopics() {
		return likedTopics;
	}

	public void setLikedTopics(List<Topic> likedTopics) {
		this.likedTopics = likedTopics;
	}

	public List<Topic> getDislikedTopics() {
		return dislikedTopics;
	}

	public void setDislikedTopics(List<Topic> dislikedTopics) {
		this.dislikedTopics = dislikedTopics;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", name=" + name
				+ ", surname=" + surname + ", phoneNumber=" + phoneNumber + ", registrationDate=" + registrationDate
				+ ", followedSubforums=" + followedSubforums + ", followedTopics=" + savedTopics + ", comments="
				+ comments + "]";
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void like(Topic topic) {
		likedTopics.add(topic);
	}
	
	public void dislike(Topic topic) {
		dislikedTopics.add(topic);
	}

	public void saveTopic(Topic topic) {
		savedTopics.add(topic);
	}
	
	public void saveComment(Comment comment) {
		savedComments.add(comment);
	}

	public void followForum(Subforum subforum) {
		followedSubforums.add(subforum.getName());
	}

	public boolean followsSubforum(String subforumId) {
		for(String followedSubforum: followedSubforums) {
			if(followedSubforum.equals(subforumId)) {
				return true;
			}
		}
		return false;
	}
	
}