package org.gm.docdrive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOException;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.dao.interfaces.Message;
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

	public InputStream dowloadFileAsStream(File f) throws IllegalArgumentException{

		File real = getFile(f);
		if(real.getKind().equals(Kind.FOLDER)){
			throw new IllegalArgumentException("Cannot download folder as stream");
		}

		Path p = Constants.MAIN_PATH.resolve(real.getId());
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(p.toFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;

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

	public Message addProperty(File target, String key, String value){

		File f = getFile(target); //TODO check more then one or check if the id is set
		Map<String, String> properties = f.getProperties();
		if(properties == null){
			properties = new HashMap<String, String>();
			f.setProperties(properties);
		}
		properties.put(key, value);
		updateFile(new File(f.getId()),f);
		return Message.SUCCESS;

	}

	public Message addProperties(File target, Map<String, String> props){

		File f = getFile(target);
		Map<String, String> properties = f.getProperties();
		if(properties == null){
			properties = new HashMap<String, String>();
		}
		properties.putAll(props);
		updateFile(new File(f.getId()),f);
		return Message.SUCCESS;

	}


	public void updateFile(File filter, File toUpdate) {

		try {
			dao.updateFile(filter, toUpdate);
		} catch (FileDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public File insertFolder(File f) {
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

	public File delete(File f){
		try{
			return dao.delete(f);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
