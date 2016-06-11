package org.gm.docdrive.ws.api;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.gm.docdrive.model.User;
import org.gm.docdrive.security.SecurityManager;
import org.gm.docdrive.security.SecurityManager.SecurityResponse;
import org.gm.docdrive.ws.model.Error;

@Path("/auth")
public class SecurityEndPoint {
	
	private SecurityManager secManager = new SecurityManager();

	
	
	@POST
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	public Response authenticate(@FormParam("username") String username, 
			@FormParam("password") String password) {


		User u = null;
		if( (u = secManager.authenticate(username, password))!=null){
			String token = secManager.issueToken(u);
			return Response.ok(token).build();

		}
		else{
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}      
	}
	

	private Response renew(@PathParam("token") String token) {

		return Response.ok(token).build();

	}
	
	
	public Response register(@FormParam("username") String username,
								 @FormParam("password") String password,
								 @FormParam("emailAddress") String email,
								 @FormParam("displayName") String displayName,
								 @FormParam("pictureUrl") String pictureUrl){
		User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		u.setEmailAddress(email);
		u.setDisplayName(displayName);
		u.setId(UUID.randomUUID().toString());
		u.setPictureUrl(pictureUrl);
		
		SecurityResponse<Boolean> res = secManager.validateUser(u);
		if(res.getValue()){
			secManager.register(u);
			return Response.status(Response.Status.OK).build(); 
		}
		else{
			return Response.status(Response.Status.FORBIDDEN).entity(new Error(0, res.getStatus().getValue())).build();
		}

		
		
		
	}
	
	
	public Response checkForUsername(@FormParam("username") String username){
		
		SecurityResponse<Boolean> res =secManager.checkForUsername(username);
		if(res.getValue())
			return Response.status(Response.Status.OK).build();
		else{
			return Response.status(Response.Status.FORBIDDEN).entity(new Error(-1, res.getStatus().getValue())).build();
		}
		
	}
	
	public Response checkForEmail(@FormParam("email") String email){
		SecurityResponse<Boolean> res =secManager.checkForEmail(email);

		if(res.getValue())
			return Response.status(Response.Status.OK).build();
		else{
			return Response.status(Response.Status.FORBIDDEN).entity(new Error(-1, res.getStatus().getValue())).build();
		}
		
	}

}
