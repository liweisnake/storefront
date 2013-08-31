package com.hp.sdf.ngp.model;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Rolecategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class RoleCategory  implements java.io.Serializable {
	


	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6222092881728588873L;
	private Long id;
	private String description;
	private Date createDate=new Date();
	private String roleName;
	private String displayName;
	private Set<UserRoleCategory> userRoleCategorys = new HashSet<UserRoleCategory>(
			0);

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 512)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createdate) {
		this.createDate = createdate;
	}

	@Column(nullable = false, length = 255, unique=true)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "roleCategory")
	public Set<UserRoleCategory> getUserRoleCategorys() {
		return userRoleCategorys;
	}

	public void setUserRoleCategorys(Set<UserRoleCategory> userRoleCategorys) {
		this.userRoleCategorys = userRoleCategorys;
	}
	
	

}