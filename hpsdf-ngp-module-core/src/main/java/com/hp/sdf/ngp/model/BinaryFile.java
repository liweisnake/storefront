package com.hp.sdf.ngp.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Appkeyrelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class BinaryFile implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 486377427723311396L;
	private Long id;
	private String fileLocation;
	private byte[] fileBinary;

	// Constructors

	/** default constructor */
	public BinaryFile() {
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

	@Column(nullable = false, unique = true)
	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * This field must to set a length large than 16777215 so that in MySQL
	 * dialect, it will be mapped to longblob instead of blob
	 * 
	 * @return
	 */
	@Column(nullable = false, length = 26777215)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] getFileBinary() {
		return fileBinary;
	}

	public void setFileBinary(byte[] fileBinary) {
		this.fileBinary = fileBinary;
	}

}