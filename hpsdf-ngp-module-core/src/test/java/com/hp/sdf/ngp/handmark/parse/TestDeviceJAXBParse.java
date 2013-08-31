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

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.handmark.model.device.Device;
import com.hp.sdf.ngp.handmark.model.device.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestDeviceJAXBParse {

	private static final String COM_HP_SDF_NGP_HANDMARK_DEVICE = "com.hp.sdf.ngp.handmark.model.device";

	@Test
	public void testDeviceParse() {

		InputStream deviceXMLInputStream = this.getClass().getClassLoader().getResourceAsStream("device_feed.xml");
		List<Device> devices = null;
		try {
			devices = getDevices(deviceXMLInputStream);
		} catch (JAXBException exception) {
			exception.printStackTrace();
		}

		Assert.assertTrue(devices != null && devices.size() > 0);
	}

	/**
	 * get handmark devices from device feed.
	 * 
	 * 
	 * @param deviceXMLInputStream
	 * @return List<Device>
	 * @throws JAXBException
	 */
	private List<Device> getDevices(InputStream deviceXMLInputStream) throws JAXBException {
		JAXBContext devJc = JAXBContext.newInstance(COM_HP_SDF_NGP_HANDMARK_DEVICE);
		Unmarshaller deviceUnmarshaller = devJc.createUnmarshaller();
		Message devicesData = (Message) deviceUnmarshaller.unmarshal(deviceXMLInputStream);

		List<Device> devices = devicesData.getDevice();
		return devices;
	}

}
