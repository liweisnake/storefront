<%@ page import="java.lang.Throwable" %>
<%@ page import="org.jboss.portal.common.util.Exceptions" %>
<%@ page import="org.jboss.portal.common.text.EntityEncoder" %>


<h2 class="portlet-msg-error"><%= request.getAttribute("org.jboss.portal.control.ERROR_TYPE") %>
</h2>

<%
   if (request.getAttribute("org.jboss.portal.control.CAUSE") != null)
   {
%>
<div class="portlet-font">Cause: <%= EntityEncoder.FULL.encode(request.getAttribute("org.jboss.portal.control.CAUSE").toString()) %>
</div>
<%
   }
   if (request.getAttribute("org.jboss.portal.control.MESSAGE") != null)
   {
%>
<div class="portlet-font">Message: <%= EntityEncoder.FULL.encode(request.getAttribute("org.jboss.portal.control.MESSAGE").toString()) %>
</div>
<%
   }
%>
<div class="portlet-font">See log for stacktrace</div>