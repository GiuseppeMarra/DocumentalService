package org.gm.docdrive.dao.impl.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOException;
import org.gm.docdrive.dao.interfaces.Message;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;

import com.fasterxml.jackson.annotation.JsonInclude;
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


	public Message insertFile(File f) {

		/*
		 * Check the consistency of the File f object and insert elements
		 */

		f.setCreatedTime(new Date());
		String[] splits = f.getName().split("\\.");
		String ext = splits.length > 1 ?splits[splits.length-1] : null;
		f.setFileExtension(ext);
		if(f.getParent()==null || f.getParent().isEmpty())
			f.setParent(Constants.ROOT_ID);

		String jsonString ="{}";
		try {
			jsonString = mapper.writeValueAsString(f);

			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Message.DB_MODEL_CREATION_FILED;
		}

		Document mongoFile = Document.parse(jsonString);
		db.insertOne(mongoFile);
		return Message.SUCCESS;
	}

	public File getRoot() throws FileDAOException{

		Document res =  db.find(
				new Document("id", Constants.ROOT_ID)).first();
		File root;
		try {
			root = mapper.readValue(res.toJson(), File.class);
			return root;

		} catch (IOException e) {
			e.printStackTrace();
			throw new FileDAOException("Impossible to parse file object");
		}


	}


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
		if(parent == null)
			parentReal = getRoot();
		else{

			if(parent.getId()== null || parent.getId().isEmpty()){
				throw new FileDAOException("Input file does not have an id. It cannot be recognized.");
			}

			parentReal = getFile(parent);
			if(parentReal == null)
				throw new FileDAOException("Provided parent file does not exist.");
			if(!parentReal.getKind().equals(Kind.FOLDER)){
				throw new FileDAOException("Provided parent file is not a folder.");
			}
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


	@Override
	public Message updateFile(File filter, File toUpdate) throws FileDAOException{
		toUpdate.setModifiedTime(new Date());
		toUpdate.setId(null);
		String jsonFilter = null;
		String jsonToUpdate = null;
		try {
			jsonFilter = mapper.writeValueAsString(filter);
			jsonToUpdate = mapper.writeValueAsString(toUpdate);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Message.DB_MODEL_CREATION_FILED;
		}

		Document searchFilter = Document.parse(jsonFilter);
		Document toUpdateDoc = Document.parse(jsonToUpdate);
		db.updateMany(searchFilter, new Document("$set", toUpdateDoc));
		return Message.SUCCESS;
	}
		
	

}
