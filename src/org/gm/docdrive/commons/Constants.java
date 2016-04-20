package org.gm.docdrive.commons;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
	
	public static final String HASH_SALT_KEY = "hash:salt:key";
	public static final String FILE_ID_KEY = "file.ids";
	private static String MAIN_PATH0 ="/Users/giuseppe/Documents/docdrive_repo/";
	public static Path MAIN_PATH =Paths.get(MAIN_PATH0);
	public static Path CURRENT = Paths.get(MAIN_PATH0);

}
