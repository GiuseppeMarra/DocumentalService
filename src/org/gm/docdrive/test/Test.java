package org.gm.docdrive.test;

import org.gm.docdrive.dao.interfaces.FileDAO;
import org.gm.docdrive.dao.interfaces.FileDAOFactory;
import org.gm.docdrive.model.File;
import org.gm.docdrive.model.File.Kind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) throws JsonProcessingException {

		File f  = new File();
		f.setName("test.xml");
		FileDAO dao = FileDAOFactory.getInstance();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(f));

		
	}

}
