package org.gm.docdrive.security;

import java.util.List;

import org.gm.docdrive.model.File;
import org.gm.docdrive.model.User;
import org.gm.docdrive.model.UserRight.Right;

public interface TokenManager {
	
	public boolean registerToken(String token, User u); //if there is a token then renew
	public boolean registerAllowedFile(User u, List<File> files);
	public boolean registerRoot(String token, File root);
	public Right isFileAllowed(String token, File f);
	public File getRoot(String token);
	public User tokenIsRegistered(String token);
	public void addAllowedFile(File res, User u);

}
