package org.gm.docdrive.dao.interfaces;

import java.util.List;

import org.gm.docdrive.model.File;
import org.gm.docdrive.model.User;

public interface FileDAO {
	
	
	public File insertFile(File f) throws FileDAOException;

	public File getRoot(User u) throws FileDAOException;

	public File getFile(File f) throws FileDAOException;
	
	public List<File> list(File parent) throws FileDAOException;


//	public Message updateFile(File filter, File toUpdate) throws FileDAOException;

	public File delete(File f) throws FileDAOException;

	public List<File> getAllowedFileForUser(String id) throws FileDAOException;

}
