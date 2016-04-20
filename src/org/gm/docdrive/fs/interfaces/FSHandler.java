package org.gm.docdrive.fs.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gm.docdrive.commons.Constants;
import org.gm.docdrive.model.File;
import org.hashids.Hashids;

import redis.clients.jedis.Jedis;

public class FSHandler {
	
	private Jedis jedis = new Jedis("localhost");
		
	private String generateId(){
		Hashids h = new Hashids(jedis.get(Constants.HASH_SALT_KEY));
		return h.encode(jedis.incr(Constants.FILE_ID_KEY));
	}
	
	public void insertFile(File f, InputStream is) throws IOException{
		
		Path p = Paths.get(Constants.MAIN_PATH+f.getName());
		Files.copy(is,p);
	}

}
