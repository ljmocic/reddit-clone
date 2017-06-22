package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utils.Config;

@SuppressWarnings("serial")
public class Application implements Serializable {
	
	private List<Subforum> subforums;
	private List<User> users;
	
	public Application() {
		super();
		this.subforums = new ArrayList<Subforum>();
		this.users = new ArrayList<User>();
		loadTestData();
	}
	
	private void loadTestData() {
		User admin = new User("admin", "admin", "email", "name", "surname", "phoneNumber");
		admin.setRole(Config.ADMIN);
		User temp = new User("temp", "temp", "email", "name", "surname", "phoneNumber");
		admin.setRole(Config.ADMIN);
		users.add(admin);
		users.add(temp);
		
		Subforum subforum1 = new Subforum("test1", "test", "test", "test", admin);
		subforums.add(subforum1);
		subforums.add(new Subforum("test2", "test", "test", "test", admin));
		subforums.add(new Subforum("test3", "test", "test", "test", admin));
		
		admin.followForum(subforum1);
	}

	public List<Subforum> getSubforums() {
		return subforums;
	}

	public void setSubforums(List<Subforum> subforums) {
		this.subforums = subforums;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
