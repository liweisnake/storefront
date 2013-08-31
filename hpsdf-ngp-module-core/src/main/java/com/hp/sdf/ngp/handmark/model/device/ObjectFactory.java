package com.hp.sdf.ngp.handmark.model.device;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.hp.sdf.ngp.handmark.model package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _Platform_QNAME = new QName("", "platform");
	private final static QName _Manufacturer_QNAME = new QName("",
			"manufacturer");
	private final static QName _Model_QNAME = new QName("", "model");
	private final static QName _LastUpdated_QNAME = new QName("",
			"last_updated");
	private final static QName _DeviceId_QNAME = new QName("", "device_id");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.hp.sdf.ngp.handmark.model
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Message }
	 * 
	 */
	public Message createMessage() {
		return new Message();
	}

	/**
	 * Create an instance of {@link Device }
	 * 
	 */
	public Device createDevice() {
		return new Device();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "platform")
	public JAXBElement<String> createPlatform(String value) {
		return new JAXBElement<String>(_Platform_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "manufacturer")
	public JAXBElement<String> createManufacturer(String value) {
		return new JAXBElement<String>(_Manufacturer_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "model")
	public JAXBElement<String> createModel(String value) {
		return new JAXBElement<String>(_Model_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "last_updated")
	public JAXBElement<String> createLastUpdated(String value) {
		return new JAXBElement<String>(_LastUpdated_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "device_id")
	public JAXBElement<Short> createDeviceId(Short value) {
		return new JAXBElement<Short>(_DeviceId_QNAME, Short.class, null, value);
	}

}
