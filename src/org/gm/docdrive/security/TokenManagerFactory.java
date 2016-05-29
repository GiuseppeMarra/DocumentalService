package org.gm.docdrive.security;

public class TokenManagerFactory {
	
	
	private static TokenManager manager = new MapTokenManager();
	
	public static TokenManager getInstance(){
		
		return manager;
	}
	
	
	

}
