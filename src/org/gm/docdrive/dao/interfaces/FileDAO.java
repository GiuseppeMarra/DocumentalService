package org.gm.docdrive.dao.interfaces;

import java.util.List;

import org.gm.docdrive.model.File;

public interface FileDAO {
	
	
	public Message insertFile(File f);

	public File getRoot() throws FileDAOException;

	public File getFile(File f) throws FileDAOException;
	
	public List<File> list(File parent) throws FileDAOException;

}
