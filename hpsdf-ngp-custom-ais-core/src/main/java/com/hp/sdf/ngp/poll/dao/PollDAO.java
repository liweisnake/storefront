package com.hp.sdf.ngp.poll.dao;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.dao.JpaDao;
import com.hp.sdf.ngp.poll.model.Poll;

@Component
public class PollDAO extends JpaDao<Long,Poll>{

}