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
package com.hp.sdf.ngp.eav.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.dao.JpaDao;
import com.hp.sdf.ngp.eav.AttributeType;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.eav.dao.AttributeDAO;
import com.hp.sdf.ngp.eav.dao.AttributeValueDomainDAO;
import com.hp.sdf.ngp.eav.dao.EntityAttributeDAO;
import com.hp.sdf.ngp.eav.dao.EntityDAO;
import com.hp.sdf.ngp.eav.model.Attribute;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.eav.model.AttributeValueChar;
import com.hp.sdf.ngp.eav.model.AttributeValueDate;
import com.hp.sdf.ngp.eav.model.AttributeValueDomain;
import com.hp.sdf.ngp.eav.model.AttributeValueNumber;
import com.hp.sdf.ngp.eav.model.Entity;
import com.hp.sdf.ngp.eav.model.EntityAttribute;
import com.hp.sdf.ngp.eav.util.EavUtil;

@Service
@Transactional
public class EavRepositoryImpl implements EavRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = 422939894385487938L;

	private final static Log log = LogFactory.getLog(EavRepositoryImpl.class);

	@Resource
	private EntityDAO entityDao;

	@Resource
	private AttributeDAO attributeDao;

	@Resource
	private EntityAttributeDAO entityAttributeDao;

	@Resource
	private AttributeValueDomainDAO attributeValueDomainDao;

	@Resource
	private EavUtil eavUtil;

	public void addEntity(Entity entity) {
		this.entityDao.persist(entity);
	}

	private void deleteAttributeValue(Long entityID, AttributeType attributeType) {
		this.deleteAttributeValue(entityID, null, attributeType);
	}

	private void deleteAttributeValue(Long entityID, Long attributeId, AttributeType attributeType) {
		JpaDao dao = this.eavUtil.getAttributeValueDao(attributeType);
		String tableName = null;
		switch (attributeType) {
		case varchar:
			tableName = AttributeValueChar.class.getSimpleName();
			break;
		case number:
			tableName = AttributeValueNumber.class.getSimpleName();
			break;
		case date:
			tableName = AttributeValueDate.class.getSimpleName();
			break;
		}
		if (tableName == null || entityID == null) {
			return;
		}
		String hql = "delete from " + tableName + " model where model.entityId = ?";
		if (attributeId != null) {
			hql = hql + " and model.attribute.attributeID= ? ";
		}
		log.debug("Delete values in " + tableName);
		log.debug(hql);
		if (attributeId != null) {
			dao.executeHQLBySequenceParam(hql, entityID, attributeId);
		} else {
			dao.executeHQLBySequenceParam(hql, entityID);
		}

	}

	private void deleteEntityAttributeValue(Long entityID, Long attributeId, AttributeType attributeType) {
		JpaDao dao = this.eavUtil.getAttributeValueDao(attributeType);
		String tableName = null;
		switch (attributeType) {
		case varchar:
			tableName = AttributeValueChar.class.getSimpleName();
			break;
		case number:
			tableName = AttributeValueNumber.class.getSimpleName();
			break;
		case date:
			tableName = AttributeValueDate.class.getSimpleName();
			break;
		}
		if (tableName == null || entityID == null) {
			return;
		}
		String hql = "select attributeValueID from " + tableName + " where  entityId=? and attributeID=? ";
		List<Long> attributeValueIDs = dao.findHql(hql, entityID, attributeId);
		if (attributeValueIDs == null || attributeValueIDs.size() == 0) {
			return;
		}
		
		/*
		 * String sql = "delete from " + EntityAttribute.class.getSimpleName() +
		 * " using " + EntityAttribute.class.getSimpleName() + "," + tableName +
		 * " where "
		 * +EntityAttribute.class.getSimpleName()+".attributeValueID="+tableName
		 * +".attributeValueID and "+tableName+".entityId = " + entityID; if
		 * (attributeId != null) { sql = sql +
		 * " and "+tableName+".attributeID= " + attributeId; }
		 */
		
		hql = "delete from " + EntityAttribute.class.getSimpleName() + " where attributeValueID in (-1";
		for (Long attributeValueID : attributeValueIDs) {
			hql = hql + ",?";
		}
		hql = hql + ")";

		
		log.debug("Delete values in " + EntityAttribute.class.getSimpleName());
		log.debug(hql);
		Long[] objects=new Long[attributeValueIDs.size()];
		objects=attributeValueIDs.toArray(objects);
		dao.executeHQLBySequenceParam(hql, objects);
	}

	@SuppressWarnings("unchecked")
	public void deleteEntity(Long entityID) {

		Entity entity = this.getEntityByID(entityID);
		if (entity == null) {
			return;
		}
		for (AttributeType attributeType : AttributeType.values()) {
			deleteAttributeValue(entityID, attributeType);
		}

		this.entityDao.remove(entity);
	}

	@SuppressWarnings("unchecked")
	public Entity getEntityByID(Long entityID) {
		Entity entity = this.entityDao.findById(entityID);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public void addEntityAttribute(List<EntityAttribute> attributes) {
		if (attributes == null || attributes.size() == 0)
			return;
		Iterator<EntityAttribute> iterator = attributes.iterator();
		while (iterator.hasNext()) {
			EntityAttribute ea = iterator.next();
			Object object = ea.getAttributeValueObject();
			AttributeType type = ea.getAttributeType();
			JpaDao dao = this.eavUtil.getAttributeValueDao(type);
			AttributeValue value = this.eavUtil.getAttributeValueModel(type);
			value.setAttribute(ea.getAttribute());
			value.setValue(object);
			value.setEntityId(ea.getEntity().getEntityID());
			dao.persist(value);
			ea.setAttributeValueID(value.getAttributeValueID());
			this.entityAttributeDao.persist(ea);
		}
	}

	@SuppressWarnings("unchecked")
	public Long addEntityAttribute(Long entityID, Long attributeID, AttributeType attributeType, Object object) {
		EntityAttribute ea = new EntityAttribute();
		ea.setEntity(new Entity(entityID));
		ea.setAttributeType(attributeType);
		JpaDao dao = this.eavUtil.getAttributeValueDao(attributeType);
		AttributeValue value = this.eavUtil.getAttributeValueModel(attributeType);
		value.setAttribute(new Attribute(attributeID));
		value.setValue(object);
		value.setEntityId(entityID);
		dao.persist(value);
		ea.setAttributeValueID(value.getAttributeValueID());
		this.entityAttributeDao.persist(ea);
		return ea.getId();
	}

	private Attribute getAttributeByName(String attributeName) {
		List<Attribute> list = this.attributeDao.findByHQL("attributeName", attributeName);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public Long addEntityAttribute(Long entityID, String attributeName, AttributeType attributeType, Object object) {
		Long result = null;
		Attribute attribute = getAttributeByName(attributeName);
		if (attribute == null)
			return null;
		Long attributeID = attribute.getAttributeID();
		result = this.addEntityAttribute(entityID, attributeID, attributeType, object);
		return result;
	}

	@SuppressWarnings("unchecked")
	public void updateEntityAttribute(List<EntityAttribute> attributes) {
		if (attributes == null || attributes.size() == 0)
			return;
		Iterator<EntityAttribute> iterator = attributes.iterator();
		while (iterator.hasNext()) {
			EntityAttribute ea = iterator.next();
			Long attributeValueID = ea.getAttributeValueID();
			Object object = ea.getAttributeValueObject();
			AttributeType type = ea.getAttributeType();
			JpaDao dao = this.eavUtil.getAttributeValueDao(type);
			AttributeValue value = (AttributeValue) dao.findById(attributeValueID);
			value.setValue(object);
			dao.merge(value);
		}
	}

	@SuppressWarnings("unchecked")
	public void updateEntityAttribute(Long entityAttributeID, Object object) {
		EntityAttribute ea = this.entityAttributeDao.findById(entityAttributeID);
		if (ea == null) {
			return;
		}
		JpaDao dao = this.eavUtil.getAttributeValueDao(ea.getAttributeType());
		AttributeValue value = (AttributeValue) dao.findById(ea.getAttributeValueID());
		if (value == null) {
			return;
		}
		value.setValue(object);
		dao.merge(value);
	}

	@SuppressWarnings("unchecked")
	public void updateEntityAttribute(Long attributeValueID, AttributeType attributeType, Object object) {
		JpaDao dao = this.eavUtil.getAttributeValueDao(attributeType);
		AttributeValue value = (AttributeValue) dao.findById(attributeValueID);
		if (value == null) {
			return;
		}
		value.setValue(object);
		dao.merge(value);
	}

	@SuppressWarnings("unchecked")
	public void deleteEntityAttribute(Long entityAttributeID) {
		EntityAttribute ea = this.entityAttributeDao.findById(entityAttributeID);
		if (ea == null) {
			return;
		}
		JpaDao dao = this.eavUtil.getAttributeValueDao(ea.getAttributeType());
		dao.remove(ea.getAttributeValueID());
		this.entityAttributeDao.remove(ea);
	}

	public void deleteEntityAttribute(Long entityID, String attributeName) {
		if (entityID == null) {
			return;
		}
		Attribute attribute = this.getAttributeByName(attributeName);
		if (attribute == null) {
			return;
		}
		// delete the entity attribute first
		this.deleteEntityAttributeValue(entityID, attribute.getAttributeID(), attribute.getAttributeType());
		// then delete the attribute value
		this.deleteAttributeValue(entityID, attribute.getAttributeID(), attribute.getAttributeType());

	}

	private Attribute getAttributeById(Long attributeID) {
		if (attributeID == null) {
			return null;
		}
		return this.attributeDao.findById(attributeID);
	}

	@SuppressWarnings("unchecked")
	public List<AttributeValue> getEntityAttributes(Long entityID, Long attributeID) {
		Attribute attribute = this.getAttributeById(attributeID);
		return getEntityAttributes(entityID, attribute);
	}

	@SuppressWarnings("unchecked")
	public List<AttributeValue> getEntityAttributes(Long entityID, String attributeName) {
		Attribute attribute = this.getAttributeByName(attributeName);
		return getEntityAttributes(entityID, attribute);
	}

	@SuppressWarnings("unchecked")
	private List<AttributeValue> getEntityAttributes(Long entityID, Attribute attribute) {
		List<AttributeValue> list = null;
		if (entityID == null) {
			return list;
		}

		if (attribute == null) {
			return list;
		}
		String tableName = null;
		switch (attribute.getAttributeType()) {
		case varchar:
			tableName = AttributeValueChar.class.getSimpleName();
			break;
		case number:
			tableName = AttributeValueNumber.class.getSimpleName();
			break;
		case date:
			tableName = AttributeValueDate.class.getSimpleName();
			break;
		}
		if (tableName == null) {
			return list;
		}
		JpaDao dao = this.eavUtil.getAttributeValueDao(attribute.getAttributeType());
		list = (List<AttributeValue>) dao.findHql("from " + tableName + " model where model.entityId= ? and model.attribute.attributeID= ? ",
				entityID, attribute.getAttributeID());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AttributeValue> getEntityAttributes(Long entityID) {
		List<AttributeValue> list = new ArrayList<AttributeValue>();
		if (entityID == null) {
			return null;
		}
		
		for (AttributeType attributeType : AttributeType.values()) {
			String tableName = null;
			switch (attributeType) {
			case varchar:
				tableName = AttributeValueChar.class.getSimpleName();
				break;
			case number:
				tableName = AttributeValueNumber.class.getSimpleName();
				break;
			case date:
				tableName = AttributeValueDate.class.getSimpleName();
				break;
			}
			if (tableName == null) {
				return list;
			}
			JpaDao dao = this.eavUtil.getAttributeValueDao(attributeType);
			
//			List<AttributeValue> result = (List<AttributeValue>) dao.findBy("entityId", entityID);
			List<AttributeValue> result = dao.findHql("from " + tableName + " join " + tableName + ". where ");
			if (result != null && result.size() > 0) {
				list.addAll(result);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void addAttributeDefine(Attribute attribute) {
		if (attribute == null)
			return;
		this.attributeDao.persist(attribute);
		AttributeType type = attribute.getAttributeType();
		Long id = attribute.getAttributeID();
		if (attribute.getAttributeValueDomain() != null && attribute.getAttributeValueDomain().size() > 0) {
			Iterator<AttributeValueDomain> iterator = attribute.getAttributeValueDomain().iterator();
			while (iterator.hasNext()) {
				AttributeValueDomain avd = iterator.next();
				JpaDao dao = this.eavUtil.getAttributeValueDao(type);
				AttributeValue value = this.eavUtil.getAttributeValueModel(type);
				value.setAttribute(attribute);
				value.setValue(avd.getAttributeValueObject());
				value.setEntityId(0L);
				dao.persist(value);
				avd.setAttributeValueID(value.getAttributeValueID());
				this.attributeValueDomainDao.persist(avd);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void updateAttributeDefine(Attribute attribute) {
		if (attribute == null)
			return;
		Set<AttributeValueDomain> domains = attribute.getAttributeValueDomain();
		attribute.setAttributeValueDomain(null);
		this.attributeDao.merge(attribute);
		AttributeType type = attribute.getAttributeType();
		Long id = attribute.getAttributeID();
		JpaDao dao = this.eavUtil.getAttributeValueDao(type);
		AttributeValue value = this.eavUtil.getAttributeValueModel(type);

		String sql = "delete from " + value.getClass().getSimpleName() + " using " + value.getClass().getSimpleName() + ",AttributeValueDomain"
				+ " where " + value.getClass().getSimpleName()
				+ ".attributeValueID = AttributeValueDomain.attributeValueID and AttributeValueDomain.attributeID = " + id;
		// dao.batchExecuteBySequenceParam(sql,id);
		log.info("Delete attribute domain values.");
		log.info(sql);
		dao.sqlExecute(sql);
		String sqlDomain = "delete from AttributeValueDomain model where model.attribute.attributeID = ?";
		log.info("Delete attribute domains.");
		log.info(sqlDomain);
		this.attributeValueDomainDao.executeHQLBySequenceParam(sqlDomain, id);

		if (domains != null && domains.size() > 0) {
			Iterator<AttributeValueDomain> iterator = domains.iterator();
			while (iterator.hasNext()) {
				AttributeValueDomain avd = iterator.next();
				AttributeValue v = this.eavUtil.getAttributeValueModel(type);
				v.setAttribute(attribute);
				v.setValue(avd.getAttributeValueObject());
				v.setEntityId(0L);
				dao.persist(v);
				avd.setAttributeValueID(v.getAttributeValueID());
				this.attributeValueDomainDao.persist(avd);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void deleteAttributeDefine(Long attributeID) {
		Attribute attribute = this.getAttributeDefineByID(attributeID, false);
		AttributeType type = attribute.getAttributeType();
		JpaDao dao = this.eavUtil.getAttributeValueDao(type);
		AttributeValue value = this.eavUtil.getAttributeValueModel(type);

		String sql = "delete from " + value.getClass().getSimpleName() + " using " + value.getClass().getSimpleName() + ",AttributeValueDomain"
				+ " where " + value.getClass().getSimpleName()
				+ ".attributeValueID = AttributeValueDomain.attributeValueID and AttributeValueDomain.attributeID = " + attributeID;
		log.info("Delete attribute domain values.");
		log.info(sql);
		dao.sqlExecute(sql);
		String sqlDomain = "delete from AttributeValueDomain model where model.attribute.attributeID = ?";
		log.info("Delete attribute domains.");
		log.info(sqlDomain);
		this.attributeValueDomainDao.executeHQLBySequenceParam(sqlDomain, attributeID);

		this.attributeDao.remove(attribute);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Long addAttributeValueDomain(Long attributeID, Object object, boolean isDefault) {
		Attribute attribute = this.getAttributeDefineByID(attributeID, false);
		JpaDao dao = this.eavUtil.getAttributeValueDao(attribute.getAttributeType());
		AttributeValue value = this.eavUtil.getAttributeValueModel(attribute.getAttributeType());
		value.setAttribute(new Attribute(attributeID));
		value.setValue(object);
		value.setEntityId(0L);
		dao.persist(value);
		AttributeValueDomain avd = new AttributeValueDomain();
		avd.setAttribute(attribute);
		avd.setAttributeValueID(value.getAttributeValueID());
		avd.setIsDefault(isDefault);
		this.attributeValueDomainDao.persist(avd);
		return avd.getId();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void deleteAttributeValueDomain(Long attributeValueDomainID) {
		AttributeValueDomain domain = this.attributeValueDomainDao.findById(attributeValueDomainID);
		AttributeType type = domain.getAttribute().getAttributeType();
		JpaDao dao = this.eavUtil.getAttributeValueDao(type);
		AttributeValue value = this.eavUtil.getAttributeValueModel(type);
		dao.executeHQLBySequenceParam("delete from " + value.getClass().getSimpleName() + " model where model.attributeValueID = ?", domain
				.getAttributeValueID());
		this.attributeValueDomainDao.remove(domain);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Attribute getAttributeDefineByID(Long attributeID, boolean loadDomain) {
		Attribute result = null;
		result = this.attributeDao.findById(attributeID);
		if (result == null || !loadDomain)
			return result;
		if (result.getAttributeValueDomain() == null || result.getAttributeValueDomain().size() == 0)
			return result;
		AttributeType type = result.getAttributeType();
		Iterator<AttributeValueDomain> iterator = result.getAttributeValueDomain().iterator();
		while (iterator.hasNext()) {
			AttributeValueDomain attributeValueDomain = iterator.next();
			Long attributeValueID = attributeValueDomain.getAttributeValueID();
			JpaDao dao = this.eavUtil.getAttributeValueDao(type);
			AttributeValue value = (AttributeValue) dao.findById(attributeValueID);
			if (value != null)
				attributeValueDomain.setAttributeValueObject(value.getValue());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Attribute getAttributeDefineByName(String attributeName, boolean loadDomain) {
		Attribute result = null;
		List<Attribute> list = this.attributeDao.findByHQL("attributeName", attributeName);
		if (list == null || list.size() == 0)
			return null;
		result = list.get(0);
		if (result == null || !loadDomain)
			return result;
		if (result.getAttributeValueDomain() == null || result.getAttributeValueDomain().size() == 0)
			return result;
		AttributeType type = result.getAttributeType();
		Iterator<AttributeValueDomain> iterator = result.getAttributeValueDomain().iterator();
		while (iterator.hasNext()) {
			AttributeValueDomain attributeValueDomain = iterator.next();
			Long attributeValueID = attributeValueDomain.getAttributeValueID();
			JpaDao dao = this.eavUtil.getAttributeValueDao(type);
			AttributeValue value = (AttributeValue) dao.findById(attributeValueID);
			if (value != null)
				attributeValueDomain.setAttributeValueObject(value.getValue());
		}
		return result;
	}

	@Transactional
	public boolean existsAttributeDefine(String name) {
		List<Attribute> attributes = attributeDao.findByHQL("attributeName", name);
		if (attributes == null || attributes.size() == 0) {
			return false;
		}
		return true;

	}

}

// $Id$