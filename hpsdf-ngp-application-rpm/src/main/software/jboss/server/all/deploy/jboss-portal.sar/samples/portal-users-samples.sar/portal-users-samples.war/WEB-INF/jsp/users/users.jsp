<%@ page import="org.jboss.portal.core.CoreConstants" %>
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
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<div class="box">
   <table border="0" class="portlet-font" cellspacing="0" cellpadding="2">
      <tr>
         <td colspan="1">
             <span class="portlet-text">
             <% 
             java.util.LinkedList linkedStack = (java.util.LinkedList)contextStack.get();
             org.jboss.portal.core.servlet.jsp.taglib.context.Context currentContext = 
         	 ((org.jboss.portal.core.servlet.jsp.taglib.context.NamedContext)linkedStack.getLast()).getContext();
             
             if (renderRequest.getPreferences().getValue("guestNumber", "").equals("true")) { 
             			if (Integer.parseInt(currentContext.get("USERS_COUNT")) > 1)
             			{
             			   %>
                ${n:i18n("USERS_ONLINE_0")}
                ${n:out("USERS_COUNT")}
                ${n:i18n("USERS_ONLINE_1")}     
						<%
             			}
             			else
             			{
             			   %>
				${n:i18n("ONE_USER_ONLINE")}
							<%
             			}
             			%>
             <br/><br/> 
             <%
             }
             if (Integer.parseInt(currentContext.get("USERS_COUNT")) > 1 && !currentContext.get("USERS_LOGGED_COUNT").equals("0") && renderRequest.getPreferences().getValue("loggedNumber", "").equals("true")) { %>             

                   ${n:i18n("USERS_WHICH_0")}
                   ${n:out("USERS_LOGGED_COUNT")}
                   <% if (!currentContext.get("USERS_LOGGED_COUNT").equals("1")) { %>  
                      ${n:i18n("USERS_WHICH_1")}
                   <% } else { %>  
                      ${n:i18n("USERS_WHICH_2")} 
                   <% } %>
             <br/><br/>
             <% }
                if (Integer.parseInt(currentContext.get("USERS_COUNT")) > 1 && !currentContext.get("USERS").equals("[]") && renderRequest.getPreferences().getValue("loggedUsers", "").equals("true")) { %>                 

                ${n:i18n("USERS_ARE_0")}
                ${n:out("USERS")}
                ${n:i18n("USERS_ARE_1")}  
             <% } %>
             </span>
         </td>
      </tr>
   </table>
</div>