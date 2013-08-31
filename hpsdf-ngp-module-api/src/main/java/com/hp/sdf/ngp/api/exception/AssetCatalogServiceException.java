package com.hp.sdf.ngp.api.exception;

/**
 * This exception is thrown for the asset catalog service interface.
 * 
 */
public class AssetCatalogServiceException extends Exception {

	private static final long serialVersionUID = -1;

	public AssetCatalogServiceException(String msg) {
		super(msg);
	}
	
	public AssetCatalogServiceException(Exception e) {
		super(e);
	}

	public AssetCatalogServiceException(String msg, Exception e) {
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
