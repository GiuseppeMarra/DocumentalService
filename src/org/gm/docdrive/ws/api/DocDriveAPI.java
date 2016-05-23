package org.gm.docdrive.ws.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.gm.docdrive.DocDrive;
import org.gm.docdrive.dao.interfaces.Message;
import org.gm.docdrive.model.File;

@Path("/")
public class DocDriveAPI extends Application {

	private DocDrive drive = new DocDrive();

	
	
	@POST
	@Produces("application/json")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("upload")
	public Response insertFile(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader, @FormDataParam("parentId") String parentId) {

		File f = new File();
		f.setName(contentDispositionHeader.getFileName());
		f.setCreatedTime(contentDispositionHeader.getCreationDate());
		f.setModifiedTime(contentDispositionHeader.getModificationDate());
		f.setParent(parentId);
		try {
			drive.insertFile(f, fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}


		return Response.status(200).build();

	}


	@GET
	@Produces("application/json")
	@Path("download/{fileId}")
	public Response downloadFile(@PathParam("fileId") String fileId) {

		StreamingOutput fileStream =  new StreamingOutput() 
        {
            @Override
            public void write(java.io.OutputStream out) throws IOException, WebApplicationException 
            {
                try
                {
                	InputStream in =  drive.dowloadFileAsStream(new File(fileId));
                	byte[] buffer = new byte[16384];
                	int len;
                	while ((len = in.read(buffer)) != -1) {
                	    out.write(buffer, 0, len);
                	}
                    out.flush();
                } 
                catch (Exception e) 
                {
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        File f = drive.getFile(new File(fileId));
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = "+f.getName())
                .build();
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

		return drive.addProperties(target, props);

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

	@POST
	@Produces("application/json")
	@Path("delete/{fileId}")
	public Response delete(@PathParam("fileId") String fileId) {
		File f = new File(fileId);
		drive.delete(f);
		return Response.status(200).build();
	}

}
