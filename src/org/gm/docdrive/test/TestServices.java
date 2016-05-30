package org.gm.docdrive.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.gm.docdrive.ws.model.FileVW;
import org.gm.docdrive.ws.model.FolderVW;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestServices {


	public static WebTarget mainTarget;
	public static Client client;

	static{
		ClientConfig config = new ClientConfig();
		config.register(MultiPartFeature.class);
		client = ClientBuilder.newClient(config);
	}

	public static void main(String[] args) {

		mainTarget = client.target("http://localhost:8080");

		String token = login("peppe3", "test");
		FolderVW rootVW = listRoot(token);
		FileVW folder = insertFolder(rootVW.getFolderId(), "sixthFolder", token);
		rootVW = listRoot(token);
		try {
			System.out.println((new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(rootVW));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int status = insertFile(folder.getId(), "test.txt", token);
		System.out.println(status);
		FolderVW nestedFolder = listFolder(folder.getId(), token);
		try {
			System.out.println((new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(nestedFolder));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Get the id of the last inserted file
		List<FileVW> files = nestedFolder.getFiles();
		files.sort((f1,f2)-> f2.getCreatedTime().compareTo(f1.getCreatedTime()));
		FileVW file = files.get(0);
		delete(file, token);

	}



	private static int delete(FileVW file, String token) {

		WebTarget target = mainTarget.path("drive/rest/"+file.getId()+"/delete");
		Invocation.Builder invocationBuilder =
				target.request(MediaType.APPLICATION_JSON_TYPE);
		invocationBuilder = invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer "+token);
		return invocationBuilder.post(Entity.json(null)).getStatus();
		
	}



	private static FolderVW listFolder(String id, String token) {

		WebTarget target = mainTarget.path("drive/rest/"+id+"/list");
		Invocation.Builder invocationBuilder =
				target.request(MediaType.APPLICATION_JSON_TYPE);
		invocationBuilder = invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer "+token);
		return invocationBuilder.get(FolderVW.class);

	}

	private static FileVW insertFolder(String parentId, String folderName, String token) {
		WebTarget target = mainTarget.path("drive/rest/"+parentId+"/folder/new");
		Form form = new Form();
		form.param("folder_name", folderName);

		Invocation.Builder invocationBuilder =
				target.request(MediaType.APPLICATION_JSON_TYPE);
		invocationBuilder = invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer "+token);
		return invocationBuilder.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),
				FileVW.class);

	}

	private static int insertFile(String parentId, String filePath, String token){

		WebTarget webTarget = mainTarget.path("drive/rest/"+parentId+"/upload");
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
				new File(filePath));
		multiPart.bodyPart(fileDataBodyPart);

		Response s = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
				.header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));
		try {
			multiPart.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s.getStatus();



	}

	private static FolderVW listRoot(String token) {

		WebTarget target = mainTarget.path("drive/rest/");
		Invocation.Builder invocationBuilder =
				target.request(MediaType.APPLICATION_JSON_TYPE);
		invocationBuilder = invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer "+token);
		return invocationBuilder.get(FolderVW.class);
	}

	private static String login(String username, String password) {


		WebTarget target = mainTarget.path("drive/rest/auth");

		Form form = new Form();
		form.param("username", username);
		form.param("password", password);

		String token =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						String.class);
		return token;
	}

}
