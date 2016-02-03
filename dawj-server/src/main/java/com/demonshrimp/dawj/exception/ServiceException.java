package com.demonshrimp.dawj.exception;

public class ServiceException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public ServiceException(Throwable t){
		super(t);
	}
	public ServiceException(String exceptionMsg){
		super(exceptionMsg);
	}
	public ServiceException(String exceptionMsg,Throwable t){
		super(exceptionMsg,t);
	}
}
