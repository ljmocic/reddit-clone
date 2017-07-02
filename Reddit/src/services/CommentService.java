package services;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Comment;
import beans.Topic;
import beans.User;
import database.ApplicationDAO;

@Path("/comment")
public class CommentService {

	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;

	ApplicationDAO dao = ApplicationDAO.getInstance();
	
	@GET
	@Path("/create/{id}/{commentText}")
	@Produces(MediaType.TEXT_PLAIN)
	public String create(	@PathParam("id") String id,
							@PathParam("commentText") String commentText) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String[] arr = id.split("_");
		
		String subforumId = arr[0];
		String topicId = arr[1];
		
		if(user != null) {
			
			Topic topic = dao.searchTopics(subforumId, topicId);
			if(!commentText.equals("")){
				topic.getComments().add(new Comment(user.getName(), commentText, subforumId, topicId, null));		
				return "Successfully added a comment";
			}
			else {
				return "Comment cannot be empty!";
			}
		}
		else {
			return "Must be logged in to comment!";
		}
	}
	
	@GET
	@Path("/delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@PathParam("id") String id) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String[] arr = id.split("_");
		
		String subforumId = arr[0];
		String topicId = arr[1];
		String commentId = arr[2];
		
		if(user != null) {
			Topic topic = dao.searchTopics(subforumId, topicId);
			Comment comment = dao.searchComments(subforumId, topicId, commentId);
			topic.getComments().remove(comment);
			return "Comment deleted!";
		}
		else {
			return "Must be logged in to comment!";
		}
	}
	
	@GET
	@Path("/like/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String like(	@PathParam("id") String id) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String[] arr = id.split("_");
		
		String subforumId = arr[0];
		String topicId = arr[1];
		String commentId = arr[2];
		
		if(user != null) {
			
			Comment comment = dao.searchComments(subforumId, topicId, commentId);
			
			if(comment != null) {
				if(!user.getLikedComments().contains(comment)) {
					
					// if user disliked before, remove dislike and put like
					if(user.getDislikedComments().contains(comment)) {
						user.getDislikedComments().remove(comment);
						comment.removeDislike();
						dao.saveDatabase();
						return "Removed dislike comment!";
					}
					else {
						comment.like();
						user.like(comment);
						dao.saveDatabase();
						return "Successfully liked comment!";
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
			return "Must be logged to like comment!";
		}
		
	}
	
	@GET
	@Path("/dislike/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String dislike(	@PathParam("id") String id) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String[] arr = id.split("_");
		
		String subforumId = arr[0];
		String topicId = arr[1];
		String commentId = arr[2];
		
		if(user != null) {
			
			Comment comment = dao.searchComments(subforumId, topicId, commentId);
			
			if(comment != null) {
				if(!user.getDislikedComments().contains(comment)) {
					
					// if user liked before, remove dislike and put like
					if(user.getLikedComments().contains(comment)) {
						user.getLikedComments().remove(comment);
						comment.removeLike();
						dao.saveDatabase();
						return "Removed like comment!";
					}
					else {
						comment.dislike();
						user.dislike(comment);
						dao.saveDatabase();
						return "Successfully disliked comment!";
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
			return "Must be logged to dislike comment!";
		}
		
	}
	

}
