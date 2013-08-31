package org.apache.jsp.WEB_002dINF.jsp.header;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jboss.portal.api.PortalURL;
import org.jboss.portal.common.text.EntityEncoder;
import org.jboss.portal.identity.User;
import java.util.ResourceBundle;
import java.security.Principal;

public final class header_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

   ResourceBundle rb = ResourceBundle.getBundle("Resource", request.getLocale());
   Principal principal = (Principal)request.getAttribute("org.jboss.portal.header.PRINCIPAL");
   PortalURL dashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.DASHBOARD_URL");
   PortalURL loginURL = (PortalURL)request.getAttribute("org.jboss.portal.header.LOGIN_URL");
   PortalURL defaultPortalURL = (PortalURL)request.getAttribute("org.jboss.portal.header.DEFAULT_PORTAL_URL");
   PortalURL adminPortalURL = (PortalURL)request.getAttribute("org.jboss.portal.header.ADMIN_PORTAL_URL");
   PortalURL editDashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.EDIT_DASHBOARD_URL");
   PortalURL copyToDashboardURL = (PortalURL)request.getAttribute("org.jboss.portal.header.COPY_TO_DASHBOARD_URL");
   PortalURL signOutURL = (PortalURL)request.getAttribute("org.jboss.portal.header.SIGN_OUT_URL");

      out.write('\n');
      out.write('\n');

   if (principal == null)
   {

      out.write('\n');
      out.write('\n');
if(request.getAttribute("ssoEnabled") == null){
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    /* <![CDATA[ */\n");
      out.write("    if (typeof isModalLoaded != 'undefined'){\n");
      out.write("        document.write('<a href=\\\"#\\\" onclick=\\\"alertModal(\\'login-modal\\',\\'login-modal-msg\\');return false;\\\">Login</a>');\n");
      out.write("    }else{\n");
      out.write("        document.write('<a href=\\\"");
      out.print( loginURL );
      out.write("\\\">Login</a>');\n");
      out.write("    }\n");
      out.write("   //set the iframe src for login modal to requested URL\n");
      out.write("   var iframeSrc = '");
      out.print( loginURL );
      out.write("' + '?loginheight=0';\n");
      out.write("   document.getElementById('loginIframe').src = iframeSrc;\n");
      out.write("   /* ]]> */\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("<noscript>\n");
      out.write("      <a href=\"");
      out.print( loginURL );
      out.write('"');
      out.write('>');
      out.print( EntityEncoder.FULL.encode(rb.getString("LOGIN")) );
      out.write("</a>\n");
      out.write("</noscript>\n");
}else{
      out.write("\n");
      out.write("<a href=\"");
      out.print( loginURL );
      out.write('"');
      out.write('>');
      out.print( EntityEncoder.FULL.encode(rb.getString("LOGIN")) );
      out.write("</a>\n");
}
      out.write('\n');
      out.write('\n');
      out.write('\n');

}
else
{

      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("   /* <![CDATA[ */\n");
      out.write("   //we don't need the iframe/modal if logged in\n");
      out.write("   document.getElementById('loginIframe').src = '';\n");
      out.write("   /* ]]> */\n");
      out.write("</script>\n");
      out.print( rb.getString("LOGGED") );
      out.write(':');
      out.write(' ');
      out.print( principal.getName() );
      out.write("<br/><br/>\n");
      out.write("\n");

   if (dashboardURL != null)
   {

      out.write("&nbsp;&nbsp;<a href=\"");
      out.print( dashboardURL );
      out.write('"');
      out.write('>');
      out.print( rb.getString("DASHBOARD") );
      out.write("</a>&nbsp;&nbsp;|");

   }

   if (defaultPortalURL != null)
   {

      out.write("&nbsp;&nbsp;<a href=\"");
      out.print( defaultPortalURL );
      out.write('"');
      out.write('>');
      out.print( rb.getString("PORTAL") );
      out.write("</a>&nbsp;&nbsp;|");

   }

   if (adminPortalURL != null)
   {

      out.write("&nbsp;&nbsp;<a href=\"");
      out.print( adminPortalURL );
      out.write('"');
      out.write('>');
      out.print( rb.getString("ADMIN") );
      out.write("</a>&nbsp;&nbsp;|");

   }

   if (editDashboardURL != null)
   {

      out.write("&nbsp;&nbsp;<a href=\"");
      out.print( editDashboardURL );
      out.write('"');
      out.write('>');
      out.print( rb.getString("CONFIGURE_DASHBOARD") );
      out.write("</a>&nbsp;&nbsp;|");

   }

   if (copyToDashboardURL != null)
   {

      out.write("&nbsp;&nbsp;<a href=\"");
      out.print( copyToDashboardURL );
      out.write('"');
      out.write('>');
      out.print( rb.getString("COPY_TO_DASHBOARD") );
      out.write("</a>&nbsp;&nbsp;|");

   }

      out.write("&nbsp;&nbsp;<a href=\"");
      out.print( signOutURL );
      out.write('"');
      out.write('>');
      out.print( rb.getString("LOGOUT") );
      out.write("</a>\n");

   }

      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
