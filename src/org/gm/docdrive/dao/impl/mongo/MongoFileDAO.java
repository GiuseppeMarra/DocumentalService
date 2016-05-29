package org.gm.docdrive.dao.impl.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOException;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight;
import org.gm.docdrive.model.UserRight.Right;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class MongoFileDAO implements FileDAO {


	private ObjectMapper mapper;
	private MongoClient mongoClient;
	private MongoCollection<Document> db;

	public MongoFileDAO(){
		mapper = new ObjectMapper();
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(MongoConstants.DB_NAME).getCollection(MongoConstants.FILES_COLLECTION);

	}


	public File insertFile(File f) throws FileDAOException{

		/*
		 * Check the consistency of the File f object and insert elements
		 */

		f.setCreatedTime(new Date());
		String[] splits = f.getName().split("\\.");
		String ext = splits.length > 1 ?splits[splits.length-1] : null;
		f.setFileExtension(ext);
		if(f.getParent()==null || f.getParent().isEmpty()){
			throw new FileDAOException("No parent provided.");
		}

		String jsonString ="{}";
		try {
			jsonString = mapper.writeValueAsString(f);


		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		Document mongoFile = Document.parse(jsonString);
		db.insertOne(mongoFile);
		return getFile(f);
	}

	public File getRoot(User u) throws FileDAOException{
		if(u.getId()==null || u.getId()==""){
			throw new FileDAOException("The provided user needs an id.");
		}

		try {

			File f = new File();
			UserRight ur = new UserRight();
			ur.setIdUser(u.getId());
			ur.setRight(Right.WRITE);
			f.setOwner(ur);

			Document d = Document.parse(mapper.writeValueAsString(f));
			d.append("parent", null);
			Document res =  db.find(d).first();
			if(res!=null){
				File root;
				root = mapper.readValue(res.toJson(), File.class);
				return root;
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new FileDAOException("Impossible to get the root of the provided user");
		}

		return null;


	}


	//FIXME Use the entire file and not only the id!!!!!!
	public File getFile(File f) throws FileDAOException {

		try {

			Document filter = new Document().append("id", f.getId());
			Long count  =db.count(filter);
			if(count >1)
				throw new FileDAOException("More then one file");
			else{
				return mapper.readValue(db.find(filter).first().toJson(), File.class);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

	}


	public List<File> list(File parent) throws FileDAOException {
		File parentReal= null;


		if(parent.getId()== null || parent.getId().isEmpty()){
			throw new FileDAOException("Input file does not have an id. It cannot be recognized.");
		}

		parentReal = getFile(parent);
		if(parentReal == null)
			throw new FileDAOException("Provided parent file does not exist.");
		if(!parentReal.getKind().equals(Kind.FOLDER)){
			throw new FileDAOException("Provided parent file is not a folder.");
		}

		List<File> res = new ArrayList<File>();
		FindIterable<Document> resColl = db.find(new Document().append("parent", parentReal.getId()));
		for(Document n : resColl)
			try {
				res.add(mapper.readValue(n.toJson(), File.class));
			} catch (IOException e) {
				e.printStackTrace();
				throw new FileDAOException("Error fetching files with provided parent.");

			}

		return res;
	}


//	@Override
//	public Message updateFile(File filter, File toUpdate) throws FileDAOException{
//		toUpdate.setModifiedTime(new Date());
//		toUpdate.setId(null);
//		String jsonFilter = null;
//		String jsonToUpdate = null;
//		try {
//			jsonFilter = mapper.writeValueAsString(filter);
//			jsonToUpdate = mapper.writeValueAsString(toUpdate);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			return Message.DB_MODEL_CREATION_FILED;
//		}
//
//		Document searchFilter = Document.parse(jsonFilter);
//		Document toUpdateDoc = Document.parse(jsonToUpdate);
//		db.updateMany(searchFilter, new Document("$set", toUpdateDoc));
//		return Message.SUCCESS;
//	}


	@Override
	public File delete(File f) throws FileDAOException {
		String jsonFilter = null;
		try{
			jsonFilter = mapper.writeValueAsString(f);
		}
		catch(JsonProcessingException e){
			e.printStackTrace();
			return null;
		}
		Document mongoFilter = Document.parse(jsonFilter);
		db.deleteMany(mongoFilter);
		return f;
	}


	@Override
	public  List<File>  getAllowedFileForUser(String id) throws FileDAOException{

		String query = "{ $or : [ {\"sharedWith.idUser\": \"b"+id+"\"}, {\"owner.idUser\": \""+id+"\"}]}";
		Document filter = Document.parse(query);

		List<File> res = new ArrayList<File>();
		FindIterable<Document> resColl = db.find(filter);
		for(Document n : resColl)
			try {
				res.add(mapper.readValue(n.toJson(), File.class));
			} catch (IOException e) {
				e.printStackTrace();
				throw new FileDAOException("Error fetching allowed files for user.");

			}


		return res;
	}



}
