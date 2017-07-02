package services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Message;
import beans.Subforum;
import beans.Topic;
import beans.User;
import dao.ApplicationDAO;
import utils.Config;

@Path("/index")
public class IndexService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;

	ApplicationDAO dao = ApplicationDAO.getInstance();
	
	@GET
	@Path("/subforums")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subforum> subforums() {
		return dao.getSubforums();
	}
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> users() {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getRole() != null) {
				if(user.getRole().equals(Config.MODERATOR) || user.getRole().equals(Config.ADMIN)) {
					return dao.getUsers();
				}
			}
		}
		
		return null;
	}
	
	@POST
	@Path("/message")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendMessage(Message messageToAdd) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			User receiver = dao.searchUser(messageToAdd.getReceiverId());
			
			if(receiver != null) {
				Message message = new Message(user.getUsername(), receiver.getUsername(), messageToAdd.getContent());
				receiver.addMessage(message);
				dao.saveDatabase();
				
				return "Message sent!";
			}

			return "Receiver doesn't exist!";
			
		}
		else {
			return "Must be logged in to send message!";
		}
	}
	
	@GET
	@Path("/messages")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages() {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			return user.getMessages();
		}
		else {
			return null;
		}
	}
	
	@GET
	@Path("/searchSubforums/{searchQuery}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subforum> searchSubforums(@PathParam("searchQuery") String searchQuery) {
		
		List<Subforum> result = new ArrayList<Subforum>();
		
		for(Subforum subforum: dao.getSubforums()) {
			if( subforum.getName().contains(searchQuery) || 
				subforum.getDescription().contains(searchQuery) ||
				subforum.getResponsibleModerator().contains(searchQuery)) {
				
				result.add(subforum);
				
			}
		}
		
		return result;
	}
	
	@GET
	@Path("/searchTopics/{searchQuery}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Topic> searchTopics(@PathParam("searchQuery") String searchQuery) {
		
		List<Topic> result = new ArrayList<Topic>();
		
		for(Subforum subforum: dao.getSubforums()) {
			for(Topic topic: subforum.getTopics()) {
				if( topic.getName().contains(searchQuery) || 
					topic.getContent().contains(searchQuery) ||
					topic.getAuthor().contains(searchQuery) ||
					topic.getParentSubforumName().contains(searchQuery)) {

					result.add(topic);
						
				}
			}
			
		}
		
		return result;
		
	}
	
	@GET
	@Path("/searchUsers/{searchQuery}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> searchUsers(@PathParam("searchQuery") String searchQuery) {
		List<User> result = new ArrayList<User>();
		
		for(User user: dao.getUsers()) {
			if( user.getUsername().contains(searchQuery)) {
				result.add(user);
			}
		}
		
		return result;
	}
	
	@GET
	@Path("/recommendations")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Topic> recommendations() {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List<Topic> topics = new ArrayList<Topic>();
		
		for(Subforum subforum: dao.getSubforums()) {
			for(Topic topic: subforum.getTopics()) {
				topics.add(topic);
			}
		}
		
		List<Topic> best = new ArrayList<Topic>();
		
		
		
		while(best.size() < 5 || topics.size() > 0) {
			int max = -1;
			int maxTopicInd = 0;
			
			// find best, add to list, remove it
			for(int j = 0; j < topics.size(); j++) {
				if(topics.get(j).getClicks() > max) {
					max = topics.get(j).getClicks();
					maxTopicInd = j;
				}
			}
			
			// TODO check this code for edge cases
			
			if(user != null) {
				for(int j = 0; j < user.getClickedTopics().size(); j++) {
					
					// if user already seen that article, remove it
					if(topics.size() >= maxTopicInd) {
						if(user.getClickedTopics().get(j).equals(topics.get(maxTopicInd).getName())) {
							topics.remove(maxTopicInd);
							break;
						}
					}
				}
			}
			
			best.add(topics.get(maxTopicInd));
			topics.remove(maxTopicInd);
		}
		
		return best;
	}
	
}
