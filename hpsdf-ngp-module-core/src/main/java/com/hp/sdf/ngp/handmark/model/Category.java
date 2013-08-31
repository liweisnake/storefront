//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-3268 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.04.09 at 03:48:28 ���� CST 
//


package com.hp.sdf.ngp.handmark.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for category element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="category">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://www.handmark.com/master_browse_response}category_id"/>
 *           &lt;element ref="{http://www.handmark.com/master_browse_response}display_name"/>
 *           &lt;element ref="{http://www.handmark.com/master_browse_response}sub_categories" minOccurs="0"/>
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
@XmlType(name = "", propOrder = {
    "categoryId",
    "displayName",
    "subCategories"
})
@XmlRootElement(name = "category")
public class Category {

    @XmlElement(name = "category_id", namespace = "http://www.handmark.com/master_browse_response")
    protected short categoryId;
    @XmlElement(name = "display_name", namespace = "http://www.handmark.com/master_browse_response", required = true)
    protected DisplayName displayName;
    @XmlElement(name = "sub_categories", namespace = "http://www.handmark.com/master_browse_response")
    protected SubCategories subCategories;

    /**
     * Gets the value of the categoryId property.
     * 
     */
    public short getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the value of the categoryId property.
     * 
     */
    public void setCategoryId(short value) {
        this.categoryId = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayName }
     *     
     */
    public DisplayName getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayName }
     *     
     */
    public void setDisplayName(DisplayName value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the subCategories property.
     * 
     * @return
     *     possible object is
     *     {@link SubCategories }
     *     
     */
    public SubCategories getSubCategories() {
        return subCategories;
    }

    /**
     * Sets the value of the subCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubCategories }
     *     
     */
    public void setSubCategories(SubCategories value) {
        this.subCategories = value;
    }

}