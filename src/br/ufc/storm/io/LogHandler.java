package br.ufc.storm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.ufc.storm.properties.PropertiesHandler;

public class LogHandler {
	static Logger logger = getInstance(); 
	
	private static Logger getInstance() {
		java.util.logging.FileHandler fh;  
		logger = Logger.getLogger("MyLog");
		try {  
			// This block configure the logger with handler and formatter  
			Path pathToFile = Paths.get(PropertiesHandler.getProperty("core.log.path")+"/core.log");
			Files.createDirectories(pathToFile.getParent());
			fh = new java.util.logging.FileHandler(PropertiesHandler.getProperty("core.log.path")+"/core.log");
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			
			fh.setFormatter(formatter);  
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		return logger;
	}
	
	public static Logger getLog(){
		return logger;
	}
	
	public static void doLog(String msg){
		logger.warning(msg);
	}
}
