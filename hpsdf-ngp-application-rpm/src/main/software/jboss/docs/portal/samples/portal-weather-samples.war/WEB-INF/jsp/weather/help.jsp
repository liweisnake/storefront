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

<div class="portlet-font" style="font-size:larger;font-weight:bold">Weather Portlet Help:</div>
</div>
<br/>
<div class="portlet-font">Description: The weather portlet retrieves an RSS weather feed, based on a key (Can be a US Postal Code), and displays it as HTML to the user.</div>
<br/>
<div class="portlet-font">The user can change the weather forecast for the location of his choice by going to the edit mode after being logged-in and entering the key for that
particular location.</div>
<br/>
<div class="portlet-font" style="font-size:larger">How to get the key:</div>
</div>
<br/>
<div class="portlet-font">For a location is the US, the key is simply the zipcode. For a location outside the US you need to get the key by
doing the following steps:
 <ol>
  <li>Go to <a href="http://weather.yahoo.com" target="_blank">http://weather.yahoo.com</a></li>
  <li>Enter your city name in the location field found on that page
  <div style="text-align:center;margin-bottom:1em"><img src="<%= renderRequest.getContextPath() %>/images/weather_config1.png"/></div>
  </li>
  <li>Find your location on the list under the form and click on it (<font style="font-style:italic">Neuchatel, NE CH</font>, in this case)
  <div style="text-align:center;margin-bottom:1em"><img src="<%= renderRequest.getContextPath() %>/images/weather_config2.png"/></div>
  </li>
  <li>Copy the location digits from the address bar in your browser (<font style="font-style:italic">SZXX0023</font> in this case)
  <div style="text-align:center;margin-bottom:1em"><img src="<%= renderRequest.getContextPath() %>/images/weather_config3.png"/></div>
  </li>
  <li>Paste this key in the weather portlet edit form and press <font style="font-style:italic">Submit</font>.
  <div style="text-align:center;margin-bottom:1em"><img src="<%= renderRequest.getContextPath() %>/images/weather_config4.png"/></div>
  </li>
 </ol>
</div>
<br/>
<div class="portlet-font"><a href="<portlet:renderURL portletMode='view'/>">Back</a></div>