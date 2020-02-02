package br.ufc.storm.exception;

public class ShelfRuntimeException extends ShelfException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShelfRuntimeException() {
		super();
	}

	public ShelfRuntimeException(String message) {
		super("ShelfRuntimeException: "+message);
		// TODO Auto-generated constructor stub
	}

	public ShelfRuntimeException(String message, Throwable cause) {
		super("ShelfRuntimeException: "+message, cause);
		// TODO Auto-generated constructor stub
	}

	public ShelfRuntimeException(Throwable cause) {
		super("ShelfRuntimeException: "+cause);
		// TODO Auto-generated constructor stub
	}

}
