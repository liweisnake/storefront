<%@ page import="org.jboss.portal.cms.model.Folder" %>
<%@ page import="org.jboss.portal.cms.util.NodeUtil" %>
<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>

<%
   // needed for copy/move type: fi(file) or fo(folder).
   String sCMType = (String)request.getAttribute("type");
%>

<script language="JavaScript" type="text/javascript">
   function fillform(val)
   {
   this.document.pickform.destination.value = val;
   this.document.pickform.showdestination.value = val;
   }
</script>

<h6>${n:i18n("CMS_CONTENT_DIR")}</h6>
${n:i18n("CMS_CONTENT_DIR_USE")}
<hr/>

<a href="<portlet:renderURL>
   <portlet:param name="op" value="<%= OP %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="navpath" value="/"/>
   <%
      if (sCMType != null)
      {
   %>
   <portlet:param name="type" value="<%= sCMType %>"/>
   <%
      }
   %>
   </portlet:renderURL>">
   <img src="<%= request.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH %>/plus.gif" border="0"
        alt="Expand"/></a>
&nbsp;
<a href="javascript:fillform('/')">${n:i18n("CMS_ROOT_FOLDER")}</a><br>

<%
   String sNavPath = (String)request.getAttribute("navpath");
   List NAVfolders = (List)request.getAttribute("folders");
   if (NAVfolders != null && NAVfolders.size() > 0)
   {
      Folder trailFolder = (Folder)NAVfolders.get(0);
      String sSomePath = trailFolder.getBasePath();
      int firstSlash = sSomePath.indexOf("/");
      int lastSlash = sSomePath.lastIndexOf("/");
      sSomePath = sSomePath.substring(firstSlash, lastSlash);
      StringTokenizer parser = new StringTokenizer(sSomePath, "/");
      String sPathBuilder = "";
      while (parser.hasMoreTokens())
      {
         String sPathChunk = parser.nextToken();
         sPathBuilder += "/" + sPathChunk;
%>
&nbsp;
<a href="<portlet:renderURL>
   <portlet:param name="op" value="<%= OP %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="navpath" value="<%= sPathBuilder %>"/>
   <%
      if (sCMType != null)
      {
   %>
   <portlet:param name="type" value="<%= sCMType %>"/>
   <%
      }
   %>
   </portlet:renderURL>">
   <img src="<%= request.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH %>/plus.gif" border="0"
        alt="Expand"/></a>&nbsp;<a href="javascript:fillform('<%= sPathBuilder %>')"><%= sPathBuilder %></a><br>

<%
   }

   for (int i = 0; i < NAVfolders.size(); i++)
   {
      Folder folder = (Folder)NAVfolders.get(i);
%>

&nbsp;&nbsp;&nbsp;<a href="
<portlet:renderURL>
   <portlet:param name="op" value="<%= OP %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="navpath" value="<%= folder.getBasePath() %>"/>
   <%
      if (sCMType != null)
      {
   %>
   <portlet:param name="type" value="<%= sCMType %>"/>
   <%
      }
   %>
</portlet:renderURL>
"><img src="<%= request.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH %>/plus.gif" border="0"
alt="Expand"/></a>&nbsp;<a href="javascript:fillform('<%= folder.getBasePath() %>')"><%=
folder.getBasePath().substring(folder.getBasePath().lastIndexOf("/") + 1, folder.getBasePath().length()) %></a>
<br>

<%}
}%>
