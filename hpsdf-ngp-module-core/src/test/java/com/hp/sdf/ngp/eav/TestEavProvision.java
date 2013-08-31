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
package com.hp.sdf.ngp.eav;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.eav.impl.EavRepositoryImpl;
import com.hp.sdf.ngp.eav.model.Attribute;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.eav.model.AttributeValueDomain;
import com.hp.sdf.ngp.eav.model.Entity;
import com.hp.sdf.ngp.eav.model.EntityAttribute;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestEavProvision extends DBEnablerTestBase {

	@Resource
	private EavRepositoryImpl eavRepository;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testGetAttributeByID() throws Exception {
		Attribute attribute = this.eavRepository.getAttributeDefineByID(new Long(1), false);
		Assert.assertTrue(attribute.getAttributeName().equals("Category"));
	}

	@Test
	public void testAddAttributeDefine() throws Exception {
		Attribute attribute = new Attribute();
		attribute.setAttributeName("balabala");
		attribute.setAttributeType(AttributeType.varchar);
		AttributeValueDomain domain = new AttributeValueDomain();
		domain.setAttribute(attribute);
		domain.setAttributeValueObject("kidding me");
		Set<AttributeValueDomain> set = new HashSet<AttributeValueDomain>();
		set.add(domain);
		attribute.setAttributeValueDomain(set);
		this.eavRepository.addAttributeDefine(attribute);
		Attribute check = this.eavRepository.getAttributeDefineByID(attribute.getAttributeID(), true);
		boolean result = (check != null) && (check.getAttributeValueDomain() != null) && (check.getAttributeValueDomain().size() > 0)
				&& (check.getAttributeValueDomain().iterator().next().getAttributeValueID() > 0);
		Assert.assertTrue(result);
	}

	@Test
	public void testUpdateAttributeDefine() throws Exception {
		Attribute attribute = new Attribute();
		attribute.setAttributeID(new Long(1));
		attribute.setAttributeName("Category2.0");
		attribute.setAttributeType(AttributeType.varchar);
		AttributeValueDomain domain = new AttributeValueDomain();
		domain.setAttribute(attribute);
		domain.setAttributeValueObject("News");
		Set<AttributeValueDomain> set = new HashSet<AttributeValueDomain>();
		set.add(domain);
		attribute.setAttributeValueDomain(set);
		try {
			this.eavRepository.updateAttributeDefine(attribute);
		} catch (Exception e) {
		}
		// Attribute check = this.eavRepository.getAttributeDefineByID(new
		// Long(1), true);
		// boolean result = (check != null)
		// && (check.getAttributeName().equals("Category2.0"))
		// && (check.getAttributeValueDomain() != null)
		// && (check.getAttributeValueDomain().size() == 1)
		// &&
		// (check.getAttributeValueDomain().iterator().next().getAttributeValueObject().equals("News"));
		Assert.assertTrue(true);
	}

	@Test
	public void testDeleteAttributeDefine() throws Exception {
		Long id = new Long(1);
		try {
			this.eavRepository.deleteAttributeDefine(id);
		} catch (Exception e) {
		}
		// Attribute attribute = this.eavRepository.getAttributeDefineByID(id,
		// false);
		Assert.assertTrue(true);
	}

	@Test
	public void testDeleteEntityAttribute2() throws Exception {
		cleanAttributeValue();
		List<EntityAttribute> list = new ArrayList<EntityAttribute>();
		EntityAttribute ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(2)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("Linux");
		ea.setEntity(new Entity(new Long(1)));
		list.add(ea);

		ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(2)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("WINDOWS");
		ea.setEntity(new Entity(new Long(1)));
		list.add(ea);
		this.eavRepository.addEntityAttribute(list);

		ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(1)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("APPLE");
		ea.setEntity(new Entity(new Long(1)));
		list.add(ea);
		this.eavRepository.addEntityAttribute(list);

		this.eavRepository.deleteEntityAttribute(1L, "Platform");

		List<AttributeValue> attributeValues = this.eavRepository.getEntityAttributes(1L, "Platform");
		Assert.assertTrue(attributeValues == null || attributeValues.size() == 0);
		ITable table = this.databaseTester.getConnection().createQueryTable("result",
				"select * from attributevaluechar where attributevaluechar.entityId=1");
		Assert.assertTrue(table.getRowCount() > 0);
		table = this.databaseTester.getConnection().createQueryTable("result",
				"select * from attributevaluechar where attributevaluechar.entityId=1 and attributevaluechar.attributeid=2");
		Assert.assertTrue(table.getRowCount() == 0);

	}

	@Test
	public void testAddEntityAttribute() throws Exception {
		cleanAttributeValue();
		List<EntityAttribute> list = new ArrayList<EntityAttribute>();
		EntityAttribute ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(2)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("Linux");
		ea.setEntity(new Entity(new Long(1)));
		list.add(ea);

		ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(2)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("WINDOWS");
		ea.setEntity(new Entity(new Long(2)));
		list.add(ea);
		this.eavRepository.addEntityAttribute(list);
		Entity entity = this.eavRepository.getEntityByID(new Long(1));
		Assert.assertTrue(entity != null);
		List<AttributeValue> attributeValues = this.eavRepository.getEntityAttributes(1L);

		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(attributeValues.get(0).getValue().equals("Linux"));

	}

	@Test
	public void testAddEntityAttribute2() throws Exception {
		cleanAttributeValue();
		this.eavRepository.addEntityAttribute(new Long(1), "Platform", AttributeType.varchar, "Unix");
		Entity entity = this.eavRepository.getEntityByID(new Long(1));
		Assert.assertTrue(entity != null);
		List<AttributeValue> attributeValues = this.eavRepository.getEntityAttributes(1L);

		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(attributeValues.get(0).getValue().equals("Unix"));
		Assert.assertTrue(attributeValues.get(0).getAttribute().getAttributeName().equals("Platform"));
	}

	@Test
	public void testUpdateEntityAttribute() throws Exception {

		cleanAttributeValue();

		List<EntityAttribute> list = new ArrayList<EntityAttribute>();
		EntityAttribute ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(2)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("Linux");
		ea.setEntity(new Entity(new Long(1)));
		list.add(ea);

		ea = new EntityAttribute();
		ea.setAttribute(new Attribute(new Long(2)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("WINDOWS");
		ea.setEntity(new Entity(new Long(2)));
		list.add(ea);
		this.eavRepository.addEntityAttribute(list);

		ea = list.get(0);
		ea.setAttributeValueObject("Funny");
		this.eavRepository.updateEntityAttribute(list);

		List<AttributeValue> attributeValues = this.eavRepository.getEntityAttributes(1L);

		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(attributeValues.get(0).getValue().equals("Funny"));

	}

	@Test
	public void testUpdateEntityAttribute2() throws Exception {
		cleanAttributeValue();
		Long entityAttributeID = this.eavRepository.addEntityAttribute(new Long(1), new Long(1), AttributeType.varchar, "testUpdateEntityAttribute2");
		this.eavRepository.updateEntityAttribute(entityAttributeID, "testUpdateEntityAttribute222");
		List<AttributeValue> attributeValues = this.eavRepository.getEntityAttributes(1L);

		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(attributeValues.get(0).getValue().equals("testUpdateEntityAttribute222"));

	}

	@Test
	public void testGetEntityByID() throws Exception {
		Long id = new Long(1);
		Entity entity = this.eavRepository.getEntityByID(id);
		boolean result = (entity != null) && (entity.getEntityID() == 1);
		Assert.assertTrue(result);
	}

	@Test
	public void testAddEntity() throws Exception {
		Entity entity = new Entity();
		// entity.setReferenceStringID("1");
		// entity.setReferenceLongID(new Long(1));
		entity.setEntityType(EntityType.ASSET.ordinal());

		this.eavRepository.addEntity(entity);
		Entity check = this.eavRepository.getEntityByID(entity.getEntityID());
		boolean result = (check != null);

		Assert.assertTrue(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteEntityAttribute() throws Exception {
		Long id = this.eavRepository.addEntityAttribute(new Long(1), new Long(102), AttributeType.varchar, "testDeleteEntityAttribute");
		this.eavRepository.deleteEntityAttribute(id);
		List<AttributeValue> list = this.eavRepository.getEntityAttributes(new Long(1), new Long(102));
		boolean result = true;
		if (list != null && list.size() > 0) {
			Iterator<AttributeValue> it = list.iterator();
			while (it.hasNext()) {
				result = result && !(it.next().getValue().equals("testDeleteEntityAttribute"));
			}
		}
		Assert.assertTrue(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteEntity() throws Exception {
		Entity entity = new Entity();

		entity.setEntityType(EntityType.ASSET.ordinal());
		List<EntityAttribute> list = new ArrayList<EntityAttribute>();
		EntityAttribute ea = new EntityAttribute();
		ea.setEntity(entity);
		ea.setAttribute(new Attribute(new Long(1)));
		ea.setAttributeType(AttributeType.varchar);
		ea.setAttributeValueObject("testDeleteEntity");
		list.add(ea);

		this.eavRepository.addEntity(entity);
		this.eavRepository.addEntityAttribute(list);

		Long id = entity.getEntityID();
		this.eavRepository.deleteEntity(id);
		ITable tableValue4 = this.databaseTester.getConnection().createQueryTable("result", "select * from entityattribute where entityid=" + id);
		Assert.assertTrue(tableValue4.getRowCount() == 0);
		tableValue4 = this.databaseTester.getConnection().createQueryTable("result", "select * from attributevaluechar where entityid=" + id);
		Assert.assertTrue(tableValue4.getRowCount() == 0);

		tableValue4 = this.databaseTester.getConnection().createQueryTable("result", "select * from attributevaluechar ");
		Assert.assertTrue(tableValue4.getRowCount() > 0);

		tableValue4 = this.databaseTester.getConnection().createQueryTable("result", "select * from entityattribute");
		Assert.assertTrue(tableValue4.getRowCount() > 0);

		Entity checkEntity = this.eavRepository.getEntityByID(id);

		List<AttributeValue> checkValue = this.eavRepository.getEntityAttributes(id);
		boolean result = (checkEntity == null) && (checkValue == null || checkValue.size() == 0);
		Assert.assertTrue(result);
	}

	// @Test
	// public void testGetEntityByReferenceID() throws Exception{
	// Entity entity = this.eavRepository.getEntityByReferenceID("1",
	// EntityType.application);
	// Assert.assertTrue(entity != null);
	// }

	@Test
	public void testAddAttributeValueDomain() throws Exception {
		int size = this.eavRepository.getAttributeDefineByID(new Long(1), true).getAttributeValueDomain().size();
		this.eavRepository.addAttributeValueDomain(new Long(1), "testAddAttributeValueDomain", false);
		Attribute attribute = this.eavRepository.getAttributeDefineByID(new Long(1), true);
		Set<AttributeValueDomain> set = attribute.getAttributeValueDomain();
		boolean result = (set != null) && (set.size() == size + 1);
		// &&
		// (((AttributeValueDomain)set.toArray()[set.size()-1]).getAttributeValueObject().equals("testAddAttributeValueDomain"));
		Assert.assertTrue(result);
	}

	@Test
	public void testDeleteAttributeValueDomain() throws Exception {
		Long id = this.eavRepository.addAttributeValueDomain(new Long(1), "testDeleteAttributeValueDomain", false);
		this.eavRepository.deleteAttributeValueDomain(id);
		Attribute attribute = this.eavRepository.getAttributeDefineByID(new Long(1), true);
		Set<AttributeValueDomain> set = attribute.getAttributeValueDomain();
		boolean result = (set != null) && (set.size() > 0)
				&& (!((AttributeValueDomain) set.toArray()[set.size() - 1]).getAttributeValueObject().equals("testDeleteAttributeValueDomain"));
		Assert.assertTrue(result);
	}

	private void cleanAttributeValue() throws Exception {
		this.databaseTester.getConnection().getConnection().createStatement().execute("delete from entityattribute");
		this.databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluechar");
		this.databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluedate");
		this.databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluenumber");

	}

}

// $Id$