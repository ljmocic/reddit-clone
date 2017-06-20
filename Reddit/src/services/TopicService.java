package services;

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

import beans.Subforum;
import beans.Topic;
import beans.User;
import dao.ApplicationDAO;

@Path("/topic")
public class TopicService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;

	ApplicationDAO dao = ApplicationDAO.getInstance();
	
	@POST
	@Path("/create")
	@Produces(MediaType.TEXT_PLAIN)
	public String create(	@FormParam("name") String name,
							@FormParam("content") String content) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Topic topic = new Topic(name, content, user, dao.searchSubforums("test1"));
			
			String subforumId = "test1";
			dao.addTopic(subforumId, topic);
			
			return "Added a topic" + topic.toString();
		}
		else {
			return "Must be logged in to add topic!";
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
				
				return "Updated topic " + subforum.toString();
			}
			else {
				return "Failed to update a topic";
			}
		}
		else {
			return "Must be logged in to update topic!";
		}
		
	}
	
	
	@POST
	@Path("/delete")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@FormParam("name") String name){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			if(dao.searchSubforums(name) != null) {
				dao.deleteSubforum(name);
				return "topic deleted " + name;
			}
			else {
				return "Error deleting " + name;
			}
		}
		else {
			return "Must be logged in to delete topic!";
		}
		
	}
	
	@GET
	@Path("/like/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String like(	@PathParam("subforumId") String subforumId,
						@PathParam("topicId") String topicId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			String subforumId1 = "test1";
			
			Topic topic = dao.searchTopics(subforumId1, topicId);
			
			if(topic != null) {
				if(!user.getLikedTopics().contains(topic)) {
					topic.like();
					user.like(topic);
					return "Successfully liked!";
				}
				else {
					return "Already liked!";
				}
			}
			else {
				return "Failed to like";
			}
		}
		else {
			return "Must be logged to like topic!";
		}
		
	}
	
	@GET
	@Path("/dislike/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String dislike(	@PathParam("subforumId") String subforumId,
						@PathParam("topicId") String topicId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			String subforumId1 = "test1";
			
			Topic topic = dao.searchTopics(subforumId1, topicId);
			
			if(topic != null) {
				if(!user.getDislikedTopics().contains(topic)) {
					topic.dislike();
					user.dislike(topic);
					return "Successfully disliked!";
				}
				else {
					return "Already disliked!";
				}
			}
			else {
				return "Failed to dislike";
			}
		}
		else {
			return "Must be logged to dislike topic!";
		}
	}
	
}
