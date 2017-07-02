package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import beans.Comment;
import beans.Message;
import beans.Report;
import beans.Subforum;
import beans.Topic;
import beans.TopicSearchRequest;
import beans.User;
import database.ApplicationDAO;

@Path("/topic")
public class TopicService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;

	ApplicationDAO dao = ApplicationDAO.getInstance();
	
	@POST
	@Path("/create/{subforumId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(@PathParam("subforumId") String subforumId, Topic topicToAdd) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			if(dao.searchTopics(subforumId, topicToAdd.getName()) == null) {
				if(!(topicToAdd.getName().equals("") || topicToAdd.getContent().equals(""))) {
					Topic topic = new Topic(topicToAdd.getName(), topicToAdd.getContent(), user.getUsername(), subforumId);
					topic.setType(topicToAdd.getType());
					
					dao.addTopic(subforumId, topic);
					
					return "Added a topic " + topic.getName();
				}
				else {
					return "Topic id and content are required fields!";
				}
				
			}
			else {
				return "Topic with that name already exists, please choose different name!";
			}
		}
		else {
			return "Must be logged in to add topic!";
		}
	}
	
	@POST
    @Path("/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadImage(InputStream uploadedInputStream) {
        String fileLocation = context.getRealPath("") + "\\";
        String imageId = UUID.randomUUID().toString()  + ".png";
        
        fileLocation += imageId;
        try {
        	File file = new File(fileLocation);
        	file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, false);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageId;
    }
	
	@POST
	@Path("/update/{subforumId}/{topicId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String update(	@PathParam("subforumId") String subforumId, 
							@PathParam("topicId") String topicId, 
							Topic topicToAdd){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Topic topic = dao.searchTopics(subforumId, topicId);
			
			if(topic != null) {
				topic.setName(topicToAdd.getName());
				topic.setType(topicToAdd.getType());
				topic.setContent(topicToAdd.getContent());
				dao.saveDatabase();
				
				return "Updated topic " + topic.getName();
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
						return "Removed dislike!";
					}
					else {
						topic.like();
						user.like(topic);
						dao.saveDatabase();
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String report(Report reportToAdd) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Subforum subforum = dao.searchSubforums(reportToAdd.getSubforumId());
			Topic topic = dao.searchTopics(reportToAdd.getSubforumId(), reportToAdd.getTopicId());
			
			if(topic != null && subforum != null) {
				
				Report report =  new Report(reportToAdd.getText(), topic.getParentSubforumName(), topic.getName(), user.getUsername());
				User moderator = dao.searchUser(subforum.getResponsibleModerator());
				
				Message message = new Message(user.getUsername(), moderator.getUsername(), reportToAdd.getSubforumId(), true, report);
				
				// Notify administrators/moderators
				moderator.addMessage(message);
				dao.sendMessageToAdministrators(message);
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
			
			user.getMessages().get(messageId).setSeen(true);
			Message message = user.getMessages().get(messageId);
			
			Report report = message.getReport();
			
			User reportAuthor = dao.searchUser(report.getUserId());
			
			Topic topic = dao.searchTopics(report.getSubforumId(), report.getTopicId());
			
			String entityAuthorId = topic.getAuthor();
			User entityAuthor = dao.searchUser(entityAuthorId);
			
			reportAuthor.addMessage(new Message(user.getUsername(), 
												reportAuthor.getUsername(), 
												"Thank you for report! Reported topic will be deleted."));
			
			entityAuthor.addMessage(new Message(user.getUsername(), 
					entityAuthor.getUsername(), 
					"The topic " + topic.getName() + " has been reported for violating rules. It will be immediately deleted!"));
			
			dao.deleteTopic(topic.getParentSubforumName(), topic);
			
			return "Report author and topic author has beed notified.";
		}
		else {
			return "Must be logged to notify report !";
		}
	}
	
	@GET
	@Path("/report/warn/{messageId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String warnReport(@PathParam("messageId") int messageId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			user.getMessages().get(messageId).setSeen(true);
			Report report = user.getMessages().get(messageId).getReport();
			
			User reportAuthor = dao.searchUser(report.getUserId());
			
			Topic topic = dao.searchTopics(report.getSubforumId(), report.getTopicId());
			
			if(topic != null) {
				String entityAuthorId = topic.getAuthor();
				User entityAuthor = dao.searchUser(entityAuthorId);
				
				reportAuthor.addMessage(new Message(user.getUsername(), 
													reportAuthor.getUsername(), 
													"Thank you for report! Reported topic author is notified about breaking rules."));
				
				entityAuthor.addMessage(new Message(user.getUsername(), 
						entityAuthor.getUsername(), 
						"Warning, the topic " + topic.getName() + " has been reported for violating rules."));
				
				dao.saveDatabase();
				
				return "Report author and topic author has been notified.";
			}
			else {
				return "Report has been already resolved.";
			}
		}
		else {
			return "Must be logged in!";
		}
	}
	
	@GET
	@Path("/report/reject/{messageId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String rejectReport(@PathParam("messageId") int messageId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			user.getMessages().get(messageId).setSeen(true);
			Report report = user.getMessages().get(messageId).getReport();
			
			Topic topic = dao.searchTopics(report.getSubforumId(), report.getTopicId());
			
			if(topic != null) {
				User reportAuthor = dao.searchUser(report.getUserId());
				reportAuthor.addMessage(new Message(user.getUsername(), 
													reportAuthor.getUsername(), 
													"Your report on " + topic.getName() + " has been rejected!"));
				
				dao.saveDatabase();
				return "Report successfully rejected, report author is notified.";
			}
			else {
				return "Report has been already resolved.";
			}
		}
		else {
			return "Must be logged in!";
		}
	}
	
	@POST
	@Path("/advancedSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Topic> searchTopics(TopicSearchRequest query) {
		
		List<Topic> result = new ArrayList<Topic>();
		
		
		boolean subforumIdSearchStatus = false, contentSearchStatus = false, 
				authorSearchStatus = false, topicIdSearchStatus = false;
		
		if(!query.getSubforumId().equals("")) {
			subforumIdSearchStatus = true;
		}
		if(!query.getTopicId().equals("")) {
			topicIdSearchStatus = true;
		}
		if(!query.getContent().equals("")) {
			contentSearchStatus = true;
		}
		if(!query.getAuthor().equals("")) {
			authorSearchStatus = true;
		}
		
		for(Subforum subforum: dao.getSubforums()) {
			for(Topic topic: subforum.getTopics()) {
				boolean flag = true;
				
				if(subforumIdSearchStatus) {
					if(topic.getParentSubforumName().contains(query.getSubforumId()) && flag) {
						flag = true;
					}
					else {
						flag = false;
					}
				}
				
				if(contentSearchStatus) {
					if(topic.getContent().contains(query.getContent()) && flag) {
						flag = true;
					}
					else {
						flag = false;
					}
				}
				
				if(authorSearchStatus) {
					if(topic.getAuthor().contains(query.getAuthor()) && flag) {
						flag = true;
					}
					else {
						flag = false;
					}
				}
				
				if(topicIdSearchStatus) {
					if(topic.getName().contains(query.getTopicId()) && flag) {
						flag = true;
					}
					else {
						flag = false;
					}
				}
				
				if(flag) {
					result.add(topic);
				}
				
			}
		}
		
		return result;
	}
	
}
