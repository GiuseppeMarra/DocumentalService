package org.gm.docdrive.commons;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

import org.gm.docdrive.DocDriveImpl;

public class TestingMain {

	public static void main(String[] args) {
		DocDriveImpl drive = new DocDriveImpl();


		Scanner in = new Scanner(System.in);
		while(true)
			if(in.hasNext()){
				String line = in.nextLine();
				String[] splits = line.split(" ");
				switch(splits[0]){

				case "touch": 
					try {
					
						if(splits.length==2){
							drive.createFile(splits[1],"");
						}
						else
							System.out.println("COMANDO NON CORRETTO");
			
					} catch (IOException e) {
						if(e instanceof FileAlreadyExistsException)
							System.err.println("FILE ESISTENTE");
					}
					break;
				
				case "ls": 
					try {
						drive.listFiles("").stream().forEach(n->System.out.println(n));
					} catch (IOException e) {
						if(e instanceof FileAlreadyExistsException)
							System.err.println("FILE ESISTENTE");
					}
					break;
					
				case "cd": 

						if(splits.length==2){
							try {
								drive.changeDirectory(splits[1],"");
							} catch (IOException e) {
								System.err.println("PERCORSO NON VALIDO");

							}
						}
						break;
						
				case "mkdirs":
					try {
						
						if(splits.length==2){
							drive.createFolder(splits[1],"");
						}
						else
							System.out.println("COMANDO NON CORRETTO");
			
					} catch (IOException e) {
						if(e instanceof FileAlreadyExistsException)
							System.err.println("CARTELLA ESISTENTE");
					}
					break;
					
						
				default: 
					System.err.println("Comando \""+splits[0]+"\" non riconosciuto");
					break;
	
				}
				

			}


	}
}
