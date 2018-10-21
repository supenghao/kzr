package com.sunnada.kernel.exception;

@SuppressWarnings("serial")
public class JsonException extends RuntimeException{

	protected Throwable cause;
	protected String code;
	
	public JsonException(){}

	public JsonException(String code){	  
	  super(code);
	  this.code = code;
	}
	public JsonException(String code, Throwable cause){		
	  super(code);
	  this.cause = cause;
	  this.code = code;
	}
	public JsonException(Throwable cause){
	    this.cause = cause;
	}
	public String toString() {
//		String message = getMessage();
//		if (ExceptionConf.getExceptionText(message)==null){
//			return "BaseException: " + getMessage();
//		}else{
//	        return "BaseException: " + ExceptionConf.getExceptionText(message);
//		}
		return "jsonException: " + getMessage();
	}
	public Throwable getCause(){
	    return this.cause;
	}
	public String getMessage() {
	    if (super.getMessage() == null) {
	      return "原因:" + this.cause.getMessage();
	    }
	    return super.getMessage();
	}
}
