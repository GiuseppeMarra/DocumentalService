package org.gm.docdrive;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface DocDriveServices {
	
	
	public File createFile(String name) throws IOException;
	
	public File createFolder(String name) throws IOException;

	
	public String changeDirectory(String path) throws IOException;

	public List<String> listFiles() throws IOException;
	
	
	
	

}
