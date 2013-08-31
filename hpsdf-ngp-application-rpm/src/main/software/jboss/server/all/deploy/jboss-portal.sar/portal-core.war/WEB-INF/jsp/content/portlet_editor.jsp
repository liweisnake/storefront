<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ page import="org.jboss.portal.common.i18n.LocalizedString" %>
<%@ page import="org.jboss.portal.common.util.IteratorStatus" %>
<%@ page import="org.jboss.portal.core.model.instance.Instance" %>
<%@ page import="org.jboss.portal.core.portlet.info.PortletIconInfo" %>
<%@ page import="org.jboss.portal.core.portlet.info.PortletInfoInfo" %>
<%@ page import="org.jboss.portal.core.ui.content.portlet.PortletContentEditorPortlet" %>
<%@ page import="org.jboss.portal.portlet.Portlet" %>
<%@ page import="org.jboss.portal.portlet.PortletInvokerException" %>
<%@ page import="org.jboss.portal.portlet.info.MetaInfo" %>
<%@ page import="org.jboss.portal.portlet.info.PortletInfo" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Locale" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>

<%
   Collection instances = (Collection)request.getAttribute("INSTANCES");
   Instance selectedInstance = (Instance)request.getAttribute("SELECTED_INSTANCE");
%>
<portlet:defineObjects/>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/tooltip/domLib.js'></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tooltip/fadomatic.js"></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/js/tooltip/domTT.js'></script>
<script type="text/javascript">

   //<![CDATA[
   var domTT_styleClass = 'domTTOverlib';
   var domTT_maxWidth = false;
   //]]>

   //This function checks for a previousSibling in the DOM and it must be a form
   //generated by JSF. otherwise the actionURL will be treated as a regular link
   //for non-JSF environment
   //
   //For JSF use:
   //<h:form></h:form>
   //<jbp:portlet
   //...
   function submitForm(actionUrl){
      var jsfForm;
      try{
         //ie is normal
         if(document.getElementById('bilto').previousSibling.nodeName.toLowerCase() == 'form'){
            jsfForm = document.getElementById('bilto').previousSibling;
         }else{
            //firefox adds that extra text node
            jsfForm = document.getElementById('bilto').previousSibling.previousSibling;
         }
      }catch(e){

      }

      if (jsfForm.nodeName.toLowerCase() == 'form'){
         jsfForm.action = actionUrl;
         jsfForm.submit();
      }else{
         window.location = actionUrl;
      }
   }

</script>

<table style="width:500px;">
<tr>
  <td><h3 class="sectionTitle tenpx-top-bottom">${n:i18n("PORTLET_INSTANCE_ASSOCIATED")}:</h3></td>
</tr>
<%
   Locale locale = renderRequest.getLocale();
   if (selectedInstance != null)
   {
      String displayName = selectedInstance.getDisplayName().getString(locale, true);
      if (displayName == null)
      {
         displayName = selectedInstance.getId();
      }

      Portlet portlet = null;
      try
      {
         portlet = selectedInstance.getPortlet();
      }
      catch (PortletInvokerException e)
      {
         e.printStackTrace();
      }
      if (portlet != null)
      {
         PortletInfo info = portlet.getInfo();
         MetaInfo metaInfo = info.getMeta();
         String iconLocation = getIcon(info);
%>
<tr class="portlet-section-selected">
   <td>
      <div id="portlet-editor-title">
      <img src="<%= iconLocation %>" align="middle" class="editor-icon" alt="icon"/>
      <span><%= displayName %></span>

    <div>
      <span class="portlet-form-field-label">${n:i18n("PORTLET_NAME")}:</span><%= displayName %>
    </div>
    <div>
      <span class="portlet-form-field-label">${n:i18n("PORTLET_DESCRIPTION")}:</span>
      <%= getLocalizedValue(metaInfo.getMetaValue(MetaInfo.DESCRIPTION), locale) %>
    </div>
    </div>
   </td>
</tr>
<%
      }
   }
%>
<tr>
   <td>
      <div class="portlet-editor-content">
         <table style="width:100%;" cellspacing="0" cellpadding="0">
            <%
               int index;
               for (IteratorStatus i = new IteratorStatus(instances); i.hasNext();)
               {
                  Instance instance = (Instance)i.next();
                  index = i.getIndex();
                  String rowClass = instance == selectedInstance ? "portlet-section-selected" : (index % 2 == 0 ? "portlet-section-body" : "portlet-section-alternate");
                  String displayName = instance.getDisplayName().getString(locale, true);
                  PortletInfo info = instance.getPortlet().getInfo();
                  MetaInfo portletMetaInfo = info.getMeta();

                  if (displayName == null)
                  {
                     displayName = instance.getId();
                  }

                  String iconLocation = getIcon(info);
                  String linkId="portlet-instance-link"+"_"+index;
                  String displayNameId = "info-container-" + displayName.replace("'","/'");
            %>
            <portlet:actionURL var="url">
               <portlet:param name="content.action.select" value="true"/>
               <portlet:param name="content.uri" value="<%= instance.getId() %>"/>
            </portlet:actionURL>

            <div style="display:none">
               <div class="darktip" id="<%=displayNameId%>">
                  <div class="toolbar" style="width: 250px;">
                     <div class="title"><%= getLocalizedValue(portletMetaInfo.getMetaValue(MetaInfo.TITLE), locale) %>
                     </div>
                  </div>
                  <div class="content">
                     <div><span class="portlet-form-field-label">${n:i18n("PORTLET_NAME")}:</span><%= displayName %>
                     </div>
                     <div><span class="portlet-form-field-label">${n:i18n("PORTLET_DESCRIPTION")}:</span>
                        <%= getLocalizedValue(portletMetaInfo.getMetaValue(MetaInfo.DESCRIPTION), locale) %>
                     </div>
                  </div>
               </div>
            </div>

            <script type="text/javascript">
               function showToolTip<%=index%>(element,event){
                  return domTT_activate(element, event, "content", document.getElementById("<%=displayNameId%>"),"delay", 0, "trail", false, "fade", "both", "fadeMax", 95, "styleClass", "none");
               }
            </script>

            <tr class="<%= rowClass %>">
               <td>
                  <img src="<%= iconLocation %>" align="middle" class="editor-icon" alt="icon"/>
                  <span
                     onmouseover="showToolTip<%=index%>(this,event);"><a
                     href="javascript:void(0);" onclick="submitForm('<%= url %>');return false"
                     id="<%= linkId %>"><%= displayName %>
                  </a></span>
               </td>
            </tr>
            <%
               }
            %>
         </table>
      </div>
   </td>
</tr>
</table>

<%!
   private String getIcon(PortletInfo info)
   {
      String iconLocation = PortletContentEditorPortlet.DEFAULT_PORTLET_ICON;
      PortletInfoInfo portletInfo = (PortletInfoInfo)info.getAttachment(PortletInfoInfo.class);

      if (portletInfo != null)
      {
         PortletIconInfo iconInfo = portletInfo.getPortletIconInfo();
         if (iconInfo != null && iconInfo.getIconLocation(PortletIconInfo.SMALL) != null)
         {
            iconLocation = iconInfo.getIconLocation(PortletIconInfo.SMALL);
         }
      }

      return iconLocation;
   }

   private String getLocalizedValue(LocalizedString locString, Locale locale)
   {
      String value = "";
      if (locString != null)
      {
         value = locString.getString(locale, true);
      }

      return value;
   }
%>
