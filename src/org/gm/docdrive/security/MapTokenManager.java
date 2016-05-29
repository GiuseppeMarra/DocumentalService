package org.gm.docdrive.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gm.docdrive.model.File;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight;
import org.gm.docdrive.model.UserRight.Right;

public class MapTokenManager implements TokenManager {


	private Map<String, User> tokenUser = new HashMap<String, User>();
	private Map<User, List<File>> userFiles = new HashMap<User, List<File>>();
	private Map<String, File> userRoots = new HashMap<String, File>();


	public boolean registerToken(String token, User u) {

		tokenUser.put(token, u);
		return true;
	}
	
	public boolean registerRoot(String token, File root){
		userRoots.put(token, root);
		return true;
	}

	@Override
	public boolean registerAllowedFile(User u, List<File> files) {
		userFiles.put(u, files);
		return true;
	}

	@Override
	public Right isFileAllowed(String token, File f) {

		User u = tokenUser.get(token);
		for (File curr : userFiles.get(u)){
			if(curr.equals(f)){
				if(curr.getOwner().getIdUser().equals(u.getId()))
					return Right.WRITE;
				else{
					for(UserRight r : curr.getSharedWith()){
						if(r.getIdUser().equals(u.getId()))
							return r.getRight();
					}
				}
				
			}
		}
			

		return null;
	}

	@Override
	public File getRoot(String token) {

		return userRoots.get(token);
		
	}

	@Override
	public User tokenIsRegistered(String token) {

		return tokenUser.get(token);
	}

	@Override
	public void addAllowedFile(File res, User u) {

		userFiles.get(u).add(res);
	}

}
