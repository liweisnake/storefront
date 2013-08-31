package com.hp.sdf.ngp.custom.sbm.api.storeclient.exception;

/**
 * This exception is thrown for the handset interface.
 * 
 */
public class HandsetServiceException extends Exception {

	private static final long serialVersionUID = 3569220404660350310L;

	public HandsetServiceException(String msg) {
		super(msg);
	}

	public HandsetServiceException(Exception e) {
		super(e);
	}

	public HandsetServiceException(String msg, Exception e) {
		super(msg, e);
	}

	/**
	 * Return the detailed explanation for the failure of the store operation
	 */
	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
