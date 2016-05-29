package org.gm.docdrive.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
	
	
	
	
	public static String getMD5Hash(String input){
		
		
		try {
			byte[] bytesOfMessage = input.getBytes();
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			return new BigInteger(1, md.digest(bytesOfMessage)).toString(16);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
