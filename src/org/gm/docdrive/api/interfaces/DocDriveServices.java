package org.gm.docdrive.api.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface DocDriveServices {
	
	
	public File createFile(String name, String authToken) throws IOException;
	
	public File createFolder(String name, String authToken) throws IOException;
	
	public String changeDirectory(String path, String authToken) throws IOException;

	public List<String> listFiles(String authToken) throws IOException;
	
	
	
	

}