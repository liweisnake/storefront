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
package com.hp.sdf.ngp.ui.page.rating;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.model.IModel;

/**
 * A customized rating panel without Ajax support
 * 
 *
 */
public abstract class CustomizedRatingPanel extends RatingPanel{

	
	
	public CustomizedRatingPanel(String id, IModel<? extends Number> rating,
			IModel<Integer> nrOfStars, IModel<Integer> nrOfVotes,
			IModel<Boolean> hasVoted, boolean addDefaultCssStyle) {
		super(id, rating, nrOfStars, nrOfVotes, hasVoted, addDefaultCssStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomizedRatingPanel(String id, IModel<? extends Number> rating,
			int nrOfStars, boolean addDefaultCssStyle) {
		super(id, rating, nrOfStars, addDefaultCssStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomizedRatingPanel(String id, IModel<? extends Number> rating,
			int nrOfStars, IModel<Integer> nrOfVotes, boolean addDefaultCssStyle) {
		super(id, rating, nrOfStars, nrOfVotes, addDefaultCssStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomizedRatingPanel(String id, IModel<? extends Number> rating) {
		super(id, rating);
		// TODO Auto-generated constructor stub
	}

	public CustomizedRatingPanel(String id, int nrOfStars) {
		super(id, nrOfStars);
		// TODO Auto-generated constructor stub
	}

	public CustomizedRatingPanel(String id) {
		super(id);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 438299891375839029L;
	
	protected Component newRatingStarBar(String id, IModel<Integer> nrOfStars)
	{
		return new RatingStarBar(id, nrOfStars);
	}
	
	/**
	 * Renders the stars and the links necessary for rating.
	 */
	private final class RatingStarBar extends Loop
	{
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		private RatingStarBar(String id, IModel<Integer> model)
		{
			super(id, model);
		}

		@Override
		protected void populateItem(LoopItem item)
		{
			// Use an link without Ajax
			Link<Void> link = new Link<Void>("link")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					LoopItem item = (LoopItem)getParent();				

					onRated(item.getIteration() + 1, null);					
				}				
			};

			int iteration = item.getIteration();

			// add the star image, which is either active (highlighted) or
			// inactive (no star)
			link.add(new WebMarkupContainer("star").add(new SimpleAttributeModifier("src",
				(onIsStarActive(iteration) ? getActiveStarUrl(iteration)
					: getInactiveStarUrl(iteration)))));
			item.add(link);
		}
	}

	

}

// $Id$