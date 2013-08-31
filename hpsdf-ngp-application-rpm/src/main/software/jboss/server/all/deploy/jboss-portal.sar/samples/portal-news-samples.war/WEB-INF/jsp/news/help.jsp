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

<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:defineObjects/>

<div class="portlet-font" style="font-size:larger;font-weight:bold">News Portlet Help:</div>
</div>
<br/>
<div class="portlet-font">Description: The news Portlet retrieves an RSS news feed from a URL and displays it as HTML to the user.</div>
<br/>
<div class="portlet-font">To change the RSS feed source, simply go to the edit mode after being logged-in and enter the URL of the new RSS feed source such as:
<font style="font-style:italic">http://feeds.feedburner.com/JBossPortal</font>. The new RSS feed source will be stored and used each time the user will be logged-in.</div>
<br/>
<div class="portlet-msg-info">This sample portlet is able to handle most RSS 1.0 and RSS 2.0 feeds</div>
<br/>
<div class="portlet-font"><a href="<portlet:renderURL portletMode='view'/>">Back</a></div>