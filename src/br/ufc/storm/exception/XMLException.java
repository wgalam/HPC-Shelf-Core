package br.ufc.storm.exception;
import java.lang.Exception;

import br.ufc.storm.io.LogHandler;

public class XMLException extends ShelfException{
	private static final long serialVersionUID = 1L;
	
	public XMLException() {
		super();
	}
	public XMLException(String message) { 
		super("XMLException: "+message); 
		LogHandler.doLog("XMLException: "+message);
	}
	public XMLException(String message, Throwable cause) { 
		super("XMLException: "+message, cause); 
		LogHandler.doLog("XMLException: "+message+" | "+cause);
	}
	public XMLException(Throwable cause) { 
		super("XMLException: "+cause); 
		LogHandler.doLog("XMLException: "+cause);
	}
	
	
}