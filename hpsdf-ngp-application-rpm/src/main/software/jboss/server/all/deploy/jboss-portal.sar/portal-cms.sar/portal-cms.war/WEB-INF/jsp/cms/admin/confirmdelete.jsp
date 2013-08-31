<%@ page import="org.jboss.portal.cms.util.NodeUtil" %>
<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="org.jboss.portal.common.text.EntityEncoder" %>

<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
   <h3 class="sectionTitle-blue">${n:i18n("TITLE_DELETECONFIRM")}</h3>
   <div class=" cms-tab-container">
      <%
         String sCurrPath = (String)request.getAttribute("currpath");
         String sBackPath = NodeUtil.getParentPath(sCurrPath);
      %>

      <form action="<portlet:actionURL>
 <portlet:param name="op" value="<%= CMSAdminConstants.OP_DELETE %>"/>
 <portlet:param name="path" value="<%= sCurrPath %>"/>
 </portlet:actionURL>" method="post">
         <table width="100%">

            <tr>
               <td align="center">
                  <font class="portlet-font">${n:i18n("CMS_DELETEPATH")} <%= EntityEncoder.FULL.encode(sCurrPath) %>
                  </font>
                  <br><br>
                  <font class="portlet-font" style="color:red"><b>${n:i18n("CMS_DELETEWARN1")}</b></font>
                  <br><br>
                  <font class="portlet-font">${n:i18n("CMS_DELETEWARN2")}</font><br>
               </td>
            </tr>
            <tr>
               <td align="center"><input type="submit" value="${n:i18n("CMS_DELETE")}" name="submit"
                                         class="portlet-form-button">
                  <input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}" name="cancel"
                         onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="<%= sBackPath %>"/></portlet:renderURL>'">
               </td>
            </tr>
         </table>
      </form>
   </div>
</div>