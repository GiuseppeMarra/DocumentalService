package org.gm.docdrive;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOException;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;

public class DocDrive {

	private FileDAO dao = FileDAOFactory.getInstance();



	public File insertFile(File f, InputStream is) throws IOException{

		//DB management
		f.setKind(Kind.FILE);
		f.setId(UUID.randomUUID().toString());
		dao.insertFile(f);

		//FS management
		Path p = Constants.MAIN_PATH.resolve(f.getId());
		Files.copy(is,p);

		try {
			return dao.getFile(f);
		} catch (FileDAOException e) {
			e.printStackTrace();
			return f;
		}

	}

	public File getFile(File f){
		
		try {
			return dao.getFile(f);
		} catch (FileDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<File> list(File parent){

		List<File> res = null;
		try {
			res = dao.list(parent);
		} catch (FileDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;



	}


	public File insertFolder(File f) throws FileDAOException {
		//DB management
				f.setKind(Kind.FOLDER);
				f.setId(UUID.randomUUID().toString());
				dao.insertFile(f);

				

				try {
					return dao.getFile(f);
				} catch (FileDAOException e) {
					e.printStackTrace();
					return f;
				}

		
	}

}
