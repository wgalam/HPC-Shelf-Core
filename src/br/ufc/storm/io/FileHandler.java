package br.ufc.storm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import br.ufc.storm.exception.DBHandlerException;
import br.ufc.storm.properties.PropertiesHandler;

public class FileHandler {

	/**
	 * This method is used to receive binary files and save into storm library dir
	 * @param data
	 * @param fileName
	 * @return
	 */
	public static boolean addFile(byte[] data,String fileName) {
		try {
			Path pathToFile = Paths.get(PropertiesHandler.getProperty("core.library.path")+"/"+fileName);
			Files.createDirectories(pathToFile.getParent());
			Files.createFile(pathToFile);
			Files.write(pathToFile, data, StandardOpenOption.WRITE);
			System.out.println("File saved in this path: "+pathToFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * This method is responsible for receive a byte array and save them content to hard disk
	 * @param path
	 * @param content
	 * @return
	 */
	public static boolean toHardDisk(String path, byte [] content){
		Path pathToFile = Paths.get(path);
		try {
			Files.createDirectories(pathToFile.getParent());
			Files.createFile(pathToFile);
			Files.write(pathToFile, content, StandardOpenOption.WRITE);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static byte[] readFile(String pathname) throws IOException {

		Path path = Paths.get(pathname);
		byte[] data = Files.readAllBytes(path);
		return data;
	}


	/**
	 * This method read a file from hard disk and return its content as a string
	 * @param pathname Path to file
	 * @return File content as string
	 * @throws IOException
	 */


	public static String readFileAsString(String pathname) throws IOException {

		Path path = Paths.get(pathname);
		byte[] data = Files.readAllBytes(path);
		return new String(data);
	}

	public static byte[] getUnitFile(int ufid) throws DBHandlerException{
		byte[] data = null;
		try {
			data = Files.readAllBytes(Paths.get(br.ufc.storm.sql.ConcreteUnitHandler.getUnitFilePath(ufid)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
