package com.hp.sdf.ngp.poll.dao;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.dao.JpaDao;
import com.hp.sdf.ngp.poll.model.Choice;

@Component
public class ChoiceDAO extends JpaDao<Long,Choice>{

}