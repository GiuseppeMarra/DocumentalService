package org.gm.docdrive.ws.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.gm.docdrive.DocDrive;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.User;
import org.gm.docdrive.security.RequiresAuthorization;
import org.gm.docdrive.util.ModelViewConverter;
import org.gm.docdrive.ws.model.FileVW;
import org.gm.docdrive.ws.model.FolderVW;

@Path("/")
public class DocDriveAPI extends Application {

	private DocDrive drive = new DocDrive();

	@GET
	@RequiresAuthorization
	@Produces("application/json")
	public Response listRoot(@Context ContainerRequestContext crc){
		
		String s = (String) crc.getProperty("rootId");
		File parent = new File(s);
		List<File> list = drive.list(parent);
		FolderVW res = new FolderVW();
		res.setFolderId(s);
		res.setFiles(ModelViewConverter.fromFileList(list));
		return Response.status(200).entity(res).build();
		
		
	}
	
	
	@POST
	@RequiresAuthorization
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	@Path("{id}/upload")
	public Response insertFile(@Context ContainerRequestContext crc, @FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader, @PathParam("id") String parentId) {

		File f = new File();
		f.setName(contentDispositionHeader.getFileName());
		f.setCreatedTime(contentDispositionHeader.getCreationDate());
		f.setModifiedTime(contentDispositionHeader.getModificationDate());
		f.setParent(parentId);
		User u = (User) crc.getProperty("user");
		try {
			drive.insertFile(f, fileInputStream, u);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}


		return Response.status(200).build();

	}


	@GET
	@RequiresAuthorization
	@Produces("application/json")
	@Path("{id}/download")
	public Response downloadFile(@PathParam("id") String fileId) {

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
        FileVW f = ModelViewConverter.fromFile((drive.getFile(new File(fileId))));
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = "+f.getName())
                .build();
	}

	@GET
	@RequiresAuthorization
	@Produces("application/json")
	@Path("{id}/list")
	public Response list(@PathParam("id") String parentId) {

		File parent = new File(parentId);
		List<File> list = drive.list(parent);
		FolderVW res = new FolderVW();
		res.setFolderId(parentId);
		res.setFiles(ModelViewConverter.fromFileList(list));
		return Response.status(200).entity(res).build();

	}
	
//
//	public Message addProperty(File target, String key, String value) {
//
//		return drive.addProperty(target, key, value);
//
//	}
//
//	public Message addProperties(File target, Map<String, String> props) {
//
//		return drive.addProperties(target, props);
//
//	}

	
//	public void updateFile(File filter, File toUpdate) {
//		
//		drive.updateFile(filter, toUpdate);
//		
//	}


	@POST
	@RequiresAuthorization
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	@Path("{id}/folder/new")
	public Response insertFolder(@Context ContainerRequestContext crc, @PathParam("id") String parentId, @FormParam("folder_name") String folderName) {
		File parent = new File();
		parent.setId(parentId);
		File f = new File();
		f.setName(folderName);
		return Response.status(200).entity(ModelViewConverter.fromFile(drive.insertFolder(parent, f,(User) crc.getProperty("user")))).build();
		
	}

	@POST
	@RequiresAuthorization
	@Produces("application/json")
	@Path("{id}/delete")
	public Response delete(@PathParam("id") String fileId) {
		File f = new File(fileId);
		drive.delete(f);
		return Response.status(200).build();
	}

}
