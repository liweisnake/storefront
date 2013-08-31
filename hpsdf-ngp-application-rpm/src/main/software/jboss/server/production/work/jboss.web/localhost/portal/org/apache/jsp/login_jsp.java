package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jboss.portal.identity.UserStatus;
import org.jboss.portal.server.ParameterSanitizer;
import java.util.ResourceBundle;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html;charset=utf-8");
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
   // todo: use ParameterValidation.sanitize after 2.7.1
   String loginheight = request.getParameter("loginheight");
   boolean paramPresent = loginheight != null;
   loginheight = ParameterSanitizer.sanitizeFromPattern(loginheight, ParameterSanitizer.CSS_DISTANCE, "300px");

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
      out.write("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
      out.write("\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
      out.write("<head>\n");
      out.write("   <title>");
      out.print( rb.getString("LOGIN_TITLE")  );
      out.write("\n");
      out.write("   </title>\n");
      out.write("   <style type=\"text/css\">\n");
      out.write("      /* <![CDATA[ */\n");
      out.write("      body {\n");
      out.write("         margin: 0;\n");
      out.write("         padding: 0;\n");
      out.write("         border: 0;\n");
      out.write("         padding-top: ");
      out.print(loginheight);
      out.write(";\n");
      out.write("      }\n");
      out.write("\n");
      out.write("      /* ]]> */\n");
      out.write("   </style>\n");
      out.write("\n");
      out.write("   <script>\n");
      out.write("      function setFocusOnLoginForm()\n");
      out.write("      {\n");
      out.write("         try\n");
      out.write("         {\n");
      out.write("            document.loginform.j_username.focus();\n");
      out.write("         }\n");
      out.write("         catch (e)\n");
      out.write("         {\n");
      out.write("         }\n");
      out.write("      }\n");
      out.write("   </script>\n");
      out.write("\n");
      out.write("   <link rel=\"stylesheet\" href=\"/portal-core/css/login.css\" type=\"text/css\"/>\n");
      out.write("</head>\n");
      out.write("<body onload=\"setFocusOnLoginForm();\">\n");
      out.write("\n");
      out.write("<div class=\"login-container\">\n");
      out.write("\n");
      out.write("\n");
      out.write("   <div class=\"login-header\">\n");
      out.write("      <h2>");
      out.print( rb.getString("LOGIN_TITLE") );
      out.write("\n");
      out.write("      </h2>\n");
      out.write("   </div>\n");
      out.write("   <div class=\"login-content\">\n");
      out.write("\n");
      out.write("      <div class=\"error-message\"\n");
      out.write("           style=\"");
      out.print((request.getAttribute(!UserStatus.OK.equals("org.jboss.portal.userStatus") ? "" : "display:none")));
      out.write(";\">\n");
      out.write("         ");


            if (UserStatus.DISABLE.equals(request.getAttribute("org.jboss.portal.userStatus")))
            {
               out.println(rb.getString("ACCOUNT_DISABLED"));
            }
            else if (UserStatus.WRONGPASSWORD.equals(request.getAttribute("org.jboss.portal.userStatus")) || UserStatus.UNEXISTING.equals(request.getAttribute("org.jboss.portal.userStatus")))
            {
               out.println(rb.getString("ACCOUNT_INEXISTING_OR_WRONG_PASSWORD"));
            }
            else if (UserStatus.NOTASSIGNEDTOROLE.equals(request.getAttribute("org.jboss.portal.userStatus")))
            {
               out.println(rb.getString("ACCOUNT_NOTASSIGNEDTOROLE"));
            }
         
      out.write("\n");
      out.write("      </div>\n");
      out.write("      <form method=\"post\" action=\"");
      out.print( response.encodeURL("j_security_check") );
      out.write("\" name=\"loginform\" id=\"loginForm\"\n");
      out.write("            target=\"_parent\">\n");
      out.write("\t     <table align=\"center\">\n");
      out.write("\t        <tr class=\"form-field\">\n");
      out.write("\t           <td><label for=\"j_username\" style=\"white-space: nowrap;\">");
      out.print( rb.getString("LOGIN_USERNAME") );
      out.write("</label></td>\n");
      out.write("               <td><input type=\"text\" name=\"j_username\" id=\"j_username\" value=\"\" size=\"12\"/></td>\n");
      out.write("            </tr>\n");
      out.write("            <tr class=\"form-field\">\n");
      out.write("               <td><label for=\"j_password\" style=\"white-space: nowrap;\">");
      out.print( rb.getString("LOGIN_PASSWORD") );
      out.write("</label></td>\n");
      out.write("               <td><input type=\"password\" name=\"j_password\" id=\"j_password\" value=\"\" size=\"12\"/></td>\n");
      out.write("            </tr>\n");
      out.write("         </table>\n");
      out.write("         <div class=\"button-container\">\n");
      out.write("            <br class=\"clear\"/>\n");
      out.write("            <input style=\"");
      out.print(paramPresent ? "" : "display:none");
      out.write(";\" type=\"button\" name=\"cancel\"\n");
      out.write("                   value=\"");
      out.print( rb.getString("LOGIN_CANCEL")  );
      out.write("\" class=\"cancel-button\"\n");
      out.write("                   onclick=\"window.parent.hideContentModal('login-modal');\"/>\n");
      out.write("            <br class=\"clear\"/>\n");
      out.write("            <input style=\"");
      out.print(paramPresent ? "" : "right:10px");
      out.write(";\" type=\"submit\" name=\"login\"\n");
      out.write("                   value=\"");
      out.print( rb.getString("LOGIN_SUBMIT")  );
      out.write("\" class=\"login-button\"/>\n");
      out.write("         </div>\n");
      out.write("         <br class=\"clear\"/>\n");
      out.write("      </form>\n");
      out.write("\n");
      out.write("   </div>\n");
      out.write("</div>\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
