package org.gm.docdrive.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.gm.docdrive.DocDrive;
import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.impl.mongo.MongoConstants;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class Test {

	public static void main(String[] args) throws JsonProcessingException {

//		insertRoot();
		DocDrive drive = new DocDrive();
//
		File f  = new File();
		f.setName("11File.txt");
		f.setParent("1cb50429-e3c6-492a-87d4-137b167406ab");
		
		try {
			drive.insertFile(f, new FileInputStream("test.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File root = drive.getFile(new File(Constants.ROOT_ID));
		print(drive,root,0);
		
		

		
//		ObjectMapper mapper = new ObjectMapper();
//		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(f));

		
	}
	
	
	public static void print(DocDrive drive, File f, int level){
	
		for(int i=0;i<level;i++) System.out.print("---");
		System.out.println(f.getName());
		if(f.getKind().equals(Kind.FOLDER)){
			List<File> c = drive.list(f);
			for (File sub : c){
				if(sub!=null)
					print(drive, sub, level+1);
			}
			
		}
		
	}
	
	
	private static void insertRoot(){
		
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
	}

}
