<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
   <h3 class="sectionTitle-blue">${n:i18n("CMS_EXPORTARCHIVE")}</h3>
   <div class=" cms-tab-container">
      <%
         String sCurrPath = (String)request.getAttribute("currpath");
      %>

      <table width="100%">
         <tr>
            <td align="left">
               <table width="100%">
                  <tr>
                     <td align="center">
                        ${n:i18n("CMS_YOUR_EXPORT_0")} <b><%= sCurrPath %>
                     </b> ${n:i18n("CMS_YOUR_EXPORT_1")}.
                     </td>
                  </tr>
                  <tr>
                     <td height="10">&nbsp;</td>
                  </tr>
                  <tr>
                     <td align="center">
                        <a href="<%= request.getContextPath() %>/cmsexport?og" target="_blank"><img
                           src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/export_pickup.gif"
                           alt="${n:i18n("CMS_DELETE")}" border="0"></a><br/>
                        <a href="<%= request.getContextPath() %>/cmsexport?og" target="_blank">${n:i18n("CMS_CLICK_TO_DOWNLOAD")}</a>
                        <br/>
                        <input class="portlet-form-button" type="button" value="${n:i18n("CMS_BACKTOBROWSER")}"
                               name="cancel"
                               onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="<%= sCurrPath %>"/></portlet:renderURL>'">
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
      </table>
   </div>
</div>
