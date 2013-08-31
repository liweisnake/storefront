package org.apache.jsp.WEB_002dINF.jsp.header;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Iterator;
import org.jboss.portal.api.node.PortalNode;
import java.util.Locale;
import java.util.ArrayList;
import org.jboss.portal.api.PortalRuntimeContext;

public final class tabs_jsp extends org.apache.jasper.runtime.HttpJspBase
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

   PortalNode root = (PortalNode)request.getAttribute("org.jboss.portal.api.PORTAL_NODE");
   PortalNode portal = root;
   PortalNode mainPage = portal;

   while (portal.getType() != PortalNode.TYPE_PORTAL)
   {
      mainPage = portal;
      portal = portal.getParent();
   }

   PortalRuntimeContext context = (PortalRuntimeContext)request.getAttribute("org.jboss.portal.api.PORTAL_RUNTIME_CONTEXT");

   // Get a locale
   Locale locale = request.getLocale();
   if (locale == null)
   {
      locale = Locale.getDefault();
   }

      out.write("\n");
      out.write("\n");
      out.write("<ul id=\"tabsHeader\">\n");
      out.write("   ");

      ArrayList tmp = new ArrayList(10);
      Iterator childrenIt = portal.getChildren().iterator();
      while (childrenIt.hasNext())
      {
         PortalNode child = (PortalNode)childrenIt.next();

         // Get the list of child pages
         tmp.clear();
         for (Iterator i = child.getChildren().iterator(); i.hasNext();)
         {
            PortalNode childChild = (PortalNode)i.next();
            if (childChild.getType() == PortalNode.TYPE_PAGE)
            {
               tmp.add(childChild);
            }
         }
   
      out.write("\n");
      out.write("   <li ");

      if (child == mainPage)
      {
         out.println(" id=\"current\"");
      } 
      out.write(" onmouseover=\"this.className='hoverOn'\"\n");
      out.write("                                                                    onmouseout=\"this.className='hoverOff'\"><a\n");
      out.write("      href=\"");
      out.print( child.createURL(context) );
      out.write('"');
      out.write('>');
      out.print( child.getDisplayName(locale) );

      if (tmp.size() == 0)
      {
   
      out.write("</a>\n");
      out.write("      ");

      }
      else
      {
      
      out.write("\n");
      out.write("      <!--[if IE 7]><!--></a><!--<![endif]-->\n");
      out.write("      <!--[if lte IE 6]>&nbsp;&nbsp;&nbsp;<table><tr><td><![endif]-->\n");
      out.write("      <ul>\n");
      out.write("         ");

            for (Iterator j = tmp.iterator(); j.hasNext();)
            {
               PortalNode childChild = (PortalNode)j.next();
         
      out.write("\n");
      out.write("         <li><a href='");
      out.print( childChild.createURL(context) );
      out.write('\'');
      out.write('>');
      out.print( childChild.getDisplayName(locale) );
      out.write("\n");
      out.write("         </a></li>\n");
      out.write("         ");

            }
         
      out.write("\n");
      out.write("      </ul>\n");
      out.write("      <!--[if lte IE 6]></td></tr></table></a><![endif]-->\n");
      out.write("      ");

         }
      
      out.write("\n");
      out.write("   </li>\n");
      out.write("   ");

      }

   
      out.write("\n");
      out.write("</ul>\n");
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
