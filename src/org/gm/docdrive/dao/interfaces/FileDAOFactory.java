package org.gm.docdrive.dao.interfaces;

import org.gm.docdrive.dao.impl.mongo.MongoFileDAO;

public class FileDAOFactory {
	
	
	public static FileDAO getInstance(){
		
		
		return new MongoFileDAO();
	}
	


}
