<%@ page import="java.util.Locale" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<div class="admin-ui">
   <br/>
   <h3 class="sectionTitle-blue">${n:i18n("CMS_UPLOADARCHIVE")}</h3>
   <div class=" cms-tab-container">
      <%
         String sCurrPath = (String)request.getAttribute("currpath");
         String OP = CMSAdminConstants.OP_UPLOADARCHIVECONFIRM;
         String[] langs = Locale.getISOLanguages();
      %>

      <form name="pickform" method="post" enctype="multipart/form-data" action="<portlet:actionURL>
    <portlet:param name="op" value="<%= CMSAdminConstants.OP_UPLOADARCHIVE %>"/>
    </portlet:actionURL>">
         <input type="hidden" name="destination" value="<%= sCurrPath %>">
         <table width="100%">
            <tr>
               <td valign="top" width="250" class="portlet-section-alternate">
                  <%@ include file="folderlist.jsp" %>
               </td>
               <td align="left">
                  <table>
                     <tr>
                        <td valign="bottom">
                           ${n:i18n("CMS_DESTINATION")}:
                        </td>
                        <td>
                           <input DISABLED type="text" size="40" name="showdestination" value="<%= sCurrPath %>"
                                  class="portlet-form-input-field"/></td>
                     </tr>
                     <tr>
                        <td valign="bottom">
                           ${n:i18n("CMS_LANGUAGE")}:
                        </td>
                        <td><select name="language" class="portlet-form-input-field">
                           <option selected
                                   value="<%= Locale.getDefault().getLanguage() %>"><%= Locale.getDefault().getDisplayLanguage() %>
                           </option>
                           <%
                              for (int i = 0; i < langs.length; i++)
                              {
                           %>
                           <option value="<%= langs[i] %>"><%= new Locale(langs[i]).getDisplayLanguage() %>
                           </option>
                           <%
                              }
                           %>
                        </select>
                        </td>
                     </tr>
                     <tr>
                        <td colspan="2">
                           <input type="file" size="32" name="response" value="" class="portlet-form-input-field"/>
                           <br><br>
                           <input type="submit" name="submit" value="${n:i18n("CMS_UPLOAD")}"
                                  class="portlet-form-button"/>
                           <input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}"
                                  name="cancel"
                                  onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="<%= sCurrPath %>"/></portlet:renderURL>'">
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
      </form>
   </div>
</div>