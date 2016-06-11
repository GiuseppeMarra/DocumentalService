package org.gm.docdrive.dao.interfaces;

import java.util.List;

import org.gm.docdrive.model.User;

public interface UserDAO {

	public User getUser(User owner) throws UserDAOException;

	public List<User> getUsersFromIds(List<String> sharedWith) throws UserDAOException;

	public User insertUser(User u);

	public boolean checkForUsername(String username);

	public boolean checkForEmail(String email);

}
