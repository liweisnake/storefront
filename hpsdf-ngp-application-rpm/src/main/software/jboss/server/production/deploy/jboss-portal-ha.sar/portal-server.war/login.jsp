<%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~ JBoss, a division of Red Hat                                             ~
  ~ Copyright 2006, Red Hat Middleware, LLC, and individual                  ~
  ~ contributors as indicated by the @authors tag. See the                   ~
  ~ copyright.txt in the distribution for a full listing of                  ~
  ~ individual contributors.                                                 ~
  ~                                                                          ~
  ~ This is free software; you can redistribute it and/or modify it          ~
  ~ under the terms of the GNU Lesser General Public License as              ~
  ~ published by the Free Software Foundation; either version 2.1 of         ~
  ~ the License, or (at your option) any later version.                      ~
  ~                                                                          ~
  ~ This software is distributed in the hope that it will be useful,         ~
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of           ~
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU         ~
  ~ Lesser General Public License for more details.                          ~
  ~                                                                          ~
  ~ You should have received a copy of the GNU Lesser General Public         ~
  ~ License along with this software; if not, write to the Free              ~
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA       ~
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.                 ~
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~--%>
<%@ page pageEncoding="utf-8" %>
<%@page import="org.jboss.portal.identity.UserStatus" %>
<%@page import="org.jboss.portal.server.ParameterSanitizer" %>
<%@page import="java.util.ResourceBundle" %>

<%
   ResourceBundle rb = ResourceBundle.getBundle("Resource", request.getLocale());
   // todo: use ParameterValidation.sanitize after 2.7.1
   String loginheight = request.getParameter("loginheight");
   boolean paramPresent = loginheight != null;
   loginheight = ParameterSanitizer.sanitizeFromPattern(loginheight, ParameterSanitizer.CSS_DISTANCE, "300px");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <title><%= rb.getString("LOGIN_TITLE")  %>
   </title>
   <style type="text/css">
      /* <![CDATA[ */
      body {
         margin: 0;
         padding: 0;
         border: 0;
         padding-top: <%=loginheight%>;
      }

      /* ]]> */
   </style>

   <script>
      function setFocusOnLoginForm()
      {
         try
         {
            document.loginform.j_username.focus();
         }
         catch (e)
         {
         }
      }
   </script>

   <link rel="stylesheet" href="/portal-core/css/login.css" type="text/css"/>
</head>
<body onload="setFocusOnLoginForm();">

<div class="login-container">


   <div class="login-header">
      <h2><%= rb.getString("LOGIN_TITLE") %>
      </h2>
   </div>
   <div class="login-content">

      <div class="error-message"
           style="<%=(request.getAttribute(!UserStatus.OK.equals("org.jboss.portal.userStatus") ? "" : "display:none"))%>;">
         <%

            if (UserStatus.DISABLE.equals(request.getAttribute("org.jboss.portal.userStatus")))
            {
               out.println(rb.getString("ACCOUNT_DISABLED"));
            }
            else if (UserStatus.WRONGPASSWORD.equals(request.getAttribute("org.jboss.portal.userStatus")) || UserStatus.UNEXISTING.equals(request.getAttribute("org.jboss.portal.userStatus")))
            {
               out.println(rb.getString("ACCOUNT_INEXISTING_OR_WRONG_PASSWORD"));
            }
            else if (UserStatus.NOTASSIGNEDTOROLE.equals(request.getAttribute("org.jboss.portal.userStatus")))
            {
               out.println(rb.getString("ACCOUNT_NOTASSIGNEDTOROLE"));
            }
         %>
      </div>
      <form method="post" action="<%= response.encodeURL("j_security_check") %>" name="loginform" id="loginForm"
            target="_parent">
	     <table align="center">
	        <tr class="form-field">
	           <td><label for="j_username" style="white-space: nowrap;"><%= rb.getString("LOGIN_USERNAME") %></label></td>
               <td><input type="text" name="j_username" id="j_username" value="" size="12"/></td>
            </tr>
            <tr class="form-field">
               <td><label for="j_password" style="white-space: nowrap;"><%= rb.getString("LOGIN_PASSWORD") %></label></td>
               <td><input type="password" name="j_password" id="j_password" value="" size="12"/></td>
            </tr>
         </table>
         <div class="button-container">
            <br class="clear"/>
            <input style="<%=paramPresent ? "" : "display:none"%>;" type="button" name="cancel"
                   value="<%= rb.getString("LOGIN_CANCEL")  %>" class="cancel-button"
                   onclick="window.parent.hideContentModal('login-modal');"/>
            <br class="clear"/>
            <input style="<%=paramPresent ? "" : "right:10px"%>;" type="submit" name="login"
                   value="<%= rb.getString("LOGIN_SUBMIT")  %>" class="login-button"/>
         </div>
      </form>
   </div>
   <div style="text-align:center"><%= rb.getString("LOGIN_MSG")  %></div>
</div>
</body>
</html>
