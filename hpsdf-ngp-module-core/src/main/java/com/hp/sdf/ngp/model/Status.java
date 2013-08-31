package com.hp.sdf.ngp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Status entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class Status  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8263943031966018118L;
	private Long id;
	private String status;
	private Long type;
	private String displayName;
	

	public Status(Long id, String status) {
		this.id = id;
		this.status = status;
	}
	
	public Status(Long id){
		this.id=id;
	}
	
	public Status(String status) {
		this.status = status;
	}
	
	public Status() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 30,unique=true)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Column
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
}