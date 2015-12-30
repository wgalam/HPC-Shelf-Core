package br.ufc.storm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
			SimpleDateFormat format = new SimpleDateFormat("M-d_HH:mm:ss");
			Path pathToFile = Paths.get(PropertiesHandler.getProperty("core.log.path")+"/core");
			Files.createDirectories(pathToFile.getParent());
			fh = new java.util.logging.FileHandler(PropertiesHandler.getProperty("core.log.path")+"/core"+ format.format(Calendar.getInstance().getTime())+".log", false);
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
	
	public static void doWarning(String msg){
		logger.warning(msg);
	}
}
