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
		
		// add administrator
		User admin = new User("admin", "admin", "admin@gmail.com", "ljubisa", "mocic", "phoneNumber");
		admin.setRole(Config.ADMIN);
		
		// add moderators
		User mod1 = new User("mod1", "mod1", "email", "name", "surname", "phoneNumber");
		User mod2 = new User("mod2", "mod2", "email", "name", "surname", "phoneNumber");
		User mod3 = new User("mod3", "mod3", "email", "name", "surname", "phoneNumber");
		mod1.setRole(Config.MODERATOR);
		mod2.setRole(Config.MODERATOR);
		mod3.setRole(Config.MODERATOR);
		users.add(admin);
		users.add(mod1);
		users.add(mod2);
		users.add(mod3);
		
		// init subforums
		subforums.add(new Subforum("android", "test subforum description", "rules", "iconPath", "mod1"));
		subforums.add(new Subforum("ios", "test subforum description", "rules", "iconPath", "mod2"));
		subforums.add(new Subforum("windows", "test subforum description", "rules", "iconPath", "mod3"));
		
		// add classic user
		User user = new User("user", "user", "user@gmail.com", "user", "user", "phoneNumber");
		users.add(user);
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
