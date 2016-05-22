package org.gm.docdrive.ws.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gm.docdrive.DocDrive;
import org.gm.docdrive.dao.interfaces.Message;
import org.gm.docdrive.model.File;

@Path("/")
public class DocDriveAPI extends Application {

	private DocDrive drive = new DocDrive();

	
//	static{
//		// Create JAX-RS application.
//		final Application application = new ResourceConfig()
//		    .packages("org.glassfish.jersey.examples.multipart")
//		    .register(MultiPartFeature.class);
//	}
//	
//	@POST
//	@Produces("application/json")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public File insertFile(@FormDataParam("file") File f, InputStream is) throws IOException {
//
//		return drive.insertFile(f, is);
//
//	}

	public File getFile(File f) {

		return drive.getFile(f);
	}

	@GET
	@Produces("application/json")
	@Path("list/{parentId}")
	public Response list(@PathParam("parentId") String parentId) {

		File parent = new File(parentId);
		return Response.status(200).entity(drive.list(parent)).build();

	}
	

	public Message addProperty(File target, String key, String value) {

		return drive.addProperty(target, key, value);

	}

	public Message addProperties(File target, Map<String, String> props) {

		return addProperties(target, props);

	}

	
	public void updateFile(File filter, File toUpdate) {
		
		drive.updateFile(filter, toUpdate);
		
	}

	@POST
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("new/folder")
	public File insertFolder(File f) {
		
		return drive.insertFolder(f);
		
	}


	public File delete(File f) {
		
		return drive.delete(f);
		
	}

}
