/*
 * Copyright (c) 2007 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.api.comon.util;

import java.io.Serializable;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.util.naming.NonSerializableFactory;

/**
 * A set of utilities to help with JNDI bindings.
 */
public class JndiUtil {

	/*
	 * The logger object used for this class.
	 */
	private static final Log log = LogFactory.getLog(JndiUtil.class);

	/**
	 * An optional JNDI initial context factory that when specified to a
	 * non-null value, it will be used instead of the default factory provided
	 * by the J2EE server.
	 * <p>
	 * This factory will be explicitly specified only for unit testing purpose.
	 */
	private static String jndiInitialContextFactory = null;

	private static boolean isNonSerializableFactory = true;

	/**
	 * Use the given JNDI factory for JNDI initial context creation.
	 * <p>
	 * This method is only used for unit test purpose and not used when the
	 * class is deployed as part of a J2EE application server which provides the
	 * JNDI factory.
	 * 
	 * @param factory
	 *            The JNDI initial context factory to be used.
	 */
	public static void setJndiFactory(String factory) {
		jndiInitialContextFactory = factory;
	}

	public static void enableNonSerializableFactory(boolean enableOrNot) {
		isNonSerializableFactory = enableOrNot;
	}

	/**
	 * Bind the resource manager instance to the JNDI directory.
	 * <p>
	 * This method will use the full JNDI path provided for the resource
	 * manager, and will create if necessary the individual segments of that
	 * path.
	 * 
	 * @param jndiName
	 *            The full JNDI path at which the resource manager instance will
	 *            be bound in the JNDI directory. JNDI clients can use that path
	 *            to obtain the resource manager instance.
	 * @param obj
	 *            The object to be bound.
	 * @return <b>true</b> if the resource manager was successfully bound to
	 *         JNDI using the provided path; otherwise <b>false</b>.
	 * 
	 * @see #unbind(String)
	 */
	public static boolean bind(String jndiName, Object obj) {
		if (log.isDebugEnabled()) {
			log.debug("Binding object [" + obj + "] in JNDI at path ["
					+ jndiName + "].");
		}
		Context ctx = null;

		try {
			// Create a binding that is local to this host only.
			Hashtable<String, String> ht = new Hashtable<String, String>();
			// If a special JNDI initial context factory was specified in the
			// constructor, then use it.
			if (jndiInitialContextFactory != null) {
				ht.put(Context.INITIAL_CONTEXT_FACTORY,
						jndiInitialContextFactory);
			}
			// Turn off binding replication .
			// ht.put(WLContext.REPLICATE_BINDINGS, "false");
			ctx = new InitialContext(ht);

			String[] arrJndiNames = jndiName.split("/");
			String subContext = "";

			for (int i = 0; i < arrJndiNames.length - 1; i++) {
				subContext = subContext + "/" + arrJndiNames[i];
				try {
					ctx.lookup(subContext);
				} catch (NameNotFoundException e) {
					ctx.createSubcontext(subContext);
				}

			}

			if (!isNonSerializableFactory && (obj instanceof Serializable
					|| jndiInitialContextFactory != null)) {
				ctx.bind(jndiName, obj);
			} else {
				NonSerializableFactory.bind(jndiName, obj);
			}
		} catch (NamingException ex) {
			log.error("An error occured while binding [" + jndiName
					+ "] to JNDI:", ex);
			return false;
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException ne) {
					log.error("Close context error:", ne);
				}
			}
		}
		return true;
	}

	/**
	 * Unbind the rsource manager instance from the JNDI directory.
	 * <p>
	 * After this method is called, it is no longer possible to obtain the
	 * resource manager instance from the JNDI directory.
	 * <p>
	 * Note that this method will not remove the JNDI contexts corresponding to
	 * the individual path segments of the full resource manager JNDI path.
	 * 
	 * @param jndiName
	 *            The full JNDI path at which the resource manager instance was
	 *            previously bound.
	 * @return <b>true</b> if the resource manager was successfully unbound from
	 *         JNDI; otherwise <b>false</b>.
	 * 
	 * @see #bind(String, Object)
	 */
	public static boolean unbind(String jndiName) {
		if (log.isDebugEnabled()) {
			log.debug("Unbinding object at path [" + jndiName
					+ "] from the JNDI repository.");
		}
		Context ctx = null;
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			// If a special JNDI initial context factory was specified in the
			// constructor, then use it.
			if (jndiInitialContextFactory != null) {
				ht.put(Context.INITIAL_CONTEXT_FACTORY,
						jndiInitialContextFactory);
			}
			ctx = new InitialContext(ht);
			Object obj = lookupObject(jndiName);
			if (!isNonSerializableFactory&&(obj instanceof Serializable
					|| jndiInitialContextFactory != null)) {
				ctx.unbind(jndiName);
			} else {
				NonSerializableFactory.unbind(jndiName);
			}
		} catch (NamingException ex) {
			log.error("An error occured while unbinding [" + jndiName
					+ "] from JNDI:", ex);
			return false;
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException ne) {
					log.error("Close context error:", ne);
				}
			}
		}
		return true;
	}

	/**
	 * Look up the object with the specified JNDI name in the JNDI server.
	 * 
	 * @param jndiName
	 *            the JNDI name specified
	 * @return the object bound with the name specified, null if this method
	 *         fails
	 */
	public static Object lookupObject(String jndiName) {
		Context ctx = null;
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			// If a special JNDI initial context factory was specified in the
			// constructor, then use it.
			if (jndiInitialContextFactory != null) {
				ht.put(Context.INITIAL_CONTEXT_FACTORY,
						jndiInitialContextFactory);
			}
			ctx = new InitialContext(ht);
			Object obj = null;
			try {
				obj = ctx.lookup(jndiName);
			} catch (Exception e) {
				if (log.isInfoEnabled()) {
					log
							.info("Lookup for an object from Non-Serializable JNDI (relookup from Serializable JNDI) using the JNDI name ["
									+ jndiName + "] : ");
				}
				obj = NonSerializableFactory.lookup(jndiName);
			}

			if (log.isDebugEnabled()) {
				log.debug("JNDI lookup with path [" + jndiName
						+ "] returned object [" + obj + "].");
			}
			return obj;
		} catch (NamingException ex) {
			log.debug("Failed to lookup for an object using the JNDI name ["
					+ jndiName + "] : " + ex);
			return null;
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException ne) {
					log.error("Close context error:", ne);
				}
			}
		}
	}

}

// $Id: JndiUtil.java 720 2007-11-05 10:08:47Z wei_zhang10 $
