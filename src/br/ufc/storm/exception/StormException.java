package br.ufc.storm.exception;
import java.lang.Exception;

public class StormException extends Exception{
	private static final long serialVersionUID = 1L;

	public StormException() {
		super();
	}
	public StormException(String message) { 
		super("StormException: "+message); 
	}
	public StormException(String message, Throwable cause) { 
		super("StormException: "+message, cause); 
	}
	public StormException(Throwable cause) { 
		super("StormException: "+cause); 
	}
}
