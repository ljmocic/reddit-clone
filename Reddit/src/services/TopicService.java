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
import beans.Report;
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
	@Path("/create/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String create(	@PathParam("subforumId") String subforumId,
							@FormParam("topicId") String topicId,
							@FormParam("type") String type,
							@FormParam("content") String content) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Topic topic = new Topic(topicId, content, user.getUsername(), subforumId);
			topic.setType(type);
			
			dao.addTopic(subforumId, topic);
			
			return "Added a topic" + topic.toString();
		}
		else {
			return "Must be logged in to add topic!";
		}
	}
	
	@POST
	@Path("/update/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String update(	@PathParam("subforumId") String subforumId,
							@PathParam("topicId") String topicId,
							@FormParam("name") String name, 
							@FormParam("type") String type, 
							@FormParam("content") String content){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null) {
				topic.setName(name);
				topic.setType(type);
				topic.setContent(content);
				dao.saveDatabase();
				
				return "Updated topic " + topic.toString();
			}
			else {
				return "Failed to update a topic";
			}
		}
		else {
			return "Must be logged in to update topic!";
		}
		
	}
	
	
	@GET
	@Path("/delete/{subforumId}/{topicId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(	@PathParam("subforumId") String subforumId,
							@PathParam("topicId") String topicId){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Topic topic = dao.searchTopics(subforumId, topicId);
		
		if(user != null) {

			if(topic != null) {
				dao.deleteTopic(subforumId, topic);
				return "topic deleted " + topic.getName();
			}
			else {
				return "Error deleting topic";
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
			
			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null) {
				if(!user.getLikedTopics().contains(topic)) {
					
					// if user disliked before, remove dislike and put like
					if(user.getDislikedTopics().contains(topic)) {
						user.getDislikedTopics().remove(topic);
						topic.removeDislike();
						dao.saveDatabase();
						return "Removed like!";
					}
					else {
						topic.like();
						user.like(topic);
						return "Successfully liked!";
					}
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
			
			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null) {
				if(!user.getDislikedTopics().contains(topic)) {
					
					// if user liked before, remove like and put dislike
					if(user.getLikedTopics().contains(topic)) {
						user.getLikedTopics().remove(topic);
						topic.removeLike();
						dao.saveDatabase();
						return "Removed like!";
					}
					else {
						topic.dislike();
						user.dislike(topic);
						dao.saveDatabase();
						return "Successfully disliked!";
					}
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
					dao.saveDatabase();
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
					dao.saveDatabase();
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
	@Path("/report")
	@Produces(MediaType.TEXT_PLAIN)
	public String report(	@FormParam("subforumId") String subforumId,
							@FormParam("topicId") String topicId,
							@FormParam("complaintText") String complaintText) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Subforum subforum = dao.searchSubforums(subforumId);
			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null && subforum != null) {
				
				Report report =  new Report(complaintText, topic, user.getUsername());
				
				User moderator = dao.searchUser(subforum.getResponsibleModerator().getUsername());
				
				moderator.addMessage(new Message(user.getUsername(), 
												moderator.getUsername(), 
												complaintText, true, report));
				
				dao.saveDatabase();
				return "Reported " + topic.getName();
			}
			else {
				return "Failed to report";
			}
		}
		else {
			return "Must be logged to report topic!";
		}
	}
	
	@GET
	@Path("/report/delete/{messageId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteReport(@PathParam("messageId") int messageId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Message message = user.getMessages().get(messageId);
			
			Report report = message.getReport();
			
			User reportAuthor = dao.searchUser(report.getUserId());
			
			String entityAuthorId = report.getEnitityOfComplaint().getAuthor();
			User entityAuthor = dao.searchUser(entityAuthorId);
			
			reportAuthor.addMessage(new Message(user.getUsername(), 
												reportAuthor.getUsername(), 
												"Thank you for report! Reported topic will be deleted."));
			
			entityAuthor.addMessage(new Message(user.getUsername(), 
					entityAuthor.getUsername(), 
					"The topic " + report.getEnitityOfComplaint().getName() + " has been reported for violating rules.You have 24h to delete it."));
			
			return "Report author and topic author has beed notified.";
		}
		else {
			return "Must be logged to delete report !";
		}
	}
	
}
