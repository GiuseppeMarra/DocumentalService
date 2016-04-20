package org.gm.docdrive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.gm.docdrive.api.interfaces.DocDriveServices;
import org.gm.docdrive.commons.Constants;

public class DocDriveImpl implements DocDriveServices {

	public File createFile(String name, String authToken) throws IOException {

		Path p = Files.createFile(Constants.CURRENT.resolve(name));
		return p.toFile();

	}

	public String changeDirectory(String path, String authToken) throws IOException
	{
		Path pathInput = Paths.get(path).normalize();
		if(pathInput!=null){
			if(pathInput.isAbsolute())
				pathInput=Constants.MAIN_PATH.resolve(pathInput.relativize(Paths.get("/"))).normalize();
			else
				pathInput = Constants.CURRENT.resolve(path).normalize();

			if(Files.exists(pathInput) && Files.isDirectory(pathInput) && pathInput.startsWith(Constants.MAIN_PATH)){
				Constants.CURRENT = pathInput;
				return ResponseStatus.SUCCESS;
			}
		}
		throw new IOException();
	}

	public Response listFiles(String authToken) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		Files.list(Constants.CURRENT)
			 .forEach(n -> res.add(n.toFile().getName()));

		return null;

	}

	@Override
	public File createFolder(String name, String authToken) throws IOException {

		Path p = Files.createDirectories(Constants.CURRENT.resolve(name));
		return p.toFile();
	}

}
