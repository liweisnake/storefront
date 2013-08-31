package com.hp.sdf.ngp.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * ApplicationSubscription entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class PurchaseHistory  implements java.io.Serializable {

	// Fields
                                            
	/**
	 * 
	 */
	private static final long serialVersionUID = 952227978930863790L;
	private Long id;
	private String userid;
	private BigDecimal paidPrice;
	private Asset asset;
	private Date purchaseDate=new Date();
	private String status;
	private String transactionid;
	private String currency;
	
	
	public PurchaseHistory(){
		
	}

	public PurchaseHistory(Long id) {
		super();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETID", nullable = false)
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	@Column
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column
	public BigDecimal getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(BigDecimal paidPrice) {
		this.paidPrice = paidPrice;
	}

	@Column
	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	
	

}