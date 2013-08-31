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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.CommentsOrderBy;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetIdCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetNameCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetProviderNameCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsContentCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsTitleCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CommentSearchCondition;

public class CommentSearchDataProvider implements IDataProvider<Comments> {

	private static final long serialVersionUID = 6080358671456933625L;

	private final static Log log = LogFactory.getLog(CommentSearchDataProvider.class);

	private ApplicationService applicationService;

	private CommentSearchCondition commentSearchCondition;

	public CommentSearchCondition getCommentSearchCondition() {
		return commentSearchCondition;
	}

	public void setCommentSearchCondition(CommentSearchCondition commentSearchCondition) {
		this.commentSearchCondition = commentSearchCondition;
	}

	public CommentSearchDataProvider(ApplicationService applicationService, CommentSearchCondition commentSearchCondition) {
		this.applicationService = applicationService;
		this.commentSearchCondition = commentSearchCondition;
	}

	private SearchExpression getSearchExpression(int first, int count) {

		SearchExpression searchExpression = new SearchExpressionImpl();

		if (commentSearchCondition != null) {
			String keyword = (null == commentSearchCondition.getKeyword()) ? "" : commentSearchCondition.getKeyword();
			searchExpression.addCondition(new CommentsContentCondition(keyword, StringComparer.LIKE, true, false).or(new CommentsTitleCondition(keyword, StringComparer.LIKE, true, false)));

			if (StringUtils.isNotEmpty(commentSearchCondition.getAssetName())) {
				searchExpression.addCondition(new CommentsAssetNameCondition(commentSearchCondition.getAssetName(), StringComparer.EQUAL, true, false));
			}

			try {
				if (commentSearchCondition.getAssetID() != null) {
					searchExpression.addCondition(new CommentsAssetIdCondition(new Long(commentSearchCondition.getAssetID()), NumberComparer.EQUAL));
				}
			} catch (Exception e) {
				searchExpression.addCondition(new CommentsAssetIdCondition(-1L, NumberComparer.EQUAL));
				log.error("Exception :" + e);
			}

			if (StringUtils.isNotEmpty(commentSearchCondition.getProviderName())) {
				searchExpression.addCondition(new CommentsAssetProviderNameCondition(commentSearchCondition.getProviderName(), StringComparer.EQUAL, true, false));
			}

			try {
				if (commentSearchCondition.getPid() != null) {
					searchExpression.addCondition(new CommentsAssetProviderIdCondition(new Long(commentSearchCondition.getPid()), NumberComparer.EQUAL));
				}
			} catch (Exception e) {
				searchExpression.addCondition(new CommentsAssetProviderIdCondition(-1L, NumberComparer.EQUAL));
				log.error("Exception :" + e);
			}

			if (StringUtils.isNotEmpty(commentSearchCondition.getSubscriberName())) {
				searchExpression.addCondition(new CommentsUseridCondition(commentSearchCondition.getSubscriberName(), StringComparer.LIKE, true, false));
			}
		}

		// can not add provider order because currently not support this order
		// searchExpression.addOrder(CommentsOrderBy.PROVIDERID, OrderEnum.ASC);

		searchExpression.addOrder(CommentsOrderBy.UPDATEDATE, OrderEnum.DESC);
		searchExpression.addOrder(CommentsOrderBy.ASSETID, OrderEnum.ASC);

		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		return searchExpression;
	}

	public Iterator<? extends Comments> iterator(int first, int count) {
		log.debug("first :" + first);
		log.debug("count :" + count);

		SearchExpression searchExpression = getSearchExpression(first, count);

		List<Comments> list = applicationService.searchComments(searchExpression);
		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public IModel<Comments> model(Comments object) {
		return new Model<Comments>(object);
	}

	public int size() {

		SearchExpression searchExpression = getSearchExpression(0, Integer.MAX_VALUE);

		return new Long(applicationService.countComments(searchExpression)).intValue();
	}

	public void detach() {
	}

}
