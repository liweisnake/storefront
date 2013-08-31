package com.hp.sdf.ngp.api.exception;

/**
 * This exception is thrown for the store client interface.
 * 
 */
public class UserCommentsCensorFailException extends StoreClientServiceException {

	private static final long serialVersionUID = -6220833175026688382L;

	public UserCommentsCensorFailException(String msg) {
		super(msg);
	}
	
	public UserCommentsCensorFailException(Exception e) {
		super(e);
	}

	public UserCommentsCensorFailException(String msg, Exception e) {
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
