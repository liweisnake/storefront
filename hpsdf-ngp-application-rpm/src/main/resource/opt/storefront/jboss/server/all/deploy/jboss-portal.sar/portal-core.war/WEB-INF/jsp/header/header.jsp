<%@ page import="org.jboss.portal.api.PortalURL" %>
<%@ page import="org.jboss.portal.common.text.EntityEncoder" %>
<%@ page import="org.jboss.portal.identity.User" %>
<%@page import="java.util.ResourceBundle"%>
<%@ page import="java.security.Principal" %>
<%@ page import="java.io.BufferedInputStream"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.Properties"%>

<%
   ResourceBundle rb = ResourceBundle.getBundle("Resource", request.getLocale());
   Principal principal = (Principal)request.getAttribute("org.jboss.portal.header.PRINCIPAL");
   PortalURL dashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.DASHBOARD_URL");
   PortalURL loginURL = (PortalURL)request.getAttribute("org.jboss.portal.header.LOGIN_URL");
   PortalURL defaultPortalURL = (PortalURL)request.getAttribute("org.jboss.portal.header.DEFAULT_PORTAL_URL");
   PortalURL adminPortalURL = (PortalURL)request.getAttribute("org.jboss.portal.header.ADMIN_PORTAL_URL");
   PortalURL editDashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.EDIT_DASHBOARD_URL");
   PortalURL copyToDashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.COPY_TO_DASHBOARD_URL");
   PortalURL signOutURL = (PortalURL)request.getAttribute("org.jboss.portal.header.SIGN_OUT_URL");
%>

<%
   if (principal == null)
   {
%>

<%if(request.getAttribute("ssoEnabled") == null){%>
<script type="text/javascript">
    /* <![CDATA[ */
    if (typeof isModalLoaded != 'undefined'){
        document.write('<a href=\"#\" onclick=\"alertModal(\'login-modal\',\'login-modal-msg\');return false;\">Login</a>');
    }else{
        document.write('<a href=\"<%= loginURL %>\">Login</a>');
    }
   //set the iframe src for login modal to requested URL
   var iframeSrc = '<%= loginURL %>' + '?loginheight=0';
   document.getElementById('loginIframe').src = iframeSrc;
   /* ]]> */
</script>

<noscript>
      <a href="<%= loginURL %>"><%= EntityEncoder.FULL.encode(rb.getString("LOGIN")) %></a>
</noscript>
<%}else{%>
<a href="<%= loginURL %>"><%= EntityEncoder.FULL.encode(rb.getString("LOGIN")) %></a>
<%}%>


<%
}
else
{
%>
<script type="text/javascript">
   /* <![CDATA[ */
   //we don't need the iframe/modal if logged in
   document.getElementById('loginIframe').src = '';
   /* ]]> */
</script>
<%= rb.getString("LOGGED") %>: <%= principal.getName() %><br/><br/>

<%
   if (dashboardURL != null)
   {
%><%
   }

   if (defaultPortalURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= defaultPortalURL %>"><%= rb.getString("PORTAL") %></a>&nbsp;&nbsp;|<%
   }

   if (adminPortalURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= adminPortalURL %>"><%= rb.getString("ADMIN") %></a>&nbsp;&nbsp;|<%
   }

   if (editDashboardURL != null)
   {
%>&nbsp;&nbsp;<a href="<%= editDashboardURL %>"><%= rb.getString("CONFIGURE_DASHBOARD") %></a>&nbsp;&nbsp;|<%
   }

   if (copyToDashboardURL != null)
   {
%><%
   }
%>&nbsp;&nbsp;<a href="<%= signOutURL %>"><%= rb.getString("LOGOUT") %></a>
<%
   }
   Properties props = new Properties();
   String filePath="/opt/storefront/customize/customize.properties";
   String drupal="";
   try{
          InputStream in = new BufferedInputStream (new FileInputStream(filePath));
          props.load(in);
          drupal=props.getProperty("casPrefix")+"://"+props.getProperty("casHost");
          if (props.getProperty("casPort")!=null && props.getProperty("casPort")!=""){
              drupal=drupal+":"+props.getProperty("casPort");
          }
          drupal=drupal+props.getProperty("casPath")+"?service="+props.getProperty("drupalPrefix")+"://"+props.getProperty("drupalHost");
          if (props.getProperty("drupalPort")!=null && props.getProperty("drupalPort")!=""){
              drupal=drupal+":"+props.getProperty("drupalPort");
          }
          drupal=drupal+props.getProperty("drupalPath");
   }catch (Exception e) {
       throw e;
   }
%>
<!--| <a href="#" onclick="document.location='<%=drupal%>';return false;">Drupal</a>-->
