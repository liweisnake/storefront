<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>

<h2>CMS Portlet - Help</h2>

<h2>${n:i18n("CMS_INTRODUCTION")}</h2>

<p class="portlet-font">${n:i18n("CMS_HELP")}.</p>

<p class="portlet-font">${n:i18n("CMS_TO_MODIFY")}
   <a href="<portlet:renderURL portletMode="edit"></portlet:renderURL>">${n:i18n("CMS_CLICK_HERE")}.</a></p>
