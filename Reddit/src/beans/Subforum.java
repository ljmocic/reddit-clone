package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Subforum implements Serializable {

	private String name;
	private String description;
	private String icon;
	private User responsibleModerator;
	private String rules;
	
	@JsonIgnore
	private List<Topic> topics;
	
	private List<User> moderators;

	public Subforum(String name, String description, String rules, String icon, User responsibleModerator) {
		super();
		this.name = name;
		this.description = description;
		this.rules = rules;
		this.icon = icon;
		this.responsibleModerator = responsibleModerator;
		this.topics = new ArrayList<Topic>();
		this.moderators = new ArrayList<User>();
		
		initData();
	}

	private void initData() {
		User user = new User("username", "password", "email", "name", "surname", "phoneNumber");
		
		topics.add(new Topic("topic1", "content1", user, this));
		topics.add(new Topic("topic2", "content2", user, this));
		topics.add(new Topic("topic3", "content3", user, this));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public User getResponsibleModerator() {
		return responsibleModerator;
	}

	public void setResponsibleModerator(User responsibleModerator) {
		this.responsibleModerator = responsibleModerator;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public List<User> getModerators() {
		return moderators;
	}

	public void setModerators(List<User> moderators) {
		this.moderators = moderators;
	}

	@Override
	public String toString() {
		return "Subforum [name=" + name + ", description=" + description + "]";
	}

	public void addTopic(Topic topic) {
		topics.add(topic);
	}

}