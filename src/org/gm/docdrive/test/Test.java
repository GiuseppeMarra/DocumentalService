package org.gm.docdrive.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.gm.docdrive.DocDrive;
import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.impl.mongo.MongoConstants;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight;
import org.gm.docdrive.model.UserRight.Right;
import org.gm.docdrive.util.SecurityUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class Test {

	public static void main(String[] args) throws JsonProcessingException {

		//		insertRoot();
//		DocDrive drive = new DocDrive();
//		
//		String rootId = "891188bf-b1b6-498f-b04b-04de3e89b0cd";
//
//		//Insert folders
//		for(int i=0; i<7; i++){
//			File f = new File();
//		}
//		File f  = new File();
//		f.setName("peppe3File.txt");
//		f.setParent("891188bf-b1b6-498f-b04b-04de3e89b0cd"); //a previously created folder				
//
//		UserRight rig = new UserRight();
//		rig.setIdUser("3b2b7245-0575-4648-8066-a8a10864fe79");
//		rig.setRight(Right.WRITE);
//
//		f.setOwner(rig);
//
//		File inserted = null;
//		try {
//			inserted = drive.insertFile(f, new FileInputStream("test.txt"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		//				
//		//		//Print the tree
//		//		File root = drive.getFile(new File(Constants.ROOT_ID));
//		//		print(drive,root,0);
	}


	public static void print(DocDrive drive, File f, int level){

		for(int i=0;i<level;i++) System.out.print("---");
		System.out.println(f.getName()+"-----("+f.getId()+")");
		if(f.getKind().equals(Kind.FOLDER)){
			List<File> c = drive.list(f);
			for (File sub : c){
				if(sub!=null)
					print(drive, sub, level+1);
			}

		}

	}


	public static void insertRoot(){

		File f = new File();
		f.setName("root");
		f.setId("cb0a9126-457f-4acd-94b6-28c7c77f3601");
		f.setKind(Kind.FOLDER);
		MongoClient client = new MongoClient();
		MongoCollection<Document> db = client.getDatabase(MongoConstants.DB_NAME).getCollection(MongoConstants.FILES_COLLECTION);
		try {
			db.insertOne(Document.parse((new ObjectMapper()).writeValueAsString(f)));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
	}



	public static void insertUser(String userId){
//		User u = new User();
//		u.setDisplayName("peppe");
//		u.setEmailAddress("peppe@peppe.it");
//		u.setId(userId);
//		u.setUsername("peppe3");
//		u.setPassword(SecurityUtil.getMD5Hash("test"));
//		u.setPictureUrl("www.images.it/peppe_image.png");
//
//		MongoClient client = new MongoClient();
//		MongoCollection<Document> db = client.getDatabase(MongoConstants.DB_NAME).getCollection(MongoConstants.USERS_COLLECTIONS);
//		try {
//			db.insertOne(Document.parse((new ObjectMapper()).writeValueAsString(u)));
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		DocDrive drive = new DocDrive();
//		File root = new File();
//		root.setName("peppe3Root");
//		UserRight r = new UserRight();
//		r.setIdUser(u.getId());
//		r.setRight(Right.WRITE);
//		root.setOwner(r);
//		drive.insertFolder(root);


	}

}
