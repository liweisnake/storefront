<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ page isELIgnored="false" %>

<%@ page import="org.jboss.portal.cms.model.Content" %>
<%@ page import="org.jboss.portal.cms.util.NodeUtil" %>
<%@ page import="org.jboss.portal.core.cms.ui.admin.CMSAdminConstants" %>
<%@ page import="org.jboss.portal.common.text.EntityEncoder" %>
<%@ page import="java.text.Format" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.util.Vector" %>

<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="/portal-admin/css/style.css" media="screen"/>

<div class="admin-ui">
<br/>
<h3 class="sectionTitle-blue">
   ${n:i18n("CMS_MANAGE")}
</h3>
<div class=" cms-tab-container">
<%
   String sConfirm = renderRequest.getParameter("confirm");
   String sCurrPath = (String)request.getAttribute("currpath");
   Vector vContents = (Vector)request.getAttribute("contents");
   Collection pendingQueue = (Collection)request.getAttribute("pendingQueue");

   String sType = "";
   if (vContents.size() > 0)
   {
      List contentList = (List)vContents.elementAt(0);
      if (contentList.size() > 0)
      {
         Content content = (Content)contentList.get(0);
         sType = content.getMimeType();
      }
   }
   String sPreviewPath = (String)request.getAttribute("previewpath");

   String createDate = "";
   String modifiedDate = "";

   String rowClass = "portlet-section-body";

   String exception = request.getParameter("exception");
   Boolean manageWorkflowAccessible = (Boolean)request.getAttribute("manageWorkflowAccessible");
   
   Format dateFormat = (Format)request.getAttribute("dateFormat");
%>




<ul class="objectpath">
   <li class="pathItem"><a href="<portlet:renderURL>
      <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/>
      <portlet:param name="path" value="/"/>
      </portlet:renderURL>">${n:i18n("CMS_HOME")}</a></li>


   <%
      StringTokenizer parser = new StringTokenizer(sCurrPath, "/");
      String sPathBuilder = "";
      while (parser.hasMoreTokens())
      {
         String sPathChunk = parser.nextToken();
         sPathBuilder += "/" + sPathChunk;
         if (parser.hasMoreTokens())
         {
   %>
   <li class="pathSeparator"><img src="/portal-admin/img/pathSeparator.png" alt=">"></li>
   <li class="pathItem"><a href="
<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/>
   <portlet:param name="path" value="<%= sPathBuilder %>"/>
</portlet:renderURL>
"><%= EntityEncoder.FULL.encode(sPathChunk) %>
   </a>
   </li>
   <%
   }
   else
   {
   %>
   <li class="pathSeparator"><img src="/portal-admin/img/pathSeparator.png" alt=">"></li>
   <li class="selected"><%= EntityEncoder.FULL.encode(sPathChunk) %>
   </li>
   <%
         }
      }
   %>
</ul>
<br/>
<!-- file-level action dropdown -->
<div class="menu-container" style="width:100%">

<%
   if (sConfirm != null && !"".equals(sConfirm))
   {
%>
   <div class="portlet-msg-success"><%= sConfirm %></div>
   <br/>
<%
   }
%>

   <div class="menu">

      <select onchange="window.open(this.options[this.selectedIndex].value,'_top')">
         <option value="">${n:i18n("CMS_SELECT_ACTION")}</option>
         <%
            if (sType.equals("text/html") || sType.equals("text/plain"))
            {
         %>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_CREATENEWTEXT %>"/>
   <portlet:param name="path" value="<%= NodeUtil.getParentPath(sCurrPath) %>"/>
   </portlet:renderURL>">${n:i18n("CMS_CREATE")}</option>
         <%
         }
         else
         {
         %>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_UPLOADCONFIRM %>"/>
   <portlet:param name="path" value="<%= NodeUtil.getParentPath(sCurrPath) %>"/>
   </portlet:renderURL>">${n:i18n("CMS_EDIT")}</option>
         <%
            }
         %>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMCOPY %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="type" value="fi"/>
   </portlet:renderURL>">${n:i18n("CMS_COPY")}</option>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMMOVE %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="type" value="fi"/>
   </portlet:renderURL>">${n:i18n("CMS_MOVE")}</option>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMDELETE %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   </portlet:renderURL>">${n:i18n("CMS_DELETE")}</option>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_CONFIRMSECURE %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="returnOp" value="<%= CMSAdminConstants.OP_VIEWFILE %>"/>
   </portlet:renderURL>">${n:i18n("CMS_SECURE")}</option>
         <%
            if (manageWorkflowAccessible.booleanValue())
            {
         %>
         <option value="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_VIEWPENDING %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   </portlet:renderURL>">${n:i18n("CMS_APPROVAL")}</option>
         <%}%>
      </select>

   </div>
</div>
<br style="clear:both"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<%
   if (vContents.size() > 0)
   {
      for (int j = 0; j < vContents.size(); j++) // cycle thru list of content nodes
      {
         List contentList = (List)vContents.elementAt(j);
         if (contentList.size() > 0)
         {
            Content content = (Content)contentList.get(0);
            
            boolean hasPublishedContent = false;
            for (int i = 0; i < contentList.size(); i++) // cycle thru list of version nodes
   			{
      			Content version = (Content)contentList.get(i);
      			if (!version.isWaitingForPublishApproval())
      			{
         			hasPublishedContent = true;
         			break;
      			}
      		}
%>
<%if(hasPublishedContent){%>
<tr>
   <td height="15"></td>
</tr>
<tr>
   <td colspan="6">
      <%
         if (sType.equals("text/html") || sType.equals("text/plain"))
         {
      %>
      <img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/edit.gif"
           alt="${n:i18n("CMS_EDIT")}" border="0">&nbsp;<a href="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_EDIT %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="language" value="<%= content.getLocale().getLanguage() %>" />
   </portlet:renderURL>"><%= content.getLocale().getDisplayLanguage() %>
   </a>
      <%
      }
      else
      {
      %>
      <img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/edit.gif"
           alt="${n:i18n("CMS_EDIT")}" border="0">&nbsp;<a href="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_EDIT_BINARY %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="language" value="<%= content.getLocale().getLanguage() %>"/>
   </portlet:renderURL>"><%= content.getLocale().getDisplayLanguage() %>
   </a>
      <%
         }
      %>
   </td>
</tr>
<tr>
   <td class="portlet-table-text"><b>${n:i18n("CMS_TYPE")}</b></td>
   <td class="portlet-table-text"><b>${n:i18n("CMS_SIZE")}</b></td>
   <td class="portlet-table-text"><b>${n:i18n("CMS_VERSION")}</b></td>
   <td class="portlet-table-text"><b>${n:i18n("CMS_CREATED")}</b></td>
   <td class="portlet-table-text"><b>${n:i18n("CMS_MODIFIED")}</b></td>
   <td class="portlet-table-text"><b>${n:i18n("CMS_TITLE")}</b></td>
</tr>
<%
   for (int i = 0; i < contentList.size(); i++) // cycle thru list of version nodes
   {
      Content version = (Content)contentList.get(i);

      if (version.isWaitingForPublishApproval())
      {
         continue;
      }

      if (i % 2 == 0)
      {
         rowClass = "portlet-section-body";
      }
      else
      {
         rowClass = "portlet-section-alternate";
      }
%>
<tr onmouseover="this.className='portlet-section-selected';" onmouseout="this.className='<%= rowClass %>';"
    class="<%= rowClass %>">
   <td><%
      if (sType.equals("text/html") || sType.equals("text/plain"))
      {
   %>

      <img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/edit.gif"
           alt="${n:i18n("CMS_EDIT")}" border="0">&nbsp;<a href="<portlet:renderURL>
   <portlet:param name="op" value="<%= CMSAdminConstants.OP_EDIT %>"/>
   <portlet:param name="path" value="<%= sCurrPath %>"/>
   <portlet:param name="language" value="<%= content.getLocale().getLanguage() %>" />
   <portlet:param name="version" value="<%= version.getVersionNumber() %>"/>
   </portlet:renderURL>"><%= version.getMimeType() %>
   </a>
      <%
      }
      else
      {
      %>
      <%= version.getMimeType() %>
      <%
         }
      %>
   </td>
   <td><%= version.getSize() / 1024 %>kb</td>
   <td>
      <%
         if (sType.equals("text/html") || sType.equals("text/plain"))
         {
      %>
      <%= version.getVersionNumber() %>
      <% }
      else
      {
         //save the principal for the preview servlet
         String remoteUser = request.getRemoteUser();
         request.getSession().setAttribute("remoteUser", remoteUser);
      %>
      <a target="_blank"
         href="<%= request.getContextPath() %>/cmspreview?v=<%= version.getVersionNumber() %>&l=<%= content.getLocale().getLanguage() %>&p=<%= sCurrPath %>"><%=
      version.getVersionNumber() %>
      </a>
      <%
         }
         if (version.isLive())
         {
      %>
      <img src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/actionIcon_IsDefault.gif"
           alt="${n:i18n("CMS_LIVEVERSION")}" border="0"/>
      <%
      }
      else
      {
      %>
      <a href="<portlet:actionURL>
      	         <portlet:param name="op" value="<%= CMSAdminConstants.OP_MAKELIVE %>"/>
      	         <portlet:param name="path" value="<%= sCurrPath %>"/>
      	         <portlet:param name="language" value="<%= content.getLocale().getLanguage() %>"/>
   		         <portlet:param name="version" value="<%= version.getVersionNumber() %>"/>
   		         </portlet:actionURL>"
         >
         <img
            src="<%= renderRequest.getContextPath() + CMSAdminConstants.DEFAULT_IMAGES_PATH%>/actionIcon_MakeDefault.gif"
            alt="${n:i18n("CMS_LIVEVERSION")}" border="0"/>
      </a>
      <%}%>
   </td>
   <td>
      <%
         if (version.getCreationDate() != null)
         {
            createDate = dateFormat.format(version.getCreationDate());
         }
      %>
      <%= createDate %>
   </td>
   <td>
      <%
         if (version.getLastModified() != null)
         {
            modifiedDate = dateFormat.format(version.getLastModified());
         }
      %>
      <%= modifiedDate %>
   </td>
   <td><%= EntityEncoder.FULL.encode(version.getTitle()) %>
   </td>
</tr>
<%}%>
<%
            }
         }
      }
   }
%>
</table>

<!-- the approval queue, content waiting for managers to signoff on -->
<%
   if (pendingQueue != null && !pendingQueue.isEmpty())
   {
%>
<br/><br/>

<!-- show any errors here -->
<%
   if (exception != null && exception.trim().length() > 0)
   {
%>
<table width="100%">
   <th colspan="2"><h3 class="sectionTitle-blue">${n:i18n("CMS_ERROR")}:</h3></th>
   <tr colspan="2" align="center">
      <td colspan="2">
         <font color="red">
            <%=exception%>
         </font>
      </td>
   </tr>
</table>
<br/><br/>
<input class="portlet-form-button" type="button" value="${n:i18n("CMS_CANCEL")}" name="cancel"
       onclick="window.location='<portlet:renderURL><portlet:param name="op" value="<%= CMSAdminConstants.OP_MAIN %>"/><portlet:param name="path" value="/"/></portlet:renderURL>'">
<%}%>

<div align="center"><font class="portlet-font-dim"><b>${n:i18n("CMS_PENDING_APPROVAL_QUEUE")}</b></font></div>
<br/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
      <td class="portlet-table-text"><b>${n:i18n("CMS_TYPE")}</b></td>
      <td class="portlet-table-text"><b>${n:i18n("CMS_SIZE")}</b></td>
      <td class="portlet-table-text"><b>${n:i18n("CMS_CREATED")}</b></td>
      <td class="portlet-table-text"><b>${n:i18n("CMS_CREATED_BY")}</b></td>
      <%
         if (manageWorkflowAccessible.booleanValue())
         {
      %>
      <td class="portlet-table-text"><b>${n:i18n("CMS_ACTION")}</b></td>
      <%}%>
   </tr>
   <%int i = 0;%>
   <%
      for (Iterator itr = pendingQueue.iterator(); itr.hasNext();)
      {
   %>
   <%
      org.jboss.portal.cms.workflow.Content cour = (org.jboss.portal.cms.workflow.Content)itr.next();
      if (i % 2 == 0)
      {
         rowClass = "portlet-section-body";
      }
      else
      {
         rowClass = "portlet-section-alternate";
      }
      i++;
   %>
   <tr class="<%= rowClass %>">
      <!-- Mime Type -->
      <td><%= cour.getMimeType() %>
      </td>
      <!-- Size -->
      <td><%= cour.getSizeStr() %>
      </td>
      <!-- Creation Date -->
      <td>
         <%= cour.getCreationDateStr() %>
      </td>
      <!-- User who requested approval -->
      <td><%= cour.getUserName() %>
      </td>
      <%
         if (manageWorkflowAccessible.booleanValue())
         {
      %>
      <td>        
            <%
         if (cour.getMimeType().equals("text/html") || cour.getMimeType().equals("text/plain"))
         {
      %>
      	 <a href="<portlet:renderURL>
         		<portlet:param name="op" value="<%= CMSAdminConstants.OP_VIEWPENDINGPREVIEW %>"/>
         		<portlet:param name="pid" value="<%=cour.getProcessId()%>"/>
         		<portlet:param name="path" value="<%=sCurrPath%>"/>
         		<portlet:param name="contentPath" value="<%=cour.getPath()%>"/>
         </portlet:renderURL>">${n:i18n("CMS_PREVIEW")}</a>

      <% }
      else
      {
         //save the principal for the preview servlet
         String remoteUser = request.getRemoteUser();
         request.getSession().setAttribute("remoteUser", remoteUser);
      %>
      <a target="_blank"
         href="<%= request.getContextPath() %>/cmspreview?pid=<%= cour.getProcessId() %>&p=<%= cour.getPath() %>">${n:i18n("CMS_PREVIEW")}</a>
      <%
      }
      %>         
         
         &nbsp;
         <a href="<portlet:actionURL>
         <portlet:param name="op" value="<%=CMSAdminConstants.OP_APPROVE%>"/>
         <portlet:param name="pid" value="<%=cour.getProcessId()%>"/>
         <portlet:param name="path" value="<%=sCurrPath%>"/>
         <portlet:param name="from" value="<%=CMSAdminConstants.OP_VIEWFILE%>"/>
         </portlet:actionURL>">${n:i18n("CMS_APPROVE")}</a>
         &nbsp;
         <a href="<portlet:actionURL>
         <portlet:param name="op" value="<%=CMSAdminConstants.OP_DENY%>"/>
         <portlet:param name="pid" value="<%=cour.getProcessId()%>"/>
         <portlet:param name="path" value="<%=sCurrPath%>"/>
         <portlet:param name="from" value="<%=CMSAdminConstants.OP_VIEWFILE%>"/>
         </portlet:actionURL>">${n:i18n("CMS_DENY")}</a>
      </td>
      <%}%>
   </tr>
   <%}%>
</table>
<%}%>
</div>
</div>