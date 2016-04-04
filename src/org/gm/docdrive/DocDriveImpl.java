package org.gm.docdrive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocDriveImpl implements DocDriveServices {

	public File createFile(String name) throws IOException {

		Path p = Files.createFile(Constants.CURRENT.resolve(name));
		return p.toFile();

	}

	public String changeDirectory(String path) throws IOException
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

	public List<String> listFiles() throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		Files.list(Constants.CURRENT)
			 .forEach(n -> res.add(n.toFile().getName()));

		return res;

	}

	@Override
	public File createFolder(String name) throws IOException {

		Path p = Files.createDirectories(Constants.CURRENT.resolve(name));
		return p.toFile();
	}

}
