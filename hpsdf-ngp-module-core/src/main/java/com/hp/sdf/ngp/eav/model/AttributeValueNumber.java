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
package com.hp.sdf.ngp.eav.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class AttributeValueNumber extends AttributeValue<Float> {

	private static final long serialVersionUID = 1L; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getAttributeValueID() {
		return super.getAttributeValueID();
	}

	public void setAttributeValueID(Long attributeValueID) {
		super.setAttributeValueID(attributeValueID);
	}
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "attributeID")
	public Attribute getAttribute() {
		return super.getAttribute();
	}

	public void setAttribute(Attribute attribute) {
		super.setAttribute(attribute);
	}
	

	@Column(nullable = false)
	public Float getValue() {
		return super.getValue();
	}

	public void setValue(Float value) {
		super.setValue(value);
	}
	
	@Column(nullable = false)
	public Long getEntityId() {
		return super.getEntityId();
	}

	public void setEntityId(Long entityId) {
		super.setEntityId(entityId);
	}
	
	private Attribute attribute0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute0() {
		return attribute0;
	}

	protected void setAttribute0(Attribute attribute0) {
		this.attribute0 = attribute0;
	}

	private Attribute attribute1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute1() {
		return attribute1;
	}

	protected void setAttribute1(Attribute attribute1) {
		this.attribute1 = attribute1;
	}
	private Attribute attribute2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute2() {
		return attribute2;
	}

	protected void setAttribute2(Attribute attribute2) {
		this.attribute2 = attribute2;
	}
	
	private Attribute attribute3;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute3() {
		return attribute3;
	}

	protected void setAttribute3(Attribute attribute3) {
		this.attribute3 = attribute3;
	}
	private Attribute attribute4;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute4() {
		return attribute4;
	}

	protected void setAttribute4(Attribute attribute4) {
		this.attribute4 = attribute4;
	}
	private Attribute attribute5;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute5() {
		return attribute5;
	}

	protected void setAttribute5(Attribute attribute5) {
		this.attribute5 = attribute5;
	}

	private Attribute attribute6;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute6() {
		return attribute6;
	}

	protected void setAttribute6(Attribute attribute6) {
		this.attribute6 = attribute6;
	}

	private Attribute attribute7;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute7() {
		return attribute7;
	}

	protected void setAttribute7(Attribute attribute7) {
		this.attribute7 = attribute7;
	}

	private Attribute attribute8;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute8() {
		return attribute8;
	}

	protected void setAttribute8(Attribute attribute8) {
		this.attribute8 = attribute8;
	}

	private Attribute attribute9;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute9() {
		return attribute9;
	}

	protected void setAttribute9(Attribute attribute9) {
		this.attribute9 = attribute9;
	}
	
	private Attribute attribute10;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute10() {
		return attribute10;
	}

	protected void setAttribute10(Attribute attribute10) {
		this.attribute10 = attribute10;
	}
	
	private Attribute attribute11;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute11() {
		return attribute11;
	}

	protected void setAttribute11(Attribute attribute11) {
		this.attribute11 = attribute11;
	}
	
	private Attribute attribute12;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute12() {
		return attribute12;
	}

	protected void setAttribute12(Attribute attribute12) {
		this.attribute12 = attribute12;
	}
	
	private Attribute attribute13;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute13() {
		return attribute13;
	}

	protected void setAttribute13(Attribute attribute13) {
		this.attribute13 = attribute13;
	}
	
	private Attribute attribute14;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute14() {
		return attribute14;
	}

	protected void setAttribute14(Attribute attribute14) {
		this.attribute14 = attribute14;
	}
	
	private Attribute attribute15;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute15() {
		return attribute15;
	}

	protected void setAttribute15(Attribute attribute15) {
		this.attribute15 = attribute15;
	}
	
	private Attribute attribute16;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute16() {
		return attribute16;
	}

	protected void setAttribute16(Attribute attribute16) {
		this.attribute16 = attribute16;
	}
	
	private Attribute attribute17;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute17() {
		return attribute17;
	}

	protected void setAttribute17(Attribute attribute17) {
		this.attribute17 = attribute17;
	}
	
	private Attribute attribute18;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute18() {
		return attribute18;
	}

	protected void setAttribute18(Attribute attribute18) {
		this.attribute18 = attribute18;
	}
	
	private Attribute attribute19;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute19() {
		return attribute19;
	}

	protected void setAttribute19(Attribute attribute19) {
		this.attribute19 = attribute19;
	}
	
	private Attribute attribute20;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute20() {
		return attribute20;
	}

	protected void setAttribute20(Attribute attribute20) {
		this.attribute20 = attribute20;
	}
	
	private Attribute attribute21;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute21() {
		return attribute21;
	}

	protected void setAttribute21(Attribute attribute21) {
		this.attribute21 = attribute21;
	}
	
	private Attribute attribute22;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute22() {
		return attribute22;
	}

	protected void setAttribute22(Attribute attribute22) {
		this.attribute22 = attribute22;
	}
	
	private Attribute attribute23;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute23() {
		return attribute23;
	}

	protected void setAttribute23(Attribute attribute23) {
		this.attribute23 = attribute23;
	}
	
	private Attribute attribute24;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute24() {
		return attribute24;
	}

	protected void setAttribute24(Attribute attribute24) {
		this.attribute24 = attribute24;
	}
	
	private Attribute attribute25;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute25() {
		return attribute25;
	}

	protected void setAttribute25(Attribute attribute25) {
		this.attribute25 = attribute25;
	}
	
	private Attribute attribute26;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute26() {
		return attribute26;
	}

	protected void setAttribute26(Attribute attribute26) {
		this.attribute26 = attribute26;
	}
	
	private Attribute attribute27;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute27() {
		return attribute27;
	}

	protected void setAttribute27(Attribute attribute27) {
		this.attribute27 = attribute27;
	}
	
	private Attribute attribute28;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute28() {
		return attribute28;
	}

	protected void setAttribute28(Attribute attribute28) {
		this.attribute28 = attribute28;
	}
	
	private Attribute attribute29;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute29() {
		return attribute29;
	}

	protected void setAttribute29(Attribute attribute29) {
		this.attribute29 = attribute29;
	}	
	
	private Attribute attribute30;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
	protected Attribute getAttribute30() {
		return attribute30;
	}

	protected void setAttribute30(Attribute attribute30) {
		this.attribute30 = attribute30;
	}

}

// $Id$