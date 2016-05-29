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
import org.gm.docdrive.util.SecurityUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityManager {

	static Map<String, Right> actionRequiredRights = new HashMap<String, Right>(); //TODO Make this configurable
	static{
		actionRequiredRights.put("list", Right.READ);
	}


	public static enum Response{
		ALLOWED, NOT_ALLOWED, EXPIRED, TOKEN_NOT_RECOGNIZED
	}

	private FileDAO fileDao =FileDAOFactory.getInstance();
	private UserDAO userDao = UserDAOFactory.getInstance();


	public SecurityManager(){}

	public Response authorize(String token, String fileId, String action){

		if(fileId == null){
			return Response.ALLOWED; //TODO Implicit root request
		}
		File f = new File();
		f.setId(fileId);

		TokenManager manager = TokenManagerFactory.getInstance();

		Right r  = manager.isFileAllowed(token, f);
		if(r==null){
			return Response.TOKEN_NOT_RECOGNIZED;
		}
		if(r==Right.READ && actionRequiredRights.get(action)==Right.WRITE)
			return Response.NOT_ALLOWED;

		return Response.ALLOWED;


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

}
