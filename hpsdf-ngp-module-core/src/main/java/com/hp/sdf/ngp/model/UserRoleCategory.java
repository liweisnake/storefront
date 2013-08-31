package com.hp.sdf.ngp.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Userrolecategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class UserRoleCategory  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1042737495095532472L;
	private Long id;
	private RoleCategory roleCategory;
	private UserProfile userProfile;

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLEID", nullable = false)
	public RoleCategory getRoleCategory() {
		return this.roleCategory;
	}

	public void setRoleCategory(RoleCategory roleCategory) {
		this.roleCategory = roleCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	
}