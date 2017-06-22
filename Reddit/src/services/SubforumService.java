package services;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
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

@Path("/subforum")
public class SubforumService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;

	ApplicationDAO dao = ApplicationDAO.getInstance();
	
	@POST
	@Path("/create")
	@Produces(MediaType.TEXT_PLAIN)
	public String create(	@FormParam("name") String name,
							@FormParam("description") String description,
							@FormParam("rules") String rules) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getRole().equals(Config.MODERATOR) || user.getRole().equals(Config.ADMIN)) {
				if(dao.searchSubforums(name) == null) {
					Subforum subforum = new Subforum(name, description, rules, null, user);
					dao.addSubforum(subforum);
					return "Added a forum" + subforum.toString();
				}
				else {
					return "Subforum already exists!";
				}
			}
			else {
				return "You do not have permission to create subforum!";
			}
			
		}
		else {
			return "Must be logged in to add subforum!";
		}
	}
	
	@GET
	@Path("/{subforumId}/topics")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Topic> subforumsTopics(@PathParam("subforumId") String subforumId) {
		
		Subforum subforum = dao.searchSubforums(subforumId);
		
		if(subforum != null) {
			return subforum.getTopics();
		}
		else {
			return null;
		}
	}
	
	@GET
	@Path("/{subforumId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Subforum subforum(@PathParam("subforumId") String subforumId) {
		
		Subforum subforum = dao.searchSubforums(subforumId);
		
		if(subforum != null) {
			return subforum;
		}
		else {
			return null;
		}
	}
	
	@GET
	@Path("/load/{subforumId}/{topicId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Topic loadTopic(	@PathParam("subforumId") String subforumId,
							@PathParam("topicId") String topicId) {
		
		Topic topic = dao.searchTopics(subforumId, topicId);
		
		if(topic != null) {
			return topic;
		}
		else {
			return null;
		}
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.TEXT_PLAIN)
	public String update(	@FormParam("name") String name, 
							@FormParam("description") String description, 
							@FormParam("rules") String rules){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Subforum subforum = dao.searchSubforums(name);
			
			if(subforum != null) {
				subforum.setName(name);
				subforum.setDescription(description);
				subforum.setRules(rules);
				dao.saveDatabase();
				
				return "Updated forum " + subforum.toString();
			}
			else {
				return "Failed to update a forum";
			}
		}
		else {
			return "Must be logged in to update subforum!";
		}
		
	}
	
	@GET
	@Path("/delete/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@PathParam("subforumId") String subforumId){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(dao.searchSubforums(subforumId) != null) {
				dao.deleteSubforum(subforumId);
				return "Forum deleted " + subforumId;
			}
			else {
				return "Error deleting " + subforumId;
			}
		}
		else {
			return "Must be logged in to add subforum!";
		}
	}
	
	@GET
	@Path("/follow/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String follow(@PathParam("subforumId") String subforumId){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Subforum subforum = dao.searchSubforums(subforumId);
		
		if(user != null) {
			if(subforum != null && !user.followsSubforum(subforumId)) {
				user.followForum(subforum);
				dao.saveDatabase();
				return "Followed subforum " + subforum.getName();
			}
			else {
				return "Error while trying to follow a subforum";
			}
		}
		else {
			return "Must be logged in to follow a subforum!";
		}
	}
	
	@POST
	@Path("/report/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String report(	@PathParam("subforumId") String subforumId, 
							@FormParam("complaintText") String complaintText) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Subforum subforum = dao.searchSubforums(subforumId);
		
		if(user != null) {
			if(subforum != null) {
				// TODO real implementation
				User moderator = subforum.getResponsibleModerator();
				moderator.addMessage(new Message(	user.getName(), 
													subforum.getResponsibleModerator().getName(), 
													complaintText));
				
				return "Reported subforum " + subforum.getName();
			}
			else {
				return "Error while trying to report subforum";
			}
		}
		else {
			return "Must be logged in to report subforum!";
		}
	}
	
}
