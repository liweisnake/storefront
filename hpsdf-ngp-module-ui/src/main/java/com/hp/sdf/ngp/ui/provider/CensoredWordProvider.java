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
package com.hp.sdf.ngp.ui.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.model.CommentsSensorWord;
import com.hp.sdf.ngp.service.ApplicationService;

public class CensoredWordProvider implements IDataProvider<CommentsSensorWord> {

	private static final long serialVersionUID = -7901372154709286262L;

	private ApplicationService applicationService;

	public CensoredWordProvider(ApplicationService applicationService) {

		this.applicationService = applicationService;
	}

	public Iterator<? extends CommentsSensorWord> iterator(int first, int count) {

		List<CommentsSensorWord> commentsSensorWords = applicationService.getAllCommentsSensorWord(first, count);

		if (null == commentsSensorWords) {
			return null;
		}

		return commentsSensorWords.iterator();
	}

	public IModel<CommentsSensorWord> model(CommentsSensorWord object) {
		return new Model<CommentsSensorWord>(object);
	}

	public int size() {

		return (int) applicationService.getAllCommentsSensorWordCount();
	}

	public void detach() {
	}
}