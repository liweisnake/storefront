<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page isELIgnored="false" %>
${n:out("foo")}:
<n:iterate ctx="row1">
   ${n:out("row1.value1")}:
</n:iterate>
${n:out("bar")}
