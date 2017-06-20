package services;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Subforum;
import beans.Topic;
import beans.User;
import dao.ApplicationDAO;

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

			Subforum subforum = new Subforum(name, description, rules, null, null);
			dao.addSubforum(subforum);
			return "Added a forum" + subforum.toString();
		}
		else {
			return "Must be logged in to add subforum!";
		}
	}
	
	@GET
	@Path("/topics")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Topic> subforums() {
		return dao.getSubforums().get(0).getTopics();
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
	
	@POST
	@Path("/delete")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@FormParam("name") String name){
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			if(dao.searchSubforums(name) != null) {
				dao.deleteSubforum(name);
				return "Forum deleted " + name;
			}
			else {
				return "Error deleting " + name;
			}
		}
		else {
			return "Must be logged in to add subforum!";
		}
	}
	
}
