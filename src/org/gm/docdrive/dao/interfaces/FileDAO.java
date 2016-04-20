package org.gm.docdrive.dao.interfaces;

import org.gm.docdrive.model.File;

public interface FileDAO {
	
	
	public Message insertFile(File f);

	public File getRoot();

}
