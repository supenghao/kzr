package com.sunnada.kernel.exception;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException{
	protected Throwable cause;
	protected String code;
	
	public BaseException(){}

	public BaseException(String code){	  
	  super(code);
	  this.code = code;
	}
	public BaseException(String code, Throwable cause){		
	  super(code);
	  this.cause = cause;
	  this.code = code;
	}
	public BaseException(Throwable cause){
	    this.cause = cause;
	}
	public String toString() {
//		String message = getMessage();
//		if (ExceptionConf.getExceptionText(message)==null){
//			return "BaseException: " + getMessage();
//		}else{
//	        return "BaseException: " + ExceptionConf.getExceptionText(message);
//		}
		return "BaseException: " + getMessage();
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
