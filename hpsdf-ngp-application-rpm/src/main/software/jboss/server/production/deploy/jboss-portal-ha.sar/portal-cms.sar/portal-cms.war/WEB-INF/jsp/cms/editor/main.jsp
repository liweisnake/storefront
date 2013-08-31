<%@ page import="org.jboss.portal.cms.model.File" %>
<%@ page import="org.jboss.portal.cms.model.Folder" %>
<%@ page import="org.jboss.portal.cms.model.Content" %>
<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="java.text.Format" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page isELIgnored="false" %>

<portlet:defineObjects/>

<%
   String sCurrPath = (String)request.getAttribute("currpath");
   List folders = (List)request.getAttribute("folders");
   List files = (List)request.getAttribute("files");
   String uri = (String)renderRequest.getParameter("content.uri");
   PortletURL url = renderResponse.createRenderURL();
   url.setParameter("op", CMSAdminConstants.OP_MAIN);
   url.setParameter("path", "/");
   Format dateFormat = (Format)request.getAttribute("dateFormat");
%>

<!-- Currently browsing -->
${n:i18n("CMS_BROWSING")}: <a href="<%= url %>">${n:i18n("CMS_HOME")}</a>
<%
   StringTokenizer parser = new StringTokenizer(sCurrPath, "/");
   String sPathBuilder = "";
   while (parser.hasMoreTokens())
   {
      String sPathChunk = parser.nextToken();
      sPathBuilder += "/" + sPathChunk;
      if (parser.hasMoreTokens())
      {
         url.setParameter("path", sPathBuilder);
%>
>&nbsp;<a href="<%= url %>"><%= sPathChunk %>
</a>
<%
}
else
{
%>
>&nbsp;<%= sPathChunk %>
<%
      }
   }
%>

<br/><br/>

<%

   if (folders.size() > 0 || files.size() > 0)
   {

%>

<table width="100%" border="0" cellspacing="2" cellpadding="2">
   <tr>
      <td class="portlet-section-header">${n:i18n("CMS_NAME")}</td>
   </tr>

   <%
      if (folders.size() > 0)
      {
         for (int i = 0; i < folders.size(); i++)
         {
            Folder folder = (Folder)folders.get(i);
            url.setParameter("path", folder.getBasePath());
   %>
   <tr onmouseover="this.className='portlet-section-alternate';" onmouseout="this.className='portlet-section-body';">
      <td><img
         src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/folder.gif"
         alt="${n:i18n("CMS_FOLDER")}"
         border="0">&nbsp;<a href="<%= url %>"><%=
      folder.getBasePath().substring(folder.getBasePath().lastIndexOf("/") + 1, folder.getBasePath().length()) %>
      </a>
      </td>
   </tr>
   <%
         }
      }
      if (files.size() > 0)
      {
         for (int j = 0; j < files.size(); j++)
         {
            File file = (File)files.get(j);

            PortletURL metaURL = renderResponse.createActionURL();
            metaURL.setParameter("content.action.select", "select");
            metaURL.setParameter("content.uri", file.getBasePath());
            metaURL.setParameter("path", sCurrPath);

            if ((uri != null) && (uri.equals(file.getBasePath())))
            {
               out.println("<tr class=\"portlet-section-selected\">");
            }
            else
            {
               out.println("<tr onmouseover=\"this.className='portlet-section-alternate';\" onmouseout=\"this.className='portlet-section-body';\">");
            }
   %>
   <tr>
      <td><img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/file.gif"
               alt="${n:i18n("CMS_FILE")}"
               border="0">&nbsp;<a href="<%= metaURL %>"><%=
      file.getBasePath().substring(file.getBasePath().lastIndexOf("/") + 1, file.getBasePath().length()) %>
      </a>
      </td>
   </tr>
   <%
         }
      }
   %>
</table>
<%
   if (uri != null)
   {
      Content content = (Content)renderRequest.getAttribute("content");
%>
	<%if(content != null){%>
	<p class="portlet-font">
	   Selected file: <%= uri %><br/>
	   <%
	      if (content.getTitle() != null)
	      {
	         out.println("File title: " + content.getTitle() + "<br />");
	      }
	      if (content.getDescription() != null)
	      {
	         out.println("File description: " + content.getDescription() + "<br />");
	      }
	   %>
	</p>
	<%}else{%>
	<p class="portlet-font">
		${n:i18n("CMS_CONTENT_NOT_FOUND")}
	</p>
	<%}%>
<%
      }
   }
%>