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
package com.hp.sdf.ngp.poll.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hp.sdf.ngp.banner.PollService;
import com.hp.sdf.ngp.poll.dao.ChoiceDAO;
import com.hp.sdf.ngp.poll.dao.PollDAO;
import com.hp.sdf.ngp.poll.model.Poll;

@Service
public class PollServiceImpl implements PollService {

	private static final Log log = LogFactory.getLog(PollServiceImpl.class);

	@Resource
	private ChoiceDAO choiceDao;

	@Resource
	private PollDAO pollDao;

	public void addPoll(Poll poll) {
		Poll p = new Poll(poll.getTitle(), poll.getDescription(), poll
				.getExpiration(), poll.getChoice());
		this.pollDao.persist(p);
		poll.setId(p.getId());
	}

	public void deletePoll(Long id) {
		Poll p = pollDao.findById(id);
		if (p != null){
			pollDao.remove(p);
		}
	}

	public void updatePoll(Poll poll) {
		pollDao.merge(poll);

	}

	public Poll getPoll(Long id) {
		Poll p = this.pollDao.findById(id);
		return p;
	}

}

// $Id$