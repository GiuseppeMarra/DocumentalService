package org.gm.docdrive.util;

import java.util.ArrayList;
import java.util.List;

import org.gm.docdrive.dao.interfaces.UserDAO;
import org.gm.docdrive.dao.interfaces.UserDAOException;
import org.gm.docdrive.dao.interfaces.UserDAOFactory;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight;
import org.gm.docdrive.ws.model.FileVW;
import org.gm.docdrive.ws.model.UserVW;

public class ModelViewConverter {
	
	
	private static UserDAO userDao = UserDAOFactory.getInstance();
	
	
	public static  FileVW fromFile(File f){

	FileVW vw = new FileVW();
	vw.setCreatedTime(f.getCreatedTime());
	vw.setDescription(f.getDescription());
	vw.setFileExtension(f.getFileExtension());
	vw.setFullFileExtension(f.getFullFileExtension());
	vw.setId(f.getId());
	vw.setKind(f.getKind());
	vw.setMd5Checksum(f.getMd5Checksum());
	vw.setMimeType(f.getMimeType());
	vw.setModifiedTime(f.getModifiedTime());
	vw.setName(f.getName());
	try {
		vw.setOwner(fromUser(userDao.getUser(new User(f.getOwner().getIdUser()))));
	} catch (UserDAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	vw.setParent(f.getParent());
	vw.setProperties(f.getProperties());
	try {
		List<String> usersId = new ArrayList<String>();
		List<UserRight> sharedWith = f.getSharedWith();
		if(sharedWith!=null)
			sharedWith.forEach(n -> usersId.add(n.getIdUser()));
		List<User> sharedWithUsers = userDao.getUsersFromIds(usersId);
		if(sharedWithUsers!=null&&!sharedWithUsers.isEmpty())
				vw.setSharedWith(fromUserList(sharedWithUsers));
	} catch (UserDAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	vw.setSize(f.getSize());
	return vw;
	
	
	}

	private static List<UserVW> fromUserList(List<User> list) {
		
		
		List<UserVW> res = new ArrayList<UserVW>();
		for(User u : list) res.add(fromUser(u));
		return res;
	}

	private static UserVW fromUser(User u) {

		UserVW vw = new UserVW();
		vw.setDisplayName(u.getDisplayName());
		vw.setEmailAddress(u.getEmailAddress());
		vw.setId(u.getId());
		vw.setPictureUrl(u.getPictureUrl());
		return vw; 
		
		
	}

	public static List<FileVW> fromFileList(List<File> list){

		List<FileVW> res = new ArrayList<FileVW>();
		for(File f : list) res.add(fromFile(f));
		return res;
		
	}
}
