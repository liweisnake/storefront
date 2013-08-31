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
package com.hp.sdf.ngp.handmark.parse;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.handmark.model.Data;
import com.hp.sdf.ngp.handmark.model.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestMasterJAXBParse {

	private static final String COM_HP_SDF_NGP_HANDMARK = "com.hp.sdf.ngp.handmark.model";

	@Test
	public void testHandmarkParse() throws JAXBException {

		JAXBContext jc = JAXBContext.newInstance(COM_HP_SDF_NGP_HANDMARK);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Data data = (Data) unmarshaller.unmarshal(this.getClass()
				.getClassLoader().getResourceAsStream("master_feed.xml"));

		List<Product> productList = data.getProducts().getProduct();

		// productList.size() == 1682
		Assert.assertTrue(productList.size() > 1);
	}

}
