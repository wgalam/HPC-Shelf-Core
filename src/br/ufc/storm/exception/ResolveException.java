package br.ufc.storm.exception;

import java.lang.Exception;


public class ResolveException extends ShelfException{
	private static final long serialVersionUID = 1L;

	public ResolveException() {
		super();
	}
	public ResolveException(String message) { 
		super("ResolveException: "+message); 
	}
	public ResolveException(String message, Throwable cause) { 
		super("ResolveException: "+message, cause); 
	}
	public ResolveException(Throwable cause) { 
		super("ResolveException: "+cause); 
	}
}
