<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
   
   <xsl:variable name="link-path">
      <xsl:value-of select="rss/channel/link"/>
   </xsl:variable>

   <xsl:template match="/">
      <div style="font-size: 2em;" class="portlet-section-header">
         <a href="{$link-path}" target="_blank">
            <xsl:value-of select="rss/channel/title"/> - <xsl:value-of select="rss/channel/description"/>
         </a>
      </div>
      <ul style="max-width: 600px">
         <xsl:for-each select="rss/channel/item">
            <xsl:variable name="this-link">
               <xsl:value-of select="link"/>
            </xsl:variable>
            <li>
               <div style="font-size: 1.5em;" lass="portlet-section-subheader">
                 <a href="{$this-link}" target="_blank">
                   <xsl:value-of select="title"/>
                 </a>
               </div>
               <div class="portlet-font-dim">
                   <xsl:if test="string(author)">
                     <xsl:value-of select="author"/>
                   </xsl:if>
                   <xsl:if test="string(author) and string(pubDate)">
                     &#160;-&#160;
                   </xsl:if>
                   <xsl:if test="string(pubDate)">
                     <xsl:value-of select="pubDate"/>
                   </xsl:if>
               </div>
               <div class="portlet-font">
                   <xsl:value-of select="description" disable-output-escaping="yes"/>
               </div>
            </li>
         <br/>
         </xsl:for-each>
      </ul>
      <br/>
      <br/>
      <xsl:value-of select="rss/channel/copyright"/>
   </xsl:template>
</xsl:stylesheet>
