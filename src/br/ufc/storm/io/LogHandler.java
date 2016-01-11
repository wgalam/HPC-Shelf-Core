package br.ufc.storm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
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
			SimpleDateFormat format = new SimpleDateFormat("D-M-Y-_HH:mm:ss");
			if(Boolean.parseBoolean(PropertiesHandler.getProperty("core.database.testing"))){
				path = PropertiesHandler.getProperty("core.log.path");
			}else{
				path = PropertiesHandler.getProperty("core.log.server.path");
			}
			path+="/Core_log-"+ format.format(Calendar.getInstance().getTime())+".log";
			Files.createDirectories(Paths.get(path).getParent());
			fh = new java.util.logging.FileHandler(path, false);
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
			MyFormatter formatter = new MyFormatter();  
			
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
			LogHandler.logger.info("CREATING NEW LOG");
		}
		return LogHandler.logger;
	}

	
	public static void close() {
		if(fh != null){
			fh.close();
		}
		fh=null;
		logger = null;
		log = null;
		
	}
	
}
class MyFormatter extends Formatter {
    //
    // Create a DateFormat to format the logger timestamp.
    //
    private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");
//    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append(" (").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("())");
        builder.append("\n");
        return builder.toString();
    }
 
    public String getHead(Handler h) {
        return super.getHead(h);
    }
 
    public String getTail(Handler h) {
        return super.getTail(h);
    }

	
}