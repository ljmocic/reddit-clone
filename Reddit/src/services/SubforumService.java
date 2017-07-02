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

import beans.Message;
import beans.Report;
import beans.Subforum;
import beans.SubforumSearchRequest;
import beans.Topic;
import beans.User;
import database.ApplicationDAO;
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String create(Subforum subforumToAdd) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(user.getRole().equals(Config.MODERATOR) || user.getRole().equals(Config.ADMIN)) {
				if(dao.searchSubforums(subforumToAdd.getName()) == null) {
					if(!(subforumToAdd.getName().equals("") || subforumToAdd.getDescription().equals("") || subforumToAdd.getRules().equals(""))) {
						Subforum subforum = new Subforum(subforumToAdd.getName(), 
								subforumToAdd.getDescription(), 
								subforumToAdd.getRules(), 
								subforumToAdd.getIcon(), 
								user.getUsername());
						
						dao.addSubforum(subforum);
						return "Added a forum " + subforumToAdd.getName();
					}
					else {
						return "Name, description and rules are required fileds!";
					}
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
	
	@POST
    @Path("/icon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadIcon(InputStream uploadedInputStream) {
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
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Topic topic = dao.searchTopics(subforumId, topicId);
		
		if(topic != null) {
			topic.click();
			if(user != null) {
				user.addClickedTopic(topic.getName());
				dao.saveDatabase();
			}
			return topic;
		}
		else {
			return null;
		}
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.TEXT_PLAIN)
	public String update(Subforum subforumToAdd){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			Subforum subforum = dao.searchSubforums(subforumToAdd.getName());
			
			if(subforum != null) {
				subforum.setName(subforumToAdd.getName());
				subforum.setDescription(subforumToAdd.getDescription());
				subforum.setResponsibleModerator(subforumToAdd.getResponsibleModerator());
				subforum.setRules(subforumToAdd.getRules());
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
			if(user.getRole().equals(Config.MODERATOR) || user.getRole().equals(Config.ADMIN)) {
				if(dao.searchSubforums(subforumId) != null) {
					dao.deleteSubforum(subforumId);
					return "Forum deleted " + subforumId;
				}
				else {
					return "Error deleting " + subforumId;
				}
			}
			return "You do not have permission to delete subforum!";
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
		
		if(user != null) {
			user.followForum(subforumId);
			dao.saveDatabase();
			return "Followed subforum " + subforumId;
		}
		else {
			return "Must be logged in to follow a subforum!";
		}
	}
	
	@GET
	@Path("/unfollow/{subforumId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String unfollow(@PathParam("subforumId") String subforumId){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			user.unfollowForum(subforumId);
			dao.saveDatabase();
			return "Unfollowed subforum " + subforumId;
		}
		else {
			return "Must be logged in to follow a subforum!";
		}
	}
	
	@POST
	@Path("/report")
	@Produces(MediaType.TEXT_PLAIN)
	public String report(Report reportToAdd) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			
			Subforum subforum = dao.searchSubforums(reportToAdd.getSubforumId());
			
			if(subforum != null) {
				
				Report report =  new Report(reportToAdd.getText(), reportToAdd.getSubforumId(), "", user.getUsername());
				
				User moderator = dao.searchUser(subforum.getResponsibleModerator());
				
				Message message = new Message(user.getUsername(), moderator.getUsername(), reportToAdd.getText(), true, report);
				
				// Notify administrators/moderators
				dao.sendMessageToAdministrators(message);
				dao.saveDatabase();
				
				return "Reported " + subforum.getName();
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
			
			Report report = user.getMessages().get(messageId - 1).getReport();
			
			User admin = dao.searchUser("admin");
			
			user.addMessage(new Message(admin.getUsername(), user.getUsername(), 
			"Thank you for report! Reported subforum will be deleted."));
			
			admin.addMessage(new Message(user.getUsername(), admin.getUsername(),
			"Warning, the subforum " + report.getSubforumId() + " has been reported for violating rules. It will be immediately deleted!"));
			
			dao.deleteSubforum(report.getSubforumId());
			
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
			
			Report report = user.getMessages().get(messageId - 1).getReport();
			
			User moderator = dao.searchUser(dao.searchSubforums(report.getSubforumId()).getResponsibleModerator());
			
			user.addMessage(new Message("", user.getUsername(), 
			"Thank you for report! Reported subforum author is notified about breaking rules."));
			
			moderator.addMessage(new Message("", moderator.getUsername(),
			"Warning, the subforum " + report.getSubforumId() + " has been reported for violating rules."));
			
			dao.saveDatabase();
			
			return "Administrator has been notified.";
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
			
			Report report = user.getMessages().get(messageId - 1).getReport();
			User reportAuthor = dao.searchUser(report.getUserId());
			reportAuthor.addMessage(new Message(user.getUsername(), 
												reportAuthor.getUsername(), 
			"Your report on subforum " + report.getSubforumId() + " has been rejected!"));
			
			dao.saveDatabase();
			
			return "Report successfully rejected, report author is notified.";
		}
		else {
			return "Must be logged in!";
		}
	}

	@POST
	@Path("/advancedSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subforum> searchSubforums(SubforumSearchRequest query) {
		
		List<Subforum> result = new ArrayList<Subforum>();
		
		
		boolean subforumIdSearchStatus = false, descriptionSearchStatus = false, moderatorSearchStatus = false;
		
		if(!query.getSubforumId().equals("")) {
			subforumIdSearchStatus = true;
		}
		if(!query.getDescription().equals("")) {
			descriptionSearchStatus = true;
		}
		if(!query.getResponsibleModeratorId().equals("")) {
			moderatorSearchStatus = true;
		}
		
		for(Subforum subforum: dao.getSubforums()) {
			boolean flag = true;
			
			if(subforumIdSearchStatus) {
				if(subforum.getName().contains(query.getSubforumId()) && flag) {
					flag = true;
				}
				else {
					flag = false;
				}
			}
			if(descriptionSearchStatus) {
				if(subforum.getDescription().contains(query.getDescription()) && flag) {
					flag = true;
				}
				else {
					flag = false;
				}
			}
			if(moderatorSearchStatus) {
				if(subforum.getResponsibleModerator().contains(query.getResponsibleModeratorId()) && flag) {
					flag = true;
				}
				else {
					flag = false;
				}
			}
			
			if(flag) {
				result.add(subforum);
			}
		}
		
		return result;
	}
	
}
