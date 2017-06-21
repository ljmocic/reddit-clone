package dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import beans.Application;
import beans.Comment;
import beans.Subforum;
import beans.Topic;
import beans.User;
import utils.Config;

public class ApplicationDAO {
	
	private static String DATABASE_FILENAME = "database.bin";
	
	private Application application;
	
	private static ApplicationDAO instance = null;
	
	private ApplicationDAO() {
		super();
		File databaseFile = new File(Config.DEFAULT_SAVE_LOCATION + DATABASE_FILENAME);
		if (!databaseFile.exists()) {
			application = new Application();
			saveDatabase();
		}
		else {
			loadDatabase();
		}
	}

	public static ApplicationDAO getInstance() {
		if(instance == null) {
			instance = new ApplicationDAO();
		}
		return instance;
	}
	
	public void saveDatabase() {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(
					new BufferedOutputStream(
						new FileOutputStream(Config.DEFAULT_SAVE_LOCATION + DATABASE_FILENAME)));
			
			out.writeObject(application);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public void loadDatabase() {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(
					new BufferedInputStream(
						new FileInputStream(Config.DEFAULT_SAVE_LOCATION + DATABASE_FILENAME)));
			
			application = ((Application) in.readObject());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public List<Subforum> getSubforums() {
		return application.getSubforums();
	}

	public void setSubforums(List<Subforum> subforums) {
		this.application.setSubforums(subforums);
	}
	
	public List<User> getUsers() {
		return this.application.getUsers();
	}

	public void setUsers(List<User> users) {
		this.application.setUsers(users);
	}

	public void addUser(User user) {
		application.getUsers().add(user);
		saveDatabase();
	}
	
	public User searchUser(String username) {
		for(User user: application.getUsers()) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public Subforum searchSubforums(String id) {
		
		for(Subforum subforum: application.getSubforums()) {
			if(subforum.getName().equals(id)) {
				return subforum;
			}
		}
		return null;
	}

	public Topic searchTopics(String subforumId, String topicId) {
		
		Subforum subforum = searchSubforums(subforumId);
		List<Topic> topics = subforum.getTopics();
		
		for(Topic topic: topics) {
			if(topic.getName().equals(topicId)) {
				return topic;
			}
		}
		return null;
	}
	
	public Comment searchComments(String subforumId, String topicId, String commentId) {
		Topic topic = searchTopics(subforumId, topicId);
		
		for(Comment comment: topic.getComments()) {
			if(comment.getText().equals(commentId)) {
				return comment;
			}
		}
		return null;
	}

	public void addSubforum(Subforum subforum) {
		application.getSubforums().add(subforum);
		saveDatabase();
	}

	public void deleteSubforum(String name) {
		for(Subforum subforum: application.getSubforums()) {
			if(subforum.getName().equals(name)) {
				application.getSubforums().remove(subforum);
				saveDatabase();
				return;
			}
		}
	}

	public void addTopic(String subforumId, Topic topic) {
		// TODO update with real id later
		//Subforum subforum = searchSubforums(subforumId);
		Subforum subforum = searchSubforums(subforumId);
		
		if(subforum != null) {
			subforum.addTopic(topic);
			saveDatabase();
		}
	}

	public void changeUserRole(String username, String role) {
		for(User user: application.getUsers()) {
			if(user.getUsername().equals(username)) {
				user.setRole(role);
				saveDatabase();
				return;
			}
		}
	}

	
	
}