<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>
<%
   String OP = (String)request.getAttribute("returnOp");
   String sBackPath = null;
   if(OP != null)
   {
   		sBackPath = (String)request.getAttribute("path");
   }
   else
   {
   		OP = CMSAdminConstants.OP_MAIN;
   		sBackPath = "/";
   }
%>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
   <h3 class="sectionTitle-blue">${n:i18n("TITLE_SECURECONFIRM")}</h3>
   <div class=" cms-tab-container">
      <table width="100%">

         <tr>
            <td class="portlet-section-body" align="center">
		<h2>${n:i18n("CMS_ACCESS_DENIED")}</h2>
	   </td>
	</tr>
	<%if(sBackPath != null){%>
	<tr>
            <td class="portlet-section-body" align="center">
               <form name="accessdeniedform" method="post">
		         <input class="portlet-form-button" type="button" value="${n:i18n("CMS_BACKTOBROWSER")}"
			  name="back"
			  onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="<%= sBackPath %>"/></portlet:renderURL>'"/>
               </form>
            </td>
     </tr>
     <%}%>
     </table>
     <br/><br/>
   </div>
</div>