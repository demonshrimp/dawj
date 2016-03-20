package com.demonshrimp.dawj.exception;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public ServiceException(Throwable t) {
		super(t);
	}

	public ServiceException(String exceptionMsg) {
		super(exceptionMsg);
	}

	public ServiceException(String exceptionMsg, Throwable t) {
		super(exceptionMsg, t);
	}

	public ServiceException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ServiceException(String message, Throwable cause, String errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
