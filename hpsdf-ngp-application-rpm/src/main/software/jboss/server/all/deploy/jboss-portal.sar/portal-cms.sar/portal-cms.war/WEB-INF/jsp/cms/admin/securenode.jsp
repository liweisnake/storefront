<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="org.jboss.portal.common.text.EntityEncoder" %>
<%@ page import="org.jboss.portal.identity.Role" %>
<%@ page import="org.jboss.portal.identity.User" %>
<%@ page import="org.jboss.portal.cms.security.AuthorizationManager" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Set" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>
<%
   String sCurrPath = (String)request.getAttribute("currpath");
   Set roleSet = (Set)request.getAttribute("roles");
   Set userSet = (Set)request.getAttribute("users");
   String sConfirm = (String)request.getAttribute("confirm");
   String returnOp = (String)request.getAttribute("returnOp");

   Set readRoleSet = (Set)request.getAttribute("readRoleSet");
   Set readUserSet = (Set)request.getAttribute("readUserSet");
   Set writeRoleSet = (Set)request.getAttribute("writeRoleSet");
   Set writeUserSet = (Set)request.getAttribute("writeUserSet");
   Set manageRoleSet = (Set)request.getAttribute("manageRoleSet");
   Set manageUserSet = (Set)request.getAttribute("manageUserSet");
%>
<div class="admin-ui">
<br/>
<h3 class="sectionTitle-blue">${n:i18n("TITLE_SECURECONFIRM")}</h3>
<div class=" cms-tab-container">
<form action="<portlet:actionURL>
 <portlet:param name="op" value="<%= CMSAdminConstants.OP_SECURE %>"/>
 <portlet:param name="path" value="<%= sCurrPath %>"/>
 <portlet:param name="returnOp" value="<%= returnOp %>"/>
 </portlet:actionURL>" method="post">
<table width="100%">
<%
   if (sConfirm != null && !"".equals(sConfirm))
   {
%>
<tr>
   <td colspan="2">
      <font color="red"><%= sConfirm %>
      </font>
   </td>
</tr>
<%
   }
%>
<tr>
   <td colspan="2">
      <font class="portlet-font">${n:i18n("CMS_CONFIGURE_RESTRICTION")}: <%= EntityEncoder.FULL.encode(sCurrPath) %>
      </font>
   </td>
</tr>
<tr>
   <td colspan="2" height="10"></td>
</tr>
<tr>
   <td class="portlet-section-alternate" colspan="2">
      <table>
         <tr>
            <td colspan="2" class="portlet-section-header" align="center">${n:i18n("CMS_ADMIN_SET_READ_PERMISSIONS")}</td>
         </tr>
         <tr>
            <td>
               <select name="secureroles:read" multiple="multiple">
                  <option value="<%=AuthorizationManager.Anonymous%>"
                          <%if(readRoleSet.contains(AuthorizationManager.Anonymous)){%>selected<%}%>>
                     Anonymous
                  </option>
                  <%
                     Iterator iterator = roleSet.iterator();
                     while (iterator.hasNext())
                     {
                        Role role = (Role)iterator.next();
                  %>
                  <option value="<%= role.getName() %>" <%if(readRoleSet.contains(role.getName())){%>selected<%}%>>
                     <%= EntityEncoder.FULL.encode(role.getDisplayName()) %>
                  </option>
                  <%
                     }
                  %>
               </select>
            </td>
            <td>${n:i18n("CMS_ADMIN_SELECT_ROLES")}<br/>
               (${n:i18n("CMS_ADMIN_YOU_CAN_SELECT_CTRL_ROLES")})
            </td>
         </tr>
         <tr>
            <td colspan="2" height="10"></td>
         </tr>
         <tr>
            <td>
               <select name="secureusers:read" multiple="multiple">
                  <%
                     Iterator iteratorUser = userSet.iterator();
                     while (iteratorUser.hasNext())
                     {
                        User user = (User)iteratorUser.next();
                  %>
                  <option value="<%= user.getUserName() %>"
                          <%if(readUserSet.contains(user.getUserName())){%>selected<%}%>>
                     <%= EntityEncoder.FULL.encode(user.getUserName()) %>
                  </option>
                  <%
                     }
                  %>
               </select>
            </td>
            <td>${n:i18n("CMS_ADMIN_SELECT_USERS")}<br/>
               (${n:i18n("CMS_ADMIN_YOU_CAN_SELECT_CTRL_USERS")})
            </td>
         </tr>
      </table>
   </td>
</tr>
<tr>
   <td colspan="2" height="10"></td>
</tr>
<tr>
   <td class="portlet-section-alternate" colspan="2">
      <table>
         <tr>
            <td colspan="2" class="portlet-section-header" align="center">${n:i18n("CMS_ADMIN_SET_WRITE_PERMISSIONS")}</td>
         </tr>
         <tr>
            <td>
               <select name="secureroles:write" multiple="multiple">
                  <option value="<%=AuthorizationManager.Anonymous%>"
                          <%if(writeRoleSet.contains(AuthorizationManager.Anonymous)){%>selected<%}%>>
                     Anonymous
                  </option>
                  <%
                     iterator = roleSet.iterator();
                     while (iterator.hasNext())
                     {
                        Role role = (Role)iterator.next();
                  %>
                  <option value="<%= role.getName() %>" <%if(writeRoleSet.contains(role.getName())){%>selected<%}%>>
                     <%= EntityEncoder.FULL.encode(role.getDisplayName()) %>
                  </option>
                  <%
                     }
                  %>
               </select>
            </td>
            <td>${n:i18n("CMS_ADMIN_SELECT_ROLES")}<br/>
               (${n:i18n("CMS_ADMIN_YOU_CAN_SELECT_CTRL_ROLES")})
            </td>
         </tr>
         <tr>
            <td colspan="2" height="10"></td>
         </tr>
         <tr>
            <td>
               <select name="secureusers:write" multiple="multiple">
                  <%
                     iteratorUser = userSet.iterator();
                     while (iteratorUser.hasNext())
                     {
                        User user = (User)iteratorUser.next();
                  %>
                  <option value="<%= user.getUserName() %>"
                          <%if(writeUserSet.contains(user.getUserName())){%>selected<%}%>>
                     <%= EntityEncoder.FULL.encode(user.getUserName()) %>
                  </option>
                  <%
                     }
                  %>
               </select>
            </td>
            <td>${n:i18n("CMS_ADMIN_SELECT_USERS")}<br/>
               (${n:i18n("CMS_ADMIN_YOU_CAN_SELECT_CTRL_USERS")})
            </td>
         </tr>
      </table>
   </td>
</tr>
<tr>
   <td colspan="2" height="10"></td>
</tr>
<tr>
   <td class="portlet-section-alternate" colspan="2">
      <table>
         <tr>
            <td colspan="2" class="portlet-section-header" align="center">${n:i18n("CMS_ADMIN_SET_MANAGE_PERMISSIONS")}</td>
         </tr>
         <tr>
            <td>
               <select name="secureroles:manage" multiple="multiple">
                  <option value="<%=AuthorizationManager.Anonymous%>"
                          <%if(manageRoleSet.contains(AuthorizationManager.Anonymous)){%>selected<%}%>>
                     Anonymous
                  </option>
                  <%
                     iterator = roleSet.iterator();
                     while (iterator.hasNext())
                     {
                        Role role = (Role)iterator.next();
                  %>
                  <option value="<%= role.getName() %>" <%if(manageRoleSet.contains(role.getName())){%>selected<%}%>>
                     <%= EntityEncoder.FULL.encode(role.getDisplayName()) %>
                  </option>
                  <%
                     }
                  %>
               </select>
            </td>
            <td>${n:i18n("CMS_ADMIN_SELECT_ROLES")}<br/>
               (${n:i18n("CMS_ADMIN_YOU_CAN_SELECT_CTRL_ROLES")})
            </td>
         </tr>
         <tr>
            <td colspan="2" height="10"></td>
         </tr>
         <tr>
            <td>
               <select name="secureusers:manage" multiple="multiple">
                  <%
                     iteratorUser = userSet.iterator();
                     while (iteratorUser.hasNext())
                     {
                        User user = (User)iteratorUser.next();
                  %>
                  <option value="<%= user.getUserName() %>"
                          <%if(manageUserSet.contains(user.getUserName())){%>selected<%}%>>
                     <%= EntityEncoder.FULL.encode(user.getUserName()) %>
                  </option>
                  <%
                     }
                  %>
               </select>
            </td>
            <td>${n:i18n("CMS_ADMIN_SELECT_USERS")}<br/>
               (${n:i18n("CMS_ADMIN_YOU_CAN_SELECT_CTRL_USERS")})
            </td>
         </tr>
      </table>
   </td>
</tr>
<tr>
   <td colspan="2" height="10"></td>
</tr>
<tr>
   <td align="center" colspan="2"><input type="submit" value="${n:i18n("CMS_SECURE")}" name="submit"
                                         class="portlet-form-button">
      <input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}" name="cancel"
             onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= returnOp %>"/><portlet:param name="path" value="<%= sCurrPath %>"/></portlet:renderURL>'">
   </td>
</tr>
</table>
</form>
</div>
</div>
