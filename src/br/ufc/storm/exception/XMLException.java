package br.ufc.storm.exception;

public class XMLException extends ShelfException{
	private static final long serialVersionUID = 1L;
	
	public XMLException() {
		super();
	}
	public XMLException(String message) { 
		super("XMLException: "+message); 
	}
	public XMLException(String message, Throwable cause) { 
		super("XMLException: "+message, cause); 
	}
	public XMLException(Throwable cause) { 
		super("XMLException: "+cause); 
	}
	
	
}