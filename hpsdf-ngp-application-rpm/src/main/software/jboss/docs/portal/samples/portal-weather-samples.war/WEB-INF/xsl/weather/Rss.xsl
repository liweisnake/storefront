<?xml version="1.0" ?>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~ JBoss, a division of Red Hat                                              ~
  ~ Copyright 2006, Red Hat Middleware, LLC, and individual                   ~
  ~ contributors as indicated by the @authors tag. See the                    ~
  ~ copyright.txt in the distribution for a full listing of                   ~
  ~ individual contributors.                                                  ~
  ~                                                                           ~
  ~ This is free software; you can redistribute it and/or modify it           ~
  ~ under the terms of the GNU Lesser General Public License as               ~
  ~ published by the Free Software Foundation; either version 2.1 of          ~
  ~ the License, or (at your option) any later version.                       ~
  ~                                                                           ~
  ~ This software is distributed in the hope that it will be useful,          ~
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of            ~
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU          ~
  ~ Lesser General Public License for more details.                           ~
  ~                                                                           ~
  ~ You should have received a copy of the GNU Lesser General Public          ~
  ~ License along with this software; if not, write to the Free               ~
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA        ~
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.                  ~
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
   <xsl:variable name="image-path">http://us.i1.yimg.com/us.yimg.com/i/us/we/52/</xsl:variable>
   <xsl:variable name="link-path">
      <xsl:value-of select="rss/channel/link"/>
   </xsl:variable>
   <xsl:template match="/">
      <br/>
      <div align="center">
         <font>
            <b>
               <xsl:for-each select="rss/channel/location">
                  <xsl:value-of select="@city"/>, <xsl:value-of select="@region"/>, <xsl:value-of
                  select="@country"/>
               </xsl:for-each>
            </b>
         </font>
         <br/>
         <br/>
         <table width="130" border="0">
            <tr>
               <xsl:for-each select="rss/channel/item/condition">
                  <td>
                     <table border="0" height="130">
                        <tr>
                           <td class="portlet-section-header" align="center">Currently</td>
                        </tr>
                        <tr>
                           <td class="portlet-section-alternate" align="center" valign="top">
                              <img src="{$image-path}/{@code}.gif"/>
                           </td>
                        </tr>
                        <tr>
                           <td class="portlet-section-alternate" align="center" valign="top">
                              <xsl:value-of select="@text"/>
                           </td>
                        </tr>
                        <tr>
                           <td class="portlet-section-alternate" align="center" valign="top">
                              <xsl:value-of select="@temp"/>F</td>
                        </tr>
                     </table>
                  </td>
               </xsl:for-each>
               <xsl:for-each select="rss/channel/item/forecast">
                  <td>
                     <table border="0" height="130">
                        <tr>
                           <td class="portlet-section-header" align="center" valign="top">
                              <xsl:value-of select="@day"/>
                           </td>
                        </tr>
                        <tr>
                           <td class="portlet-section-alternate" align="center" valign="top">
                              <img src="{$image-path}/{@code}.gif"/>
                           </td>
                        </tr>
                        <tr>
                           <td class="portlet-section-alternate" align="center" valign="top">
                              <xsl:value-of select="@text"/>
                           </td>
                        </tr>
                        <tr>
                           <td class="portlet-section-alternate" align="center" valign="top">
                              <xsl:value-of select="@low"/>F/<xsl:value-of select="@high"/>F</td>
                        </tr>
                     </table>
                  </td>
               </xsl:for-each>
            </tr>
         </table>
         <br/>
         <a href="{$link-path}" target="_blank">Complete Forecast</a>
      </div>
   </xsl:template>
</xsl:stylesheet>
