package org.gm.docdrive.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.gm.docdrive.model.User;

@RequiresAuthorization
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter{


	public AuthorizationFilter(){



	}


	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = 
				requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);


		// Check if the HTTP Authorization header is present and formatted correctly 
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();
		String encodedRelativePath = requestContext.getUriInfo().getPath();

		SecurityManager secman = new SecurityManager();
		String fileId ="";
		String action ="";
		User u = secman.tokenIsRegistered(token);
		if(u==null){
			requestContext.abortWith(
					Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"error\": \"Invalid authentication token.\"}").build());
			return;

		}
		else{
			requestContext.setProperty("user", u);
			if(encodedRelativePath.equals("")){ //implicit root list request			

				fileId = secman.getRootId(token);
				requestContext.setProperty("rootId", fileId);
				return;
			}
			else{
				// Validate the token
				int index =encodedRelativePath.indexOf("/");
				fileId = encodedRelativePath.substring(0, index);
				action = encodedRelativePath.substring(index+1);


				switch(secman.authorize(token, fileId, action).getStatus()){

				case TOKEN_VALID:{

					return;
				}

				case NOT_ALLOWED:{

					requestContext.abortWith(
							Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"You are not allowed to perform this action on this file.\"}").build());
					return;

				}

				case EXPIRED:{
					requestContext.abortWith(
							Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"error\": \"token expired\"}").build());
					return;

				}

				default:{
					requestContext.abortWith(
							Response.status(Response.Status.FORBIDDEN).build());
					return;

				}

				}
			}


		}
	}


}
