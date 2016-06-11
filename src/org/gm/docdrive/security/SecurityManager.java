package org.gm.docdrive.security;

import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOException;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.dao.interfaces.UserDAO;
import org.gm.docdrive.dao.interfaces.UserDAOException;
import org.gm.docdrive.dao.interfaces.UserDAOFactory;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight.Right;
import org.gm.docdrive.security.SecurityManager.SecurityResponse.Status;
import org.gm.docdrive.util.SecurityUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityManager {

	static Map<String, Right> actionRequiredRights = new HashMap<String, Right>(); //TODO Make this configurable
	static{
		actionRequiredRights.put("list", Right.READ);
	}


	public static class SecurityResponse<T>{

		public T value;
		public Status status;

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public void setValue(T value){
			this.value = value;
		}

		public T getValue(){
			return value;
		}

		public static enum Status{

			TOKEN_VALID("The request is allowed. The token is valid and is authorized to that request."), 
			NOT_ALLOWED("The request is not allowed. Token is valid but not authorized to execute this request"), 
			EXPIRED("The token is expired. Renew it."), 
			TOKEN_NOT_VALID("The provided token is not valid."),
			USERNAME_ALREADY_USED("The provided username is already used."), 
			USERNAME_VALID("The provided username is valid."), 
			EMAIL_VALID("The provided email is valid."),
			EMAIL_ALREADY_USED("The provided email is already used."), 
			USER_OK("The provided user is constructed well.");

			private String value;
			private Status(String value){
				this.value = value;
			}

			public String getValue(){
				return value;
			}
		}
	}

	private FileDAO fileDao =FileDAOFactory.getInstance();
	private UserDAO userDao = UserDAOFactory.getInstance();


	public SecurityManager(){}

	public SecurityResponse<Boolean> authorize(String token, String fileId, String action){

		SecurityResponse<Boolean> res = new SecurityResponse<Boolean>();
		if(fileId == null){
			res.setValue(true);
			res.setStatus(SecurityResponse.Status.TOKEN_VALID);
			return res;//TODO Implicit root request
		}
		File f = new File();
		f.setId(fileId);

		TokenManager manager = TokenManagerFactory.getInstance();

		Right r  = manager.isFileAllowed(token, f);
		if(r==null){
			res.setValue(false);
			res.setStatus(Status.TOKEN_NOT_VALID);
			return res;
		}
		if(r==Right.READ && actionRequiredRights.get(action)==Right.WRITE){
			res.setValue(false);
			res.setStatus(Status.NOT_ALLOWED);
			return res;
		}

		res.setValue(true);
		res.setStatus(SecurityResponse.Status.TOKEN_VALID);
		return res;


	}




	public User authenticate(String username, String password) {

		User u = new User();
		u.setUsername(username);
		u.setPassword(SecurityUtil.getMD5Hash(password));

		User res = null;
		try {
			res = userDao.getUser(u);
		} catch (UserDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	public String issueToken(User user) {

		// decode the base64 encoded string
		byte[] decodedKey = Base64.getDecoder().decode(Constants.TOKEN_KEY);
		Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		String token = Jwts.builder().setSubject(user.toString()).signWith(SignatureAlgorithm.HS512, key).compact();


		try {
			List<File> res = fileDao.getAllowedFileForUser(user.getId());
			File root = null;
			for(File f: res){
				if(f.getParent()==null){
					root = f;
					break;
				}
			}
			TokenManager manager = TokenManagerFactory.getInstance();
			manager.registerToken(token, user);
			manager.registerAllowedFile(user, res);
			manager.registerRoot(token, root);
		} catch (FileDAOException e) {
			e.printStackTrace();
		}





		return token;
	}

	public String getRootId(String token) {

		TokenManager manager = TokenManagerFactory.getInstance();
		File root = manager.getRoot(token);
		return root.getId();
	}

	public User tokenIsRegistered(String token) {

		TokenManager manager = TokenManagerFactory.getInstance();
		return manager.tokenIsRegistered(token);
	}

	public void addAllowedFile(File res, User u) {
		TokenManager manager = TokenManagerFactory.getInstance();
		manager.addAllowedFile(res,u);

	}

	public SecurityResponse<Boolean> validateUser(User u) {

		SecurityResponse<Boolean> res = checkForUsername(u.getUsername());
		if(!res.getValue()){
			return res;
		}
		res = checkForEmail(u.getEmailAddress());
		if(!res.getValue()){
			return res;
		}

		res = new SecurityResponse<Boolean>();
		res.setValue(true);
		res.setStatus(Status.USER_OK);
		return res;
	}

	public SecurityResponse<Boolean> checkForUsername(String username) {
		
		Boolean res = userDao.checkForUsername(username);
		SecurityResponse<Boolean> result = new SecurityResponse<Boolean>();
		if(res){
			result.setStatus(Status.USERNAME_VALID);
			result.setValue(true);
		}
		else{
			result.setStatus(Status.USERNAME_ALREADY_USED);
			result.setValue(false);
		}
		return result;
		
	}

	public void register(User u) {

		userDao.insertUser(u);
	}

	public SecurityResponse<Boolean> checkForEmail(String email) {
		// TODO Auto-generated method stub
		Boolean res = userDao.checkForEmail(email);
		SecurityResponse<Boolean> result = new SecurityResponse<Boolean>();
		if(res){
			result.setStatus(Status.EMAIL_VALID);
			result.setValue(true);
		}
		else{
			result.setStatus(Status.EMAIL_ALREADY_USED);
			result.setValue(false);
		}
		return result;
	}

}
