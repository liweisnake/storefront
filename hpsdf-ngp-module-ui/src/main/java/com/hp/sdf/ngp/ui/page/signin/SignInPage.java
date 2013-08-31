/*
 * Copyright (c) 2007 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.ui.page.signin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;

import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.page.WicketPage;


public final class SignInPage extends WicketPage
{
	private static final Log log = LogFactory.getLog(SignInPage.class);
	
	
	/**
	 * Construct
	 */
	public SignInPage()
	{
		this(null);
		
	}

	/**
	 * Constructor
	 * 
	 * @param parameters
	 *            The page parameters
	 */
	public SignInPage(final PageParameters parameters)
	{
		super(parameters);
		
		add(new SignInPanel("signInPanel")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 6605490315123081426L;

			@Override
			public boolean signIn(String username, String password)
			{			
				return ((WicketSession)getSession()).authenticate(username, password);
			}
		});
	}
}
