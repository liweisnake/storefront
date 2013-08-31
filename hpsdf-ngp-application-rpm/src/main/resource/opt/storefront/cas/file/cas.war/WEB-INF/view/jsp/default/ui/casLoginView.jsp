<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>


<title>HP Storefront Portal</title>
<meta http-equiv="Content-Type" content="text/html;" />
<!-- to correct the unsightly Flash of Unstyled Content. -->
<script type="text/javascript"></script>
<!-- inject the theme, default to the Renewal theme if nothing is selected for the portal or the page  -->
<link rel="stylesheet" type="text/css" id="main_css"
	href="/cas/themes/grey/common/styles/default.css" />
<link rel="stylesheet" type="text/css" id="main_css"
	href="/cas/themes/grey/common/styles/layout.css" />
<link rel="stylesheet" type="text/css" id="main_css"
	href="/cas/themes/grey/common/styles/portal_style.css" />
<link rel="shortcut icon"
	href="/cas/themes/grey/common/images/favicon.ico" />
<!-- insert header content that was possibly set by portlets on the page  -->

<!--NGP link Javascripts -->

<script src="/cas/themes/grey/common/js/jquery-1.2.6.pack.js"
	type="text/javascript"></script>
<script src="/cas/themes/grey/common/js/stepcarousel.js"
	type="text/javascript"></script>

</head>

<body id="body">
<div id="portal-container">
<div id="sizer">
<div id="expander">
<div id="logoName"></div>
<table id="header-container" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td id="header" align="center" valign="top"><!-- Utility controls -->
			<div id="dashboardnav"><a href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/portal"%>">Home</a>
			</div>
			<div id="spacer"></div>
			</td>
		</tr>
	</tbody>
</table>
</div>
</div>
</div>
<!-- ngp header end -->
<!--%@include file="/layouts/common/modal_head.jsp"% -->
<div id="page">

<div id="mainBody">


<div id="loginIntro" class="left">
<h3>Sign in to HP Storefront Portal you can...</h3>
<ul>
	<li class="user"><b>User: </b>Browse applications and onboard as
	developer or tester.</li>
	<li class="develop"><b>Developer: </b>Subscribe API in Dev lab to
	develop an application; upload application to App Showcase Portal to be
	used by subscriber.</li>
	<li class="tester"><b>Tester:</b> Download application and test
	it.</li>
</ul>
</div>




<div id="loginForm" class="right">
<div class="mainPanel" id="login">
<div class="panelTitle">
<h3>Login</h3>

</div>
<div class="panelBody">
<div class="panelContent">
<form method="post"
	action="<%=response
							.encodeRedirectURL("login"
									+ (request.getQueryString() != null
											&& request.getQueryString()
													.length() > 0 ? "?"
											+ request.getQueryString() : ""))%>">



<fieldset class="noborder"><label>User Name:</label><br />
<input class="required" id="username" name="username" size="32"
	tabindex="1" /></fieldset>
<%--
			NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
			"autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
			information, see the following web page:
			http://www.geocities.com/technofundo/tech/web/ie_autocomplete.html
			--%>
<fieldset class="noborder"><label>Password:</label><br />
<input class="required" type="password" id="password" name="password"
	size="32" tabindex="2" /></fieldset>

<input type="hidden" name="lt" value="${flowExecutionKey}" /> <input
	type="hidden" name="_eventId" value="submit" />

<fieldset class="noborder"><input type="submit"
	class="button" accesskey="l" value="login" tabindex="4" />&nbsp; <input
	type="reset" class="button" accesskey="c" value="reset" tabindex="5" />
</fieldset>
</form>
</div>
</div>
<div class="panelBottom">
<div class="panelFoot"></div>
</div>
</div>
<fieldset class="noborder"><a class="btnSignUp"
	href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/portal"%>"
	style="margin: 0 auto;"></a></fieldset>

</div>
<div class="clear"></div>
</div>


</div>
<!-- the fooder 

<div id="footBottom" class="loginFoot">
						<div id="footCopy">
						<span><a href="#" onclick="location.href='index.html'">Privacy statement</a> | 
						<a href="#" onclick="location.href='index.html'">Using this site means you accept its terms</a> | 
						<a href="#" onclick="location.href='index.html'">Site map</a></span><br />
						<span>&copy; 2009 Hewlett-Packard Development Company, L.P.</span>
						</div>
</div>
<!-- the fooder end-->

<!--div id="footer-container" class="portal-copyright">Powered by
<a class="portal-copyright" href="http://www.jboss.com/products/jbossportal">JBoss Portal</a><br/>
</div -->
<!--
<script type='text/javascript'>footer()</script>
-->

</body>
</html>
