package com.hp.sdf.ngp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Userprofile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class SubscriberProfile extends BaseEAVModel implements java.io.Serializable {
	private static final long serialVersionUID = -3337778931953336702L;

	private String userId;
	private String displayName;
	private String msisdn;

	private Long clientTesterFlag;
	private Long clientOwnerProviderId;
	private String ownerTesterUserId;

	@Id
	@Column(length = 255)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column
	public String getOwnerTesterUserId() {
		return ownerTesterUserId;
	}

	public void setOwnerTesterUserId(String ownerTesterUserId) {
		this.ownerTesterUserId = ownerTesterUserId;
	}

	@Column(nullable = false)
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Column
	public Long getClientTesterFlag() {
		return clientTesterFlag;
	}

	public void setClientTesterFlag(Long clientTesterFlag) {
		this.clientTesterFlag = clientTesterFlag;
	}

	@Column
	public Long getClientOwnerProviderId() {
		return clientOwnerProviderId;
	}

	public void setClientOwnerProviderId(Long clientOwnerProviderId) {
		this.clientOwnerProviderId = clientOwnerProviderId;
	}


}