package org.gm.docdrive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import org.gm.docdrive.security.SecurityManager;
import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOException;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight;
import org.gm.docdrive.model.UserRight.Right;

public class DocDrive {

	private FileDAO fileDao = FileDAOFactory.getInstance();


	public File getRoot(User u) throws IllegalArgumentException{
		if(u.getId()==null || u.getId()==""){
			throw new IllegalArgumentException("The provided user needs an id.");
		}

		try {
			return fileDao.getRoot(u);
		} catch (FileDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public File insertFile(File f, InputStream is, User u) throws IOException{

		//DB management
		f.setKind(Kind.FILE);
		f.setId(UUID.randomUUID().toString());
		UserRight ur = new UserRight();
		ur.setIdUser(u.getId());
		ur.setRight(Right.WRITE);
		f.setOwner(ur);

		//FS management
		Path p = Constants.MAIN_PATH.resolve(f.getId());
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			try (InputStream stream = is){
				DigestInputStream dis = new DigestInputStream(stream, md);
				Files.copy(dis,p);
			}
			f.setSize(p.toFile().length());
			f.setMd5Checksum(new BigInteger(1, md.digest()).toString(16));
			System.out.println(f.getMd5Checksum());
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File res = null;
		try {
			res = fileDao.insertFile(f);
		} catch (FileDAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SecurityManager sm = new SecurityManager();
		sm.addAllowedFile(res,u);
		return res;


	}

	public File getFile(File f){

		try {
			return fileDao.getFile(f);
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
			res = fileDao.list(parent);
		} catch (FileDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;



	}

//	public Message addProperty(File target, String key, String value){
//
//		File f = getFile(target); //TODO check more then one or check if the id is set
//		Map<String, String> properties = f.getProperties();
//		if(properties == null){
//			properties = new HashMap<String, String>();
//			f.setProperties(properties);
//		}
//		properties.put(key, value);
//		updateFile(new File(f.getId()),f);
//		return Message.SUCCESS;
//
//	}
//
//	public Message addProperties(File target, Map<String, String> props){
//
//		File f = getFile(target);
//		Map<String, String> properties = f.getProperties();
//		if(properties == null){
//			properties = new HashMap<String, String>();
//		}
//		properties.putAll(props);
//		updateFile(new File(f.getId()),f);
//		return Message.SUCCESS;
//
//	}


//	public void updateFile(File filter, File toUpdate) {
//
//		try {
//			fileDao.updateFile(filter, toUpdate);
//		} catch (FileDAOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//	}

	public File insertFolder(File parent, File newFolder, User u) {
		//DB management
		
		if(parent==null||parent.getId()==null||parent.getId()=="")
			return null;
		if(newFolder.getName()==null || newFolder.getName()=="")
			newFolder.setName("newFolder");
		newFolder.setKind(Kind.FOLDER);
		newFolder.setId(UUID.randomUUID().toString());
		newFolder.setParent(parent.getId());
		File res = null;
		try {
			parent = fileDao.getFile(parent);
			newFolder.setOwner(parent.getOwner());
			res = fileDao.insertFile(newFolder);
			
		} catch (FileDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecurityManager sm = new SecurityManager();
		sm.addAllowedFile(res,u);
		return res;


	}

	public File delete(File f){
		try{
			return fileDao.delete(f);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
