package com.hp.sdf.ngp.eav.dao;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.dao.JpaDao;
import com.hp.sdf.ngp.eav.model.Entity;

@Component
public class EntityDAO extends JpaDao<Long,Entity>{

}