<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
   <h3 class="sectionTitle-blue">${n:i18n("TITLE_UPLOAD")}</h3>
   <div class=" cms-tab-container">
      <%
         String sCurrPath = (String)request.getAttribute("currpath");
         String sLanguage = (String)request.getAttribute("language");
      %>

      <form name="pickform" method="post" enctype="multipart/form-data" action="<portlet:actionURL>
    <portlet:param name="op" value="<%= CMSAdminConstants.OP_EDIT_BINARY %>"/>
    </portlet:actionURL>">
         <input type="hidden" name="destination" value="<%= sCurrPath %>">
         <input type="hidden" name="language" value="<%= sLanguage %>">
         <table width="100%">

            <tr>
               <td align="left">
                  <table>
                     <tr>
                        <td valign="top" width="150">
                           ${n:i18n("CMS_EDITING")}:
                        </td>
                        <td valign="top"><%= sCurrPath %>
                        </td>
                     </tr>
                     <tr>
                        <td valign="top">
                           ${n:i18n("CMS_DESCRIPTION")}:
                        </td>
                        <td valign="top"><input type="text" size="40" maxlength="80" name="description" value=""
                                                class="portlet-form-input-field"/></td>
                     </tr>
                     <tr>
                        <td valign="top">
                           ${n:i18n("CMS_TITLE")}:
                        </td>
                        <td valign="top"><input type="text" size="40" maxlength="80" name="title" value=""
                                                class="portlet-form-input-field"/></td>
                     </tr>
                     <tr>
                        <td valign="top">${n:i18n("CMS_LIVE")}:</td>
                        <td valign="top"><input type="checkbox" name="makelive" checked
                                                class="portlet-form-input-field"/></td>
                     </tr>
                     <tr>
                        <td colspan="2">
                           <input type="file" size="32" name="response" value="" class="portlet-form-input-field"/>
                           <br>
                           <input type="submit" name="submit" value="${n:i18n("CMS_UPLOAD")}"
                                  class="portlet-form-button"/>
                           <input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}"
                                  name="cancel"
                                  onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_VIEWFILE %>"/><portlet:param name="path" value="<%= sCurrPath %>"/></portlet:renderURL>'">
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
      </form>

   </div>
</div>