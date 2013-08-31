<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<n:iterate ctx="row">A<n:iterate ctx="col">B<n:iterate ctx="foo">C</n:iterate></n:iterate>D</n:iterate>