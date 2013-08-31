<%@ page import="org.jboss.portal.server.PortalConstants" %>
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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>

<table border="0" cellspacing="2" cellpadding="2">
   <tr>
      <td class="portlet-section-alternate">
         <p class="portlet-font">
            Ceci est l'installation par d&eacute;faut de <strong><%= PortalConstants.VERSION %></strong>. Vous pouvez
            vous connecter en cliquant sur le lien <span style="font-style:italic;">Login</span>
            en haut &agrave; droite de cette page, avec les identifiants suivants:
         </p>
      </td>
   </tr>
   <tr>
      <td class="portlet-section-alternate" align="center">
         <strong>user/user</strong> ou <strong>admin/admin</strong>
      </td>
   </tr>
   <tr>
      <td align="center">
         Si vous avez besoin d'aide pour naviguer, configurer ou pour administrer le portail, veuillez vous
         r&eacute;f&eacute;rer
         <a href="http://www.redhat.com/docs" target="_blank">&agrave; la documentation </a>.
      </td>
   </tr>
</table>
