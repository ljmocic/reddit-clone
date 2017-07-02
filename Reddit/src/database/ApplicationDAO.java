package database;

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
import beans.Message;
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
	
	public synchronized void saveDatabase() {
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
	
	public synchronized void loadDatabase() {
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
	
	public synchronized List<Subforum> getSubforums() {
		return application.getSubforums();
	}

	public synchronized void setSubforums(List<Subforum> subforums) {
		this.application.setSubforums(subforums);
	}
	
	public synchronized List<User> getUsers() {
		return this.application.getUsers();
	}

	public synchronized void setUsers(List<User> users) {
		this.application.setUsers(users);
	}

	public synchronized void addUser(User user) {
		application.getUsers().add(user);
		saveDatabase();
	}
	
	public synchronized User searchUser(String username) {
		for(User user: application.getUsers()) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public synchronized Subforum searchSubforums(String id) {
		
		for(Subforum subforum: application.getSubforums()) {
			if(subforum.getName().equals(id)) {
				return subforum;
			}
		}
		return null;
	}

	public synchronized Topic searchTopics(String subforumId, String topicId) {
		
		Subforum subforum = searchSubforums(subforumId);
		List<Topic> topics = subforum.getTopics();
		
		for(Topic topic: topics) {
			if(topic.getName().equals(topicId)) {
				return topic;
			}
		}
		return null;
	}
	
	public synchronized Comment searchComments(String subforumId, String topicId, String commentId) {
		Topic topic = searchTopics(subforumId, topicId);
		
		for(Comment comment: topic.getComments()) {
			if(comment.getText().equals(commentId)) {
				return comment;
			}
		}
		return null;
	}

	public synchronized void addSubforum(Subforum subforum) {
		application.getSubforums().add(subforum);
		saveDatabase();
	}

	public synchronized void deleteSubforum(String name) {
		for(Subforum subforum: application.getSubforums()) {
			if(subforum.getName().equals(name)) {
				application.getSubforums().remove(subforum);
				saveDatabase();
				return;
			}
		}
	}

	public synchronized void addTopic(String subforumId, Topic topic) {
		Subforum subforum = searchSubforums(subforumId);
		
		if(subforum != null) {
			subforum.addTopic(topic);
			saveDatabase();
		}
	}

	public synchronized void changeUserRole(String username, String role) {
		for(User user: application.getUsers()) {
			if(user.getUsername().equals(username)) {
				user.setRole(role);
				saveDatabase();
				return;
			}
		}
	}

	public synchronized void deleteTopic(String subforumId, Topic topic) {
		Subforum subforum = searchSubforums(subforumId);
		subforum.getTopics().remove(topic);
		saveDatabase();
	}

	public synchronized void sendMessageToAdministrators(Message message) {
		for(User user: application.getUsers()) {
			Message copyMessage = new Message(message);
			if(user.getRole().equals(Config.ADMIN)) {
				copyMessage.setReceiverId(user.getUsername());
				user.addMessage(copyMessage);
				return;
			}
		}
	}
	
}