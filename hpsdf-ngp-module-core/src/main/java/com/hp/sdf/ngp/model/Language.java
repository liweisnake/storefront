package com.hp.sdf.ngp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Language entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class Language  implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String locale;

	// Constructors

	/** default constructor */
	public Language() {
	}

	/** minimal constructor */
	public Language(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public Language(Long id, String name, String locale) {
		this.id = id;
		this.name = name;
		this.locale = locale;
	}
	
	public Language(Long id){
		this.id=id;
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

	@Column(nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 20)
	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}