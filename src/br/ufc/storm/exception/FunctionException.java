package br.ufc.storm.exception;

public class FunctionException extends ShelfException{
	private static final long serialVersionUID = 1L;

	public FunctionException() {
		super();
	}
	public FunctionException(String message) { 
		super("FunctionException: "+message); 
	}
	public FunctionException(String message, Throwable cause) { 
		super("FunctionException: "+message, cause); 
	}
	public FunctionException(Throwable cause) { 
		super("FunctionException: "+cause); 
	}
}
