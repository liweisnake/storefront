<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ page import="org.jboss.portal.common.text.EntityEncoder" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<%
   String sCurrPath = (String)request.getAttribute("currpath");
   String OP = CMSAdminConstants.OP_CONFIRMCOPY;
   String sType = (String)request.getAttribute("type");
   String sBackPath = NodeUtil.getParentPath(sCurrPath);
%>
<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
<h3 class="sectionTitle-blue">${n:i18n("TITLE_COPYCONFIRM")}</h3>
   <div class="cms-tab-container">
      <table width="100%">
         <tr>
            <td class="portlet-section-body" align="left">
               <form name="pickform" action="<portlet:actionURL>
    <portlet:param name="op" value="<%= CMSAdminConstants.OP_COPY %>"/>
    <portlet:param name="source" value="<%= sCurrPath %>"/>
    <portlet:param name="type" value="<%= sType %>"/>
    </portlet:actionURL>" method="post">
                  <input type="hidden" name="destination" value="/">
                  <table>
                     <tr>
                        <td>${n:i18n("CMS_SOURCE")}:</td>
                        <td><%= EntityEncoder.FULL.encode(sCurrPath) %>
                        </td>
                     </tr>
                     <tr>
                        <td height="25" valign="bottom">${n:i18n("CMS_DESTINATION")}:</td>
                        <td><input DISABLED type="text" size="40"
                                   name="showdestination" value="/"
                                   class="portlet-form-input-field"/></td>
                     </tr>
                     <tr>
                        <td></td>
                        <td valign="top"><input class="portlet-form-button" type="submit"
                                                value="${n:i18n("CMS_COPY")}"
                                                name="submit">
                           <input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}"
                                  name="cancel"
                                  onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="<%= sBackPath %>"/></portlet:renderURL>'">
                        </td>
                     </tr>
                  </table>
               </form>
            </td>
            <td valign="top" class="portlet-section-alternate">
               <%@ include file="folderlist.jsp" %>
            </td>
         </tr>
      </table>
   </div>
</div>
