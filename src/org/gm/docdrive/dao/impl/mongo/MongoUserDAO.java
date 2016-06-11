package org.gm.docdrive.dao.impl.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.gm.docdrive.dao.interfaces.UserDAO;
import org.gm.docdrive.dao.interfaces.UserDAOException;
import org.gm.docdrive.model.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class MongoUserDAO implements UserDAO{

	private ObjectMapper mapper;
	private MongoClient mongoClient;
	private MongoCollection<Document> db;

	public MongoUserDAO(){
		mapper = new ObjectMapper();
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(MongoConstants.DB_NAME).getCollection(MongoConstants.USERS_COLLECTIONS);

	}

	public User getUser(User user) throws UserDAOException{

		String jsonUser=null;
		try {
			jsonUser = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			Document filter = new Document().parse(jsonUser);
			Long count  =db.count(filter);
			if(count >1)
				throw new UserDAOException("More then one user");
			else{
				Document d = db.find(filter).first();
				if(d!=null)
					return mapper.readValue(d.toJson(), User.class);
				else{
					return null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<User> getUsersFromIds(List<String> ids) throws UserDAOException {


		List<User> users = new ArrayList<User>();
		for(String id : ids){
			try {
				users.add(getUser(new User(id)));
			} catch (UserDAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
		return users;
	}

	@Override
	public User insertUser(User u) {
		String jsonString ="{}";
		try {
			jsonString = mapper.writeValueAsString(u);


		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		Document mongoFile = Document.parse(jsonString);
		db.insertOne(mongoFile);
		try {
			return getUser(u);
		} catch (UserDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public boolean checkForUsername(String username) {

		String filter = "{ \"username\": \""+username+"\"}";
		FindIterable<Document> res = db.find(Document.parse(filter));
		if(res.iterator().hasNext())
			return false;
		return true;
	}

	@Override
	public boolean checkForEmail(String email) {
		
		String filter = "{ \"emailAddress\": \""+email+"\"}";
		FindIterable<Document> res = db.find(Document.parse(filter));
		if(res.iterator().hasNext())
			return false;
		return true;
	}

}
