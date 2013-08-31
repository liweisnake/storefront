package com.hp.sdf.ngp.api.exception;

/**
 * This exception is thrown for the asset catalog service interface.
 * 
 */
public class SubscriberCatalogServiceException extends Exception {

	private static final long serialVersionUID = -1;
	
	public SubscriberCatalogServiceException(String msg) {
		super(msg);
	}
	
	public SubscriberCatalogServiceException(Exception e) {
		super(e);
	}

	public SubscriberCatalogServiceException(String msg, Exception e) {
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
