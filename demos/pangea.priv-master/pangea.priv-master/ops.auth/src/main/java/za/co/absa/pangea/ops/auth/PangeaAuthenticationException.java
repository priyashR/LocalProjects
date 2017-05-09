/*********************************************
 * Copyright 2016 Absa ©
 * 05 Jul 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.auth;

public class PangeaAuthenticationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6080188558646333708L;

	/**
	 * Instantiates a new pangea authentication exception.
	 */
	public PangeaAuthenticationException() {
		super();
	}

	public PangeaAuthenticationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PangeaAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PangeaAuthenticationException(String message) {
		super(message);
	}

	public PangeaAuthenticationException(Throwable cause) {
		super(cause);
	}

	
	
}
