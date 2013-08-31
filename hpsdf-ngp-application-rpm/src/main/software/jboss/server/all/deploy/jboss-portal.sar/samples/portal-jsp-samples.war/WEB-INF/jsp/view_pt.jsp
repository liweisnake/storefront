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
         <p class="portlet-font">Esta &eacute; uma instala&ccedil;&atilde;o do <strong><%= PortalConstants.VERSION %></strong>.
            Voc&ecirc; pode fazer o <span style="font-style:italic;">Login</span> a qualquer momento, utilizando o link localizado no canto superior direito desta p&aacute;gina,
            com as seguintes credenciais:
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
         Se voc&ecirc; precisar de orienta&ccedil;&atilde;o no que diz respeito &agrave; navega&ccedil;&atilde;o, configura&ccedil;&atilde;o, ou de como operar o portal, por favor, veja a 
         nossa <a href="http://labs.jboss.com/portal/jbossportal/docs/index.html" target="_blank">documenta&ccedil;&atilde;o online</a>.
      </td>
   </tr>
</table>
