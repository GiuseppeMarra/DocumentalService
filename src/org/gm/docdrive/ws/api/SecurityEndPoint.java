package org.gm.docdrive.ws.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.dao.interfaces.UserDAO;
import org.gm.docdrive.dao.interfaces.UserDAOFactory;
import org.gm.docdrive.model.User;
import org.gm.docdrive.security.SecurityManager;

@Path("/auth")
public class SecurityEndPoint {
	
	private FileDAO fileDao = FileDAOFactory.getInstance();
	private UserDAO userDao = UserDAOFactory.getInstance();
	
	
	@POST
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	public Response authenticate(@FormParam("username") String username, 
			@FormParam("password") String password) {


		SecurityManager secManager = new SecurityManager();
		User u = null;
		if( (u = secManager.authenticate(username, password))!=null){
			String token = secManager.issueToken(u);
			return Response.ok(token).build();

		}
		else{
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}      
	}
	
	@POST
	@Produces("application/json")
	@Path("{token}/renew")
	private Response renew(@PathParam("token") String token) {

		return Response.ok(token).build();

	}

}
