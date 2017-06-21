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

import beans.Comment;
import beans.Message;
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
	
	@GET
	@Path("/create/{subforumId}/{topicId}/{type}/{content}")
	@Produces(MediaType.TEXT_PLAIN)
	public String create(	@PathParam("subforumId") String subforumId,
							@PathParam("topicId") String topicId,
							@PathParam("type") String type,
							@PathParam("content") String content) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Topic topic = new Topic(topicId, content, user, subforumId);
			//topic.setType(type);
			
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
	
	@GET
	@Path("/saveTopic/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveTopic(	@PathParam("subforumId") String subforumId,
								@PathParam("topicId") String topicId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null) {
				if(!user.getSavedTopics().contains(topic)) {
					user.saveTopic(topic);
					return "Successfully saved!";
				}
				else {
					return "Already saved!";
				}
			}
			else {
				return "Failed to save";
			}
		}
		else {
			return "Must be logged to save topic!";
		}
	}
	
	@GET
	@Path("/saveComment/{subforumId}/{topicId}/{commendId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveComment(	@PathParam("subforumId") String subforumId,
						@PathParam("topicId") String topicId,
						@PathParam("commentId") String commentId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Comment comment = dao.searchComments(subforumId, topicId, commentId);
			
			if(comment != null) {
				if(!user.getSavedComments().contains(comment)) {
					user.saveComment(comment);
					return "Successfully saved!";
				}
				else {
					return "Already saved!";
				}
			}
			else {
				return "Failed to save";
			}
		}
		else {
			return "Must be logged to save topic!";
		}
	}
	
	@POST
	@Path("/report/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String report(	@PathParam("subforumId") String subforumId,
								@PathParam("topicId") String topicId,
								@FormParam("complaintText") String complaintText) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Subforum subforum = dao.searchSubforums(subforumId);
			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null && subforum != null) {
				// TODO real implementation
				User moderator = subforum.getResponsibleModerator();
				moderator.addMessage(new Message(	user.getName(), 
													subforum.getResponsibleModerator().getName(), 
													complaintText));
				return "Reported" + topic.getName();
			}
			else {
				return "Failed to report";
			}
		}
		else {
			return "Must be logged to report topic!";
		}
	}
	
}
