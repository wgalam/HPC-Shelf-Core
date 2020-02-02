package br.ufc.storm.exception;

import br.ufc.storm.io.LogHandler;

public class ShelfException extends Exception{
	private static final long serialVersionUID = 1L;

	public ShelfException() {
		super();
	}
	public ShelfException(String message) { 
		super(message); 
		LogHandler.getLogger().severe("ShelfException: "+message);
	}
	public ShelfException(String message, Throwable cause) { 
		super(message, cause); 
		String str = "";
		for(StackTraceElement element : cause.getStackTrace()){
			str+="\tat "+element.getClassName()+"."+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")\n";
		}
		LogHandler.getLogger().severe("ShelfException: "+message+"\n"+str);
	}
	public ShelfException(Throwable cause) { 
		super(cause); 
		String str = "";
		for(StackTraceElement element : cause.getStackTrace()){
			str+="at "+element.getClassName()+"."+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")\n";
		}
		LogHandler.getLogger().severe("ShelfException: "+cause.getMessage()+"\n"+str);
	}
}
