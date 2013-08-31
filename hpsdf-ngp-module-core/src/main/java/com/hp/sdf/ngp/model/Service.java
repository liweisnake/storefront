package com.hp.sdf.ngp.model;

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


/**
 * Service entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class Service implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3914896910410711913L;
	public static String SERVICE_TYPE_SGF = "SGF";
	public static String SERVICE_TYPE_COMMON = "COMMON";
	
	public static String ACCESS_INTERFACE_HTTP = "httpService";
	public static String ACCESS_INTERFACE_WEBSERVICE = "webservice";

	// Fields
	private Long id;
	private String name;
	private String description;
	private String sdkUrl;
	private String docUrl;
	private String serviceid;
	private String brokerServiceName;
	private String accessInterface;
	private String brokerServiceAuthType;
	private String brokerServiceUrl;
	private String type;

	private Set<ServiceSubscription> serviceSubscriptions = new HashSet<ServiceSubscription>(
			0);
	private Set<Operation> operations = new HashSet<Operation>(0);
	
	public Service(){
		
	}
	
	public Service(Long id) {
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

	@Column(nullable = false,unique = true, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(length = 512)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(length = 512)
	public String getSdkUrl() {
		return this.sdkUrl;
	}

	public void setSdkUrl(String sdkurl) {
		this.sdkUrl = sdkurl;
	}

	@Column(length = 512)
	public String getDocUrl() {
		return this.docUrl;
	}

	public void setDocUrl(String docurl) {
		this.docUrl = docurl;
	}

	@Column(length = 256)
	public String getServiceid() {
		return this.serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	@Column
	public String getBrokerServiceName() {
		return this.brokerServiceName;
	}

	public void setBrokerServiceName(String brokerservicename) {
		this.brokerServiceName = brokerservicename;
	}

	@Column
	public String getAccessInterface() {
		return this.accessInterface;
	}

	public void setAccessInterface(String accessinterface) {
		this.accessInterface = accessinterface;
	}

	@Column
	public String getBrokerServiceAuthType() {
		return this.brokerServiceAuthType;
	}

	public void setBrokerServiceAuthType(String brokerserviceauthtype) {
		this.brokerServiceAuthType = brokerserviceauthtype;
	}

	@Column(length = 2048)
	public String getBrokerServiceUrl() {
		return this.brokerServiceUrl;
	}

	public void setBrokerServiceUrl(String brokerserviceurl) {
		this.brokerServiceUrl = brokerserviceurl;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "service")
	public Set<ServiceSubscription> getServiceSubscriptions() {
		return this.serviceSubscriptions;
	}

	public void setServiceSubscriptions(
			Set<ServiceSubscription> servicesubscriptions) {
		this.serviceSubscriptions = servicesubscriptions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "service")
	public Set<Operation> getOperations() {
		return operations;
	}

	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}

}