<?xml version="1.0"?>

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

<xsl:stylesheet
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0">

   <xsl:output method="html"/>

   <xsl:template match="/">
      <TABLE WIDTH="100%" BORDER="0" CELLPADDING="2" CELLSPACING="0">
         <xsl:apply-templates/>
      </TABLE>
   </xsl:template>

   <xsl:template match="item">
      <TR>
         <TD>
            -
            <A TARGET="_popup" STYLE="text-decoration: none;">
               <xsl:attribute name="HREF">
                  <xsl:value-of select="link"/>
               </xsl:attribute>
               <xsl:value-of select="title"/>
            </A>
         </TD>
      </TR>
      <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="text()"/>
</xsl:stylesheet>