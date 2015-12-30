package br.ufc.storm.exception;

import br.ufc.storm.io.LogHandler;

public class ShelfException extends Exception{
	private static final long serialVersionUID = 1L;

	public ShelfException() {
		super();
	}
	public ShelfException(String message) { 
		super(message); 
		LogHandler.getLog().warning("ShelfException: "+message);
	}
	public ShelfException(String message, Throwable cause) { 
		super(message, cause); 
		LogHandler.getLog().warning("ShelfException: "+message+" | "+cause);
	}
	public ShelfException(Throwable cause) { 
		super(cause); 
		LogHandler.getLog().warning("ShelfException: "+cause);
	}
}
