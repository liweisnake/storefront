package com.hp.sdf.ngp.handmark.model.device;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for device element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;element name="device">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{}model"/>
 *           &lt;element ref="{}manufacturer"/>
 *           &lt;element ref="{}platform"/>
 *           &lt;element ref="{}device_id"/>
 *           &lt;element ref="{}last_updated"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "model", "manufacturer", "platform",
		"deviceId", "lastUpdated" })
@XmlRootElement(name = "device")
public class Device {

	@XmlElement(required = true)
	protected String model;
	@XmlElement(required = true)
	protected String manufacturer;
	@XmlElement(required = true)
	protected String platform;
	@XmlElement(name = "device_id")
	protected short deviceId;
	@XmlElement(name = "last_updated", required = true)
	protected String lastUpdated;

	/**
	 * Gets the value of the model property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the value of the model property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setModel(String value) {
		this.model = value;
	}

	/**
	 * Gets the value of the manufacturer property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Sets the value of the manufacturer property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setManufacturer(String value) {
		this.manufacturer = value;
	}

	/**
	 * Gets the value of the platform property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * Sets the value of the platform property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPlatform(String value) {
		this.platform = value;
	}

	/**
	 * Gets the value of the deviceId property.
	 * 
	 */
	public short getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the value of the deviceId property.
	 * 
	 */
	public void setDeviceId(short value) {
		this.deviceId = value;
	}

	/**
	 * Gets the value of the lastUpdated property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Sets the value of the lastUpdated property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLastUpdated(String value) {
		this.lastUpdated = value;
	}

}
