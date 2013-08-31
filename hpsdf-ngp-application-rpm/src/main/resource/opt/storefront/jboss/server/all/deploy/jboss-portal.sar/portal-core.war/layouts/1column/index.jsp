<%@ page import="org.jboss.portal.server.PortalConstants" %>
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="/WEB-INF/theme/portal-layout.tld" prefix="p" %>
<% ResourceBundle rb = ResourceBundle.getBundle("Resource", request.getLocale()); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>HP Storefront Portal</title>
   <meta http-equiv="Content-Type" content="text/html;"/>
   <!-- to correct the unsightly Flash of Unstyled Content. -->
   <script type="text/javascript"></script>
   <!-- inject the theme, default to the Renewal theme if nothing is selected for the portal or the page -->
   <p:theme themeName="renewal"/>
   <!-- insert header content that was possibly set by portlets on the page -->
   <p:headerContent/>
   <%@include file="/layouts/common/modal_head.jsp"%>
</head>

<body id="body">
<p:region regionName='AJAXScripts' regionID='AJAXScripts'/>
<%@include file="/layouts/common/modal_body.jsp"%>
<div id="portal-container">
   <div id="sizer">
      <div id="expander">
         <div id="logoName"></div>
         <table border="0" cellpadding="0" cellspacing="0" id="header-container">
            <tr>
               <td align="center" valign="top" id="header">

                  <!-- Utility controls -->
                  <p:region regionName='dashboardnav' regionID='dashboardnav'/>

                  <!-- navigation tabs and such -->
                  <p:region regionName='navigation' regionID='navigation'/>
                  <div id="spacer"></div>
               </td>
            </tr>
         </table>
         <div id="content-container">
           <!-- <table width="100%">
              <tr>
                <td valign="top">-->
                  <!-- insert the content of the 'center' region of the page, and assign the css selector id 'regionB' -->
                  <p:region regionName='center' regionID='center'/>
               <!-- </td>
              </tr>
            </table>-->
            <hr class="cleaner"/>
         </div>
      </div>
   </div>
</div>

<!-- TODO: Fix the auto jump in this tag -->
<div id="footer-container" class="portal-copyright"><%= rb.getString("POWERED_BY") %>
HP Storefront Portal
</div>

<p:region regionName='AJAXFooter' regionID='AJAXFooter'/>

</body>
</html>
