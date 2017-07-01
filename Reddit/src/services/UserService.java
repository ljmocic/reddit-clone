package services;

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

import beans.Subforum;
import beans.User;
import dao.ApplicationDAO;
import utils.Config;

@Path("/user")
public class UserService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext context;

	ApplicationDAO dao = ApplicationDAO.getInstance();

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String register(User userToRegister) {
		
		if (userToRegister.getUsername() == null || userToRegister.getPassword() == null ||  userToRegister.getEmail()== null
				|| userToRegister.getUsername().equals("") || userToRegister.getPassword().equals("") || userToRegister.getEmail().equals("")) {
			return "Username, password and email are required fields.";
		}
		if (dao.searchUser(userToRegister.getUsername()) != null) {
			return "Username is taken, please choose different username!";
		}
		else {
			dao.addUser(userToRegister);
			dao.saveDatabase();
			return "Succesfully registered!";
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String login(User userToLogIn) {

		HttpSession session = request.getSession();
		
		if (userToLogIn.getUsername() == null || userToLogIn.getPassword() == null || 
				userToLogIn.getUsername().equals("") || userToLogIn.getPassword().equals("")) {
			return "Fill both username and password field to log in!";
		}
		if (dao.searchUser(userToLogIn.getUsername()) != null) {
			
			User user = dao.searchUser(userToLogIn.getUsername());
			
			if(user.getPassword().equals(userToLogIn.getPassword()) == true) {
				session.setAttribute("user", user);
				dao.saveDatabase();
				return "Successfully logged in!";
			}
			else {
				return "Login failed!";
			}
			
		}
		if (session.getAttribute("user") != null) {
			return "Already logged in";
		}
		else {
			return "User cannot be logged in!";
		}
	}
	
	@GET
	@Path("/update/{role}/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String update(@PathParam("role") String role, @PathParam("username") String username) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {

			if(user.getRole().equals(Config.ADMIN)) {
				
				
				if(role.equals("admin")) {
					dao.changeUserRole(username, Config.ADMIN);
				}
				else if(role.equals("moderator")) {						
					dao.changeUserRole(username, Config.MODERATOR);
				}
				else if(role.equals("user")) {
					User tempUser = dao.searchUser(username);
					
					if(tempUser.getRole().equals(Config.MODERATOR)) {
						for(Subforum subforum :dao.getSubforums()) {
							if(subforum.getResponsibleModerator().equals(tempUser.getUsername())) {
								subforum.setResponsibleModerator("admin");
							}
						}
					}
					
					dao.changeUserRole(username, Config.USER);
				}
				dao.saveDatabase();
				
				return "Successfully updated!";
			}
			else {
				return "You don't have permission to edit user role";
			}
			
		}
		else {
			return "Must be logged in to update subforum!";
		}
	}
	
	@GET
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public User active() {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			return user;
		}
		else {
			return null;
		}
	}
	
	@GET
	@Path("/seen/{messageId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String seen(@PathParam("messageId") int messageId) {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			user.getMessages().get(messageId).setSeen(true);
			dao.saveDatabase();
			return "Message seen!";
		}
		else {
			return null;
		}
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.TEXT_PLAIN)
	public String logout() {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			session.invalidate();
			return "Logged out!";
		}
		else {
			return "Already logged out!";
		}
	}
	
}
