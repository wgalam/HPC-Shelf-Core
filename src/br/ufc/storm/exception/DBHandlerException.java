package br.ufc.storm.exception;

public class DBHandlerException extends Exception{
	private static final long serialVersionUID = 1L;

	public DBHandlerException() {
		super();
	}
	public DBHandlerException(String message) { 
		super("DBHandlerException: "+message); 
	}
	public DBHandlerException(String message, Throwable cause) { 
		super("DBHandlerException: "+message, cause); 
	}
	public DBHandlerException(Throwable cause) { 
		super("DBHandlerException: "+cause); 
	}
}
