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
package com.hp.sdf.ngp.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.hp.sdf.ngp.eav.model.AttributeValueChar;
import com.hp.sdf.ngp.eav.model.AttributeValueDate;
import com.hp.sdf.ngp.eav.model.AttributeValueNumber;

/**
 * This class is used as the base EAV entity model so as to support EAV search
 * in MySQL using temporary table technology , Once any model need to support
 * EAV, it should extend from this model
 * 
 * Please don't change anything unless you be very aware the details about
 * current implementation EAV search in MySQL
 * 
 * Note that, don't mark any cascade
 */
@MappedSuperclass
public abstract class BaseEAVModel {

	private Long entityId;
	
	@Column(nullable=false)
	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	
	private Set<AttributeValueChar> attributeValueChars;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars() {
		return attributeValueChars;
	}

	public void setAttributeValueChars(Set<AttributeValueChar> attributeValueChars) {
		this.attributeValueChars = attributeValueChars;
	}
	
	private Set<AttributeValueChar> attributeValueChars0;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars0() {
		return attributeValueChars0;
	}

	public void setAttributeValueChars0(Set<AttributeValueChar> attributeValueChars0) {
		this.attributeValueChars0 = attributeValueChars0;
	}
	
	
	
	private Set<AttributeValueChar> attributeValueChars1;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars1() {
		return attributeValueChars1;
	}

	public void setAttributeValueChars1(Set<AttributeValueChar> attributeValueChars1) {
		this.attributeValueChars1 = attributeValueChars1;
	}
	
	private Set<AttributeValueChar> attributeValueChars2;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars2() {
		return attributeValueChars2;
	}

	public void setAttributeValueChars2(Set<AttributeValueChar> attributeValueChars2) {
		this.attributeValueChars2 = attributeValueChars2;
	}
	
	private Set<AttributeValueChar> attributeValueChars3;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars3() {
		return attributeValueChars3;
	}

	public void setAttributeValueChars3(Set<AttributeValueChar> attributeValueChars3) {
		this.attributeValueChars3 = attributeValueChars3;
	}
	
	private Set<AttributeValueChar> attributeValueChars4;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars4() {
		return attributeValueChars4;
	}

	public void setAttributeValueChars4(Set<AttributeValueChar> attributeValueChars4) {
		this.attributeValueChars4 = attributeValueChars4;
	}
	private Set<AttributeValueChar> attributeValueChars5;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars5() {
		return attributeValueChars5;
	}

	public void setAttributeValueChars5(Set<AttributeValueChar> attributeValueChars5) {
		this.attributeValueChars5 = attributeValueChars5;
	}
	
	private Set<AttributeValueChar> attributeValueChars6;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars6() {
		return attributeValueChars6;
	}

	public void setAttributeValueChars6(Set<AttributeValueChar> attributeValueChars6) {
		this.attributeValueChars6 = attributeValueChars6;
	}
	
	private Set<AttributeValueChar> attributeValueChars7;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars7() {
		return attributeValueChars7;
	}

	public void setAttributeValueChars7(Set<AttributeValueChar> attributeValueChars7) {
		this.attributeValueChars7 = attributeValueChars7;
	}
	
	private Set<AttributeValueChar> attributeValueChars8;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars8() {
		return attributeValueChars8;
	}

	public void setAttributeValueChars8(Set<AttributeValueChar> attributeValueChars8) {
		this.attributeValueChars8 = attributeValueChars8;
	}
	
	private Set<AttributeValueChar> attributeValueChars9;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars9() {
		return attributeValueChars9;
	}

	public void setAttributeValueChars9(Set<AttributeValueChar> attributeValueChars9) {
		this.attributeValueChars9 = attributeValueChars9;
	}
	
	private Set<AttributeValueChar> attributeValueChars10;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars10() {
		return attributeValueChars10;
	}

	public void setAttributeValueChars10(Set<AttributeValueChar> attributeValueChars10) {
		this.attributeValueChars10 = attributeValueChars10;
	}
	
	private Set<AttributeValueChar> attributeValueChars11;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars11() {
		return attributeValueChars11;
	}

	public void setAttributeValueChars11(Set<AttributeValueChar> attributeValueChars11) {
		this.attributeValueChars11 = attributeValueChars11;
	}
	
	private Set<AttributeValueChar> attributeValueChars12;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars12() {
		return attributeValueChars12;
	}

	public void setAttributeValueChars12(Set<AttributeValueChar> attributeValueChars12) {
		this.attributeValueChars12 = attributeValueChars12;
	}
	
	private Set<AttributeValueChar> attributeValueChars13;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars13() {
		return attributeValueChars13;
	}

	public void setAttributeValueChars13(Set<AttributeValueChar> attributeValueChars13) {
		this.attributeValueChars13 = attributeValueChars13;
	}
	
	private Set<AttributeValueChar> attributeValueChars14;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars14() {
		return attributeValueChars14;
	}

	public void setAttributeValueChars14(Set<AttributeValueChar> attributeValueChars14) {
		this.attributeValueChars14 = attributeValueChars14;
	}
	
	private Set<AttributeValueChar> attributeValueChars15;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars15() {
		return attributeValueChars15;
	}

	public void setAttributeValueChars15(Set<AttributeValueChar> attributeValueChars15) {
		this.attributeValueChars15 = attributeValueChars15;
	}
	
	private Set<AttributeValueChar> attributeValueChars16;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars16() {
		return attributeValueChars16;
	}

	public void setAttributeValueChars16(Set<AttributeValueChar> attributeValueChars16) {
		this.attributeValueChars16 = attributeValueChars16;
	}
	
	private Set<AttributeValueChar> attributeValueChars17;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars17() {
		return attributeValueChars17;
	}

	public void setAttributeValueChars17(Set<AttributeValueChar> attributeValueChars17) {
		this.attributeValueChars17 = attributeValueChars17;
	}
	
	private Set<AttributeValueChar> attributeValueChars18;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars18() {
		return attributeValueChars18;
	}

	public void setAttributeValueChars18(Set<AttributeValueChar> attributeValueChars18) {
		this.attributeValueChars18 = attributeValueChars18;
	}
	
	private Set<AttributeValueChar> attributeValueChars19;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars19() {
		return attributeValueChars19;
	}

	public void setAttributeValueChars19(Set<AttributeValueChar> attributeValueChars19) {
		this.attributeValueChars19 = attributeValueChars19;
	}
	
	private Set<AttributeValueChar> attributeValueChars20;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars20() {
		return attributeValueChars20;
	}

	public void setAttributeValueChars20(Set<AttributeValueChar> attributeValueChars20) {
		this.attributeValueChars20 = attributeValueChars20;
	}
	
	private Set<AttributeValueChar> attributeValueChars21;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars21() {
		return attributeValueChars21;
	}

	public void setAttributeValueChars21(Set<AttributeValueChar> attributeValueChars21) {
		this.attributeValueChars21 = attributeValueChars21;
	}
	
	private Set<AttributeValueChar> attributeValueChars22;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars22() {
		return attributeValueChars22;
	}

	public void setAttributeValueChars22(Set<AttributeValueChar> attributeValueChars22) {
		this.attributeValueChars22 = attributeValueChars22;
	}
	
	private Set<AttributeValueChar> attributeValueChars23;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars23() {
		return attributeValueChars23;
	}

	public void setAttributeValueChars23(Set<AttributeValueChar> attributeValueChars23) {
		this.attributeValueChars23 = attributeValueChars23;
	}
	
	private Set<AttributeValueChar> attributeValueChars24;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars24() {
		return attributeValueChars24;
	}

	public void setAttributeValueChars24(Set<AttributeValueChar> attributeValueChars24) {
		this.attributeValueChars24 = attributeValueChars24;
	}
	
	private Set<AttributeValueChar> attributeValueChars25;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars25() {
		return attributeValueChars25;
	}

	public void setAttributeValueChars25(Set<AttributeValueChar> attributeValueChars25) {
		this.attributeValueChars25 = attributeValueChars25;
	}
	
	private Set<AttributeValueChar> attributeValueChars26;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars26() {
		return attributeValueChars26;
	}

	public void setAttributeValueChars26(Set<AttributeValueChar> attributeValueChars26) {
		this.attributeValueChars26 = attributeValueChars26;
	}
	
	private Set<AttributeValueChar> attributeValueChars27;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars27() {
		return attributeValueChars27;
	}

	public void setAttributeValueChars27(Set<AttributeValueChar> attributeValueChars27) {
		this.attributeValueChars27 = attributeValueChars27;
	}
	
	private Set<AttributeValueChar> attributeValueChars28;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars28() {
		return attributeValueChars28;
	}

	public void setAttributeValueChars28(Set<AttributeValueChar> attributeValueChars28) {
		this.attributeValueChars28 = attributeValueChars28;
	}
	
	private Set<AttributeValueChar> attributeValueChars29;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars29() {
		return attributeValueChars29;
	}

	public void setAttributeValueChars29(Set<AttributeValueChar> attributeValueChars29) {
		this.attributeValueChars29 = attributeValueChars29;
	}
	
	private Set<AttributeValueChar> attributeValueChars30;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueChar> getAttributeValueChars30() {
		return attributeValueChars30;
	}

	public void setAttributeValueChars30(Set<AttributeValueChar> attributeValueChars30) {
		this.attributeValueChars30 = attributeValueChars30;
	}
	
	
	private Set<AttributeValueDate> attributeValueDates;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates() {
		return attributeValueDates;
	}

	public void setAttributeValueDates(Set<AttributeValueDate> attributeValueDates) {
		this.attributeValueDates = attributeValueDates;
	}
	
	private Set<AttributeValueDate> attributeValueDates0;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates0() {
		return attributeValueDates0;
	}

	public void setAttributeValueDates0(Set<AttributeValueDate> attributeValueDates0) {
		this.attributeValueDates0 = attributeValueDates0;
	}
	
	private Set<AttributeValueDate> attributeValueDates1;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates1() {
		return attributeValueDates1;
	}

	public void setAttributeValueDates1(Set<AttributeValueDate> attributeValueDates1) {
		this.attributeValueDates1 = attributeValueDates1;
	}
	
	private Set<AttributeValueDate> attributeValueDates2;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates2() {
		return attributeValueDates2;
	}

	public void setAttributeValueDates2(Set<AttributeValueDate> attributeValueDates2) {
		this.attributeValueDates2 = attributeValueDates2;
	}
	
	
	private Set<AttributeValueDate> attributeValueDates3;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates3() {
		return attributeValueDates3;
	}

	public void setAttributeValueDates3(Set<AttributeValueDate> attributeValueDates3) {
		this.attributeValueDates3 = attributeValueDates3;
	}
	
	private Set<AttributeValueDate> attributeValueDates4;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates4() {
		return attributeValueDates4;
	}

	public void setAttributeValueDates4(Set<AttributeValueDate> attributeValueDates4) {
		this.attributeValueDates4 = attributeValueDates4;
	}
	
	private Set<AttributeValueDate> attributeValueDates5;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates5() {
		return attributeValueDates5;
	}

	public void setAttributeValueDates5(Set<AttributeValueDate> attributeValueDates5) {
		this.attributeValueDates5 = attributeValueDates5;
	}
	
	private Set<AttributeValueDate> attributeValueDates6;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates6() {
		return attributeValueDates6;
	}

	public void setAttributeValueDates6(Set<AttributeValueDate> attributeValueDates6) {
		this.attributeValueDates6 = attributeValueDates6;
	}
	
	private Set<AttributeValueDate> attributeValueDates7;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates7() {
		return attributeValueDates7;
	}

	public void setAttributeValueDates7(Set<AttributeValueDate> attributeValueDates7) {
		this.attributeValueDates7 = attributeValueDates7;
	}
	
	private Set<AttributeValueDate> attributeValueDates8;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates8() {
		return attributeValueDates8;
	}

	public void setAttributeValueDates8(Set<AttributeValueDate> attributeValueDates8) {
		this.attributeValueDates8 = attributeValueDates8;
	}
	
	private Set<AttributeValueDate> attributeValueDates9;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates9() {
		return attributeValueDates9;
	}

	public void setAttributeValueDates9(Set<AttributeValueDate> attributeValueDates9) {
		this.attributeValueDates9 = attributeValueDates9;
	}
	
	private Set<AttributeValueDate> attributeValueDates10;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates10() {
		return attributeValueDates10;
	}

	public void setAttributeValueDates10(Set<AttributeValueDate> attributeValueDates10) {
		this.attributeValueDates10 = attributeValueDates10;
	}
	
	private Set<AttributeValueDate> attributeValueDates11;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates11() {
		return attributeValueDates11;
	}

	public void setAttributeValueDates11(Set<AttributeValueDate> attributeValueDates11) {
		this.attributeValueDates11 = attributeValueDates11;
	}
	
	private Set<AttributeValueDate> attributeValueDates12;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates12() {
		return attributeValueDates12;
	}

	public void setAttributeValueDates12(Set<AttributeValueDate> attributeValueDates12) {
		this.attributeValueDates12 = attributeValueDates12;
	}
	
	private Set<AttributeValueDate> attributeValueDates13;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates13() {
		return attributeValueDates13;
	}

	public void setAttributeValueDates13(Set<AttributeValueDate> attributeValueDates13) {
		this.attributeValueDates13 = attributeValueDates13;
	}
	
	private Set<AttributeValueDate> attributeValueDates14;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates14() {
		return attributeValueDates14;
	}

	public void setAttributeValueDates14(Set<AttributeValueDate> attributeValueDates14) {
		this.attributeValueDates14 = attributeValueDates14;
	}
	
	private Set<AttributeValueDate> attributeValueDates15;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates15() {
		return attributeValueDates15;
	}

	public void setAttributeValueDates15(Set<AttributeValueDate> attributeValueDates15) {
		this.attributeValueDates15 = attributeValueDates15;
	}
	
	private Set<AttributeValueDate> attributeValueDates16;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates16() {
		return attributeValueDates16;
	}

	public void setAttributeValueDates16(Set<AttributeValueDate> attributeValueDates16) {
		this.attributeValueDates16 = attributeValueDates16;
	}
	
	private Set<AttributeValueDate> attributeValueDates17;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates17() {
		return attributeValueDates17;
	}

	public void setAttributeValueDates17(Set<AttributeValueDate> attributeValueDates17) {
		this.attributeValueDates17 = attributeValueDates17;
	}
	
	private Set<AttributeValueDate> attributeValueDates18;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates18() {
		return attributeValueDates18;
	}

	public void setAttributeValueDates18(Set<AttributeValueDate> attributeValueDates18) {
		this.attributeValueDates18 = attributeValueDates18;
	}
	
	private Set<AttributeValueDate> attributeValueDates19;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates19() {
		return attributeValueDates19;
	}

	public void setAttributeValueDates19(Set<AttributeValueDate> attributeValueDates19) {
		this.attributeValueDates19 = attributeValueDates19;
	}
	
	private Set<AttributeValueDate> attributeValueDates20;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates20() {
		return attributeValueDates20;
	}

	public void setAttributeValueDates20(Set<AttributeValueDate> attributeValueDates20) {
		this.attributeValueDates20 = attributeValueDates20;
	}
	
	private Set<AttributeValueDate> attributeValueDates21;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates21() {
		return attributeValueDates21;
	}

	public void setAttributeValueDates21(Set<AttributeValueDate> attributeValueDates21) {
		this.attributeValueDates21 = attributeValueDates21;
	}
	
	private Set<AttributeValueDate> attributeValueDates22;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates22() {
		return attributeValueDates22;
	}

	public void setAttributeValueDates22(Set<AttributeValueDate> attributeValueDates22) {
		this.attributeValueDates22 = attributeValueDates22;
	}
	
	private Set<AttributeValueDate> attributeValueDates23;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates23() {
		return attributeValueDates23;
	}

	public void setAttributeValueDates23(Set<AttributeValueDate> attributeValueDates23) {
		this.attributeValueDates23 = attributeValueDates23;
	}
	
	private Set<AttributeValueDate> attributeValueDates24;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates24() {
		return attributeValueDates24;
	}

	public void setAttributeValueDates24(Set<AttributeValueDate> attributeValueDates24) {
		this.attributeValueDates24 = attributeValueDates24;
	}
	
	private Set<AttributeValueDate> attributeValueDates25;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates25() {
		return attributeValueDates25;
	}

	public void setAttributeValueDates25(Set<AttributeValueDate> attributeValueDates25) {
		this.attributeValueDates25 = attributeValueDates25;
	}
	
	private Set<AttributeValueDate> attributeValueDates26;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates26() {
		return attributeValueDates26;
	}

	public void setAttributeValueDates26(Set<AttributeValueDate> attributeValueDates26) {
		this.attributeValueDates26 = attributeValueDates26;
	}
	
	private Set<AttributeValueDate> attributeValueDates27;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates27() {
		return attributeValueDates27;
	}

	public void setAttributeValueDates27(Set<AttributeValueDate> attributeValueDates27) {
		this.attributeValueDates27 = attributeValueDates27;
	}
	
	private Set<AttributeValueDate> attributeValueDates28;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates28() {
		return attributeValueDates28;
	}

	public void setAttributeValueDates28(Set<AttributeValueDate> attributeValueDates28) {
		this.attributeValueDates28 = attributeValueDates28;
	}
	
	private Set<AttributeValueDate> attributeValueDates29;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates29() {
		return attributeValueDates29;
	}

	public void setAttributeValueDates29(Set<AttributeValueDate> attributeValueDates29) {
		this.attributeValueDates29 = attributeValueDates29;
	}
	
	private Set<AttributeValueDate> attributeValueDates30;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueDate> getAttributeValueDates30() {
		return attributeValueDates30;
	}

	public void setAttributeValueDates30(Set<AttributeValueDate> attributeValueDates30) {
		this.attributeValueDates30 = attributeValueDates30;
	}
	
	
	private Set<AttributeValueNumber> attributeValueNumbers;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers() {
		return attributeValueNumbers;
	}

	public void setAttributeValueNumbers(Set<AttributeValueNumber> attributeValueNumbers) {
		this.attributeValueNumbers = attributeValueNumbers;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers0;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers0() {
		return attributeValueNumbers0;
	}

	public void setAttributeValueNumbers0(Set<AttributeValueNumber> attributeValueNumbers0) {
		this.attributeValueNumbers0 = attributeValueNumbers0;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers1;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers1() {
		return attributeValueNumbers1;
	}

	public void setAttributeValueNumbers1(Set<AttributeValueNumber> attributeValueNumbers1) {
		this.attributeValueNumbers1 = attributeValueNumbers1;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers2;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers2() {
		return attributeValueNumbers2;
	}

	public void setAttributeValueNumbers2(Set<AttributeValueNumber> attributeValueNumbers2) {
		this.attributeValueNumbers2 = attributeValueNumbers2;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers3;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers3() {
		return attributeValueNumbers3;
	}

	public void setAttributeValueNumbers3(Set<AttributeValueNumber> attributeValueNumbers3) {
		this.attributeValueNumbers3 = attributeValueNumbers3;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers4;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers4() {
		return attributeValueNumbers4;
	}

	public void setAttributeValueNumbers4(Set<AttributeValueNumber> attributeValueNumbers4) {
		this.attributeValueNumbers4 = attributeValueNumbers4;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers5;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers5() {
		return attributeValueNumbers5;
	}

	public void setAttributeValueNumbers5(Set<AttributeValueNumber> attributeValueNumbers5) {
		this.attributeValueNumbers5 = attributeValueNumbers5;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers6;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers6() {
		return attributeValueNumbers6;
	}

	public void setAttributeValueNumbers6(Set<AttributeValueNumber> attributeValueNumbers6) {
		this.attributeValueNumbers6 = attributeValueNumbers6;
	}
	private Set<AttributeValueNumber> attributeValueNumbers7;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers7() {
		return attributeValueNumbers7;
	}

	public void setAttributeValueNumbers7(Set<AttributeValueNumber> attributeValueNumbers7) {
		this.attributeValueNumbers7 = attributeValueNumbers7;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers8;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers8() {
		return attributeValueNumbers8;
	}

	public void setAttributeValueNumbers8(Set<AttributeValueNumber> attributeValueNumbers8) {
		this.attributeValueNumbers8 = attributeValueNumbers8;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers9;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers9() {
		return attributeValueNumbers9;
	}

	public void setAttributeValueNumbers9(Set<AttributeValueNumber> attributeValueNumbers9) {
		this.attributeValueNumbers9 = attributeValueNumbers9;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers10;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers10() {
		return attributeValueNumbers10;
	}

	public void setAttributeValueNumbers10(Set<AttributeValueNumber> attributeValueNumbers10) {
		this.attributeValueNumbers10 = attributeValueNumbers10;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers11;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers11() {
		return attributeValueNumbers11;
	}

	public void setAttributeValueNumbers11(Set<AttributeValueNumber> attributeValueNumbers11) {
		this.attributeValueNumbers11 = attributeValueNumbers11;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers12;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers12() {
		return attributeValueNumbers12;
	}

	public void setAttributeValueNumbers12(Set<AttributeValueNumber> attributeValueNumbers12) {
		this.attributeValueNumbers12 = attributeValueNumbers12;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers13;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers13() {
		return attributeValueNumbers13;
	}

	public void setAttributeValueNumbers13(Set<AttributeValueNumber> attributeValueNumbers13) {
		this.attributeValueNumbers13 = attributeValueNumbers13;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers14;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers14() {
		return attributeValueNumbers14;
	}

	public void setAttributeValueNumbers14(Set<AttributeValueNumber> attributeValueNumbers14) {
		this.attributeValueNumbers14 = attributeValueNumbers14;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers15;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers15() {
		return attributeValueNumbers15;
	}

	public void setAttributeValueNumbers15(Set<AttributeValueNumber> attributeValueNumbers15) {
		this.attributeValueNumbers15 = attributeValueNumbers15;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers16;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers16() {
		return attributeValueNumbers16;
	}

	public void setAttributeValueNumbers16(Set<AttributeValueNumber> attributeValueNumbers16) {
		this.attributeValueNumbers16 = attributeValueNumbers16;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers17;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers17() {
		return attributeValueNumbers17;
	}

	public void setAttributeValueNumbers17(Set<AttributeValueNumber> attributeValueNumbers17) {
		this.attributeValueNumbers17 = attributeValueNumbers17;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers18;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers18() {
		return attributeValueNumbers18;
	}

	public void setAttributeValueNumbers18(Set<AttributeValueNumber> attributeValueNumbers18) {
		this.attributeValueNumbers18 = attributeValueNumbers18;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers19;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers19() {
		return attributeValueNumbers19;
	}

	public void setAttributeValueNumbers19(Set<AttributeValueNumber> attributeValueNumbers19) {
		this.attributeValueNumbers19 = attributeValueNumbers19;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers20;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers20() {
		return attributeValueNumbers20;
	}

	public void setAttributeValueNumbers20(Set<AttributeValueNumber> attributeValueNumbers20) {
		this.attributeValueNumbers20 = attributeValueNumbers20;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers21;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers21() {
		return attributeValueNumbers21;
	}

	public void setAttributeValueNumbers21(Set<AttributeValueNumber> attributeValueNumbers21) {
		this.attributeValueNumbers21 = attributeValueNumbers21;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers22;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers22() {
		return attributeValueNumbers22;
	}

	public void setAttributeValueNumbers22(Set<AttributeValueNumber> attributeValueNumbers22) {
		this.attributeValueNumbers22 = attributeValueNumbers22;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers23;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers23() {
		return attributeValueNumbers23;
	}

	public void setAttributeValueNumbers23(Set<AttributeValueNumber> attributeValueNumbers23) {
		this.attributeValueNumbers23 = attributeValueNumbers23;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers24;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers24() {
		return attributeValueNumbers24;
	}

	public void setAttributeValueNumbers24(Set<AttributeValueNumber> attributeValueNumbers24) {
		this.attributeValueNumbers24 = attributeValueNumbers24;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers25;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers25() {
		return attributeValueNumbers25;
	}

	public void setAttributeValueNumbers25(Set<AttributeValueNumber> attributeValueNumbers25) {
		this.attributeValueNumbers25 = attributeValueNumbers25;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers26;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers26() {
		return attributeValueNumbers26;
	}

	public void setAttributeValueNumbers26(Set<AttributeValueNumber> attributeValueNumbers26) {
		this.attributeValueNumbers26 = attributeValueNumbers26;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers27;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers27() {
		return attributeValueNumbers27;
	}

	public void setAttributeValueNumbers27(Set<AttributeValueNumber> attributeValueNumbers27) {
		this.attributeValueNumbers27 = attributeValueNumbers27;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers28;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers28() {
		return attributeValueNumbers28;
	}

	public void setAttributeValueNumbers28(Set<AttributeValueNumber> attributeValueNumbers28) {
		this.attributeValueNumbers28 = attributeValueNumbers28;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers29;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers29() {
		return attributeValueNumbers29;
	}

	public void setAttributeValueNumbers29(Set<AttributeValueNumber> attributeValueNumbers29) {
		this.attributeValueNumbers29 = attributeValueNumbers29;
	}
	
	private Set<AttributeValueNumber> attributeValueNumbers30;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityId", referencedColumnName = "entityId", insertable = false, updatable = false)
	public Set<AttributeValueNumber> getAttributeValueNumbers30() {
		return attributeValueNumbers30;
	}

	public void setAttributeValueNumbers30(Set<AttributeValueNumber> attributeValueNumbers30) {
		this.attributeValueNumbers30 = attributeValueNumbers30;
	}
	
	
	
}

// $Id$