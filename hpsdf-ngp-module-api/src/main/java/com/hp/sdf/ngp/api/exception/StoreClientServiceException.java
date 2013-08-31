package com.hp.sdf.ngp.api.exception;

/**
 * This exception is thrown for the store client interface.
 * 
 */
public class StoreClientServiceException extends Exception {

	private static final long serialVersionUID = 3569220404660350310L;
	
	public StoreClientServiceException(String msg) {
		super(msg);
	}
	
	public StoreClientServiceException(Exception e) {
		super(e);
	}

	public StoreClientServiceException(String msg, Exception e) {
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
