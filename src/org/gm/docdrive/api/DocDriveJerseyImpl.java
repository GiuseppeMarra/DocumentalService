package org.gm.docdrive.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.gm.docdrive.api.interfaces.DocDriveServices;
import org.gm.docdrive.commons.Constants;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


@Path("/")
public class DocDriveJerseyImpl implements DocDriveServices {
	
	
	private MongoClient mongoClient;
	
	public DocDriveJerseyImpl(){
		mongoClient = new MongoClient();
	}

	@Override
	public File createFile(String name, String authToken) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createFolder(String name, String authToken) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changeDirectory(String path, String authToken) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("list")
	@Produces("application/json")
	public Response listFiles(@QueryParam("authToken")String authToken) throws IOException {
		
		return null;
		
	}

}
