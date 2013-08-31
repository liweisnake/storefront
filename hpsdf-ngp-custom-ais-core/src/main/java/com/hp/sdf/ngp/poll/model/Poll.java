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
package com.hp.sdf.ngp.poll.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Poll implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1372663305546454966L;
	private Long id;
	private String title;
	private String description;
	private boolean multipleChoice;
	private boolean viewResult;
	private Date expiration;
	private Set<Choice> choices;

	public Poll() {

	}

	public Poll(String title, String description, Date expiration,
			Set<Choice> choice) {
		this.setTitle(title);
		this.setDescription(description);
		this.setExpiration(expiration);
		this.setChoice(choice);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false)
	public boolean getMultipleChoice() {
		return multipleChoice;
	}

	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
	}

	@Column(nullable = false)
	public boolean getViewResult() {
		return viewResult;
	}

	public void setViewResult(boolean viewResult) {
		this.viewResult = viewResult;
	}

	@Column(nullable = false)
	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "poll")
	public Set<Choice> getChoice() {
		return choices;
	}

	public void setChoice(Set<Choice> choices) {
		this.choices = choices;
	}

}

// $Id$