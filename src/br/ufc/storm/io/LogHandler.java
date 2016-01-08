package br.ufc.storm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.ufc.storm.properties.PropertiesHandler;

public class LogHandler {
	public static LogHandler log;
	private static Logger logger = null; 
	private static java.util.logging.FileHandler fh = null;  
	
	public LogHandler(){
		
		logger = Logger.getLogger("MyLog");
		String path;


		try {  


			// This block configure the logger with handler and formatter  
			SimpleDateFormat format = new SimpleDateFormat("M-d_HH:mm:ss");
			if(Boolean.parseBoolean(PropertiesHandler.getProperty("core.database.testing"))){
				path = PropertiesHandler.getProperty("core.log.path")+"/core"+ format.format(Calendar.getInstance().getTime())+".log";
			}else{
				path = "/usr/share/Tomcat7/webapps/axis2/WEB-INF/services/CoreServices/log/core"+ format.format(Calendar.getInstance().getTime())+".log";
			}
			Files.createDirectories(Paths.get(path).getParent());
			fh = new java.util.logging.FileHandler(path, false);
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}

	public static Logger getLogger(){
		if(log == null){
			log = new LogHandler();
			log.logger.warning("CREATING NEW LOG");
		}
		return log.logger;
	}

	public static void close() {
		if(fh != null){
			fh.close();
		}
	}
}
