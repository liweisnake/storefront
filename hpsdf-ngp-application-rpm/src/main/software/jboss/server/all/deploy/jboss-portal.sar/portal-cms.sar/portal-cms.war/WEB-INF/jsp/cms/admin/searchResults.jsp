<%@ page import="org.jboss.portal.cms.model.File" %>
<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="java.text.Format" %>
<%@ page import="java.util.List" %>
<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<%
Format dateFormat = (Format)request.getAttribute("dateFormat");
%>

<portlet:defineObjects/>

<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>

<div class="admin-ui">
<br/>
<h3 class="sectionTitle-blue">
   ${n:i18n("CMS_MANAGE")}
</h3>
<div class=" cms-tab-container">

<!-- Currently browsing -->
<ul class="objectpath">
   <li class="pathItem"><a href="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/>
      <portlet:param name="path" value="/"/>
      </portlet:renderURL>">${n:i18n("CMS_HOME")}</a></li>
</ul>
<br/>

<div class="search-container">
   <form method="post" action="<portlet:actionURL>
    <portlet:param name="op" value="<%= CMSAdminConstants.OP_DOSEARCH %>"/>
    </portlet:actionURL>">
      <input type="text"
             size="15"
             maxlength="80"
             name="search"
             class="portlet-form-input-field"
             value="<%= request.getAttribute("textQuery") %>"
         />
      <input type="submit" name="search" value="${n:i18n("CMS_SEARCH")}" class="portlet-form-button"/>
   </form>
</div>

<%
Boolean error = (Boolean)request.getAttribute("conversionError");
if (error != null && error)
{
%>
<div class="portlet-msg-error">${n:i18n("CMS_QUERYERROR")}: <%= request.getAttribute("textQuery") %></div>

<%
}

   List files = (List)request.getAttribute("files");
   String createDate = "";
   String modifiedDate = "";

   if (files.size() > 0)
   {
%>
<table>
   <%
      for (int i = 0; i < files.size(); i++)
      {
         File file = (File)files.get(i);
   %>
   <tr onmouseover="this.className='portlet-section-alternate';" onmouseout="this.className='portlet-section-body';">
      <td><img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/file.gif"
               alt="${n:i18n("CMS_FILE")}"
               border="0">&nbsp;<a href="<portlet:renderURL>
          <portlet:param name="op" value="<%= CMSAdminConstants.OP_VIEWFILE %>"/>
          <portlet:param name="path"
            value="<%= file.getBasePath() %>"/>
        </portlet:renderURL>"><%=
      file.getBasePath().substring(file.getBasePath().lastIndexOf("/") + 1, file.getBasePath().length()) %>
      </a>
      </td>
      <td>
         <form method="POST" style="padding:0;margin:0;" action="<portlet:actionURL>
    <portlet:param name="path" value="<%= file.getBasePath() %>"/>
    <portlet:param name="type" value="fi"/>
    <portlet:param name="dispatch" value="1"/>
   </portlet:actionURL>">
            <select name="op">
               <option value="<%= CMSAdminConstants.OP_VIEWFILE %>">${n:i18n("CMS_VIEW")}</option>
               <option value="<%= CMSAdminConstants.OP_CONFIRMCOPY %>">${n:i18n("CMS_COPY")}</option>
               <option value="<%= CMSAdminConstants.OP_CONFIRMMOVE %>">${n:i18n("CMS_MOVE")}</option>
               <option value="<%= CMSAdminConstants.OP_CONFIRMDELETE %>">${n:i18n("CMS_DELETE")}</option>
            </select>
            <input type="submit" value="Go" name="Go" class="portlet-form-button"/>
         </form>
      </td>
      <td>
         <%
            if (file.getCreationDate() != null)
            {
               createDate = dateFormat.format(file.getCreationDate());
            }
         %>
         <%= createDate %>
      </td>
      <td>
         <%
            if (file.getLastModified() != null)
            {
               modifiedDate = dateFormat.format(file.getLastModified());
            }
         %>
         <%= modifiedDate %>
      </td>
   </tr>
   <%
      }
   %>
</table>
<%
}
else
{
%>
<h2>${n:i18n("CMS_SEARCHNORESULT")}</h2>
<%
   }
%>
<input class="portlet-form-button" type="button" value="Cancel" name="cancel"
       onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="/"/></portlet:renderURL>'">

</div>
</div>