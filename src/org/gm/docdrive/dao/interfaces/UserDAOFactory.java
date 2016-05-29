package org.gm.docdrive.dao.interfaces;

import org.gm.docdrive.dao.impl.mongo.MongoUserDAO;

public class UserDAOFactory {

	public static UserDAO getInstance() {
		// TODO Auto-generated method stub
		return new MongoUserDAO();
	}

}
