/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
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
package com.hp.sdf.ngp.handmark;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

public interface AssetImportor {

	/**
	 * parse the XML provided by handmark and save the asset into database. At
	 * the same time, the category and platform will be stored also
	 * 
	 * @param masterXMLInputStream
	 * @param deviceXMLInputStream
	 * @return import success or fail
	 */
	public boolean importAsset(InputStream masterXMLInputStream,
			InputStream deviceXMLInputStream) throws JAXBException;
}
