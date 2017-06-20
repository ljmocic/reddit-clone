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
		User temp = new User("temp", "temp", "email", "name", "surname", "phoneNumber");
		admin.setRole(Config.Role.ADMIN);
		users.add(admin);
		users.add(temp);
		
		subforums.add(new Subforum("test1", "test", "test", "test", null));
		subforums.add(new Subforum("test2", "test", "test", "test", null));
		subforums.add(new Subforum("test3", "test", "test", "test", null));
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
