package org.apache.jsp.layouts.generic;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jboss.portal.server.PortalConstants;
import java.util.ResourceBundle;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(3);
    _jspx_dependants.add("/WEB-INF/theme/portal-layout.tld");
    _jspx_dependants.add("/layouts/common/modal_head.jsp");
    _jspx_dependants.add("/layouts/common/modal_body.jsp");
  }

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

      out.write('\n');
      out.write('\n');
      out.write('\n');
 ResourceBundle rb = ResourceBundle.getBundle("Resource", request.getLocale()); 
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
      out.write("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
      out.write("<head>\n");
      out.write("    <title>");
      //  p:title
      org.jboss.portal.theme.tag.TitleTagHandler _jspx_th_p_005ftitle_005f0 = new org.jboss.portal.theme.tag.TitleTagHandler();
      org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005ftitle_005f0);
      _jspx_th_p_005ftitle_005f0.setJspContext(_jspx_page_context);
      // /layouts/generic/index.jsp(9,11) name = default type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_p_005ftitle_005f0.setDefault( PortalConstants.VERSION.toString() );
      _jspx_th_p_005ftitle_005f0.doTag();
      org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005ftitle_005f0);
      out.write("</title>\n");
      out.write("   <meta http-equiv=\"Content-Type\" content=\"text/html;\"/>\n");
      out.write("   <!-- to correct the unsightly Flash of Unstyled Content. -->\n");
      out.write("   <script type=\"text/javascript\"></script>\n");
      out.write("   <!-- inject the theme, default to the Renewal theme if nothing is selected for the portal or the page -->\n");
      out.write("   ");
      if (_jspx_meth_p_005ftheme_005f0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("   <!-- insert header content that was possibly set by portlets on the page -->\n");
      out.write("   ");
      if (_jspx_meth_p_005fheaderContent_005f0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("   ");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/js/modal.js\" type=\"text/javascript\"></script>\n");
      out.write("<link rel=\"stylesheet\" href=\"");
      out.print(request.getContextPath());
      out.write("/css/modal.css\" type=\"text/css\" />");
      out.write("\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body id=\"body\">\n");
      if (_jspx_meth_p_005fregion_005f0(_jspx_page_context))
        return;
      out.write('\n');
      out.write("   <div id=\"login-modal\" style=\"display:none\">\n");
      out.write("      <div id=\"login-modal-msg\" style=\"display:none;width:257px;height:157px\">\n");
      out.write("      <iframe src=\"\" frameborder=\"0\" width=\"257\" height=\"157\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" name=\"login-content\" class=\"login-content\" id=\"loginIframe\"></iframe>\n");
      out.write("      </div>\n");
      out.write("   </div>");
      out.write("\n");
      out.write("<div id=\"portal-container\">\n");
      out.write("   <div id=\"sizer\">\n");
      out.write("      <div id=\"expander\">\n");
      out.write("         <div id=\"logoName\"></div>\n");
      out.write("         <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"header-container\">\n");
      out.write("            <tr> \n");
      out.write("               <td align=\"center\" valign=\"top\" id=\"header\">\n");
      out.write("\n");
      out.write("                  <!-- Utility controls -->\n");
      out.write("                  ");
      if (_jspx_meth_p_005fregion_005f1(_jspx_page_context))
        return;
      out.write("\n");
      out.write("\n");
      out.write("                  <!-- navigation tabs and such -->\n");
      out.write("                  ");
      if (_jspx_meth_p_005fregion_005f2(_jspx_page_context))
        return;
      out.write("\n");
      out.write("                  <div id=\"spacer\"></div>\n");
      out.write("               </td>\n");
      out.write("            </tr>\n");
      out.write("         </table>\n");
      out.write("         <div id=\"content-container\">\n");
      out.write("            <!-- insert the content of the 'left' region of the page, and assign the css selector id 'regionA' -->\n");
      out.write("            ");
      if (_jspx_meth_p_005fregion_005f3(_jspx_page_context))
        return;
      out.write("\n");
      out.write("            <!-- insert the content of the 'center' region of the page, and assign the css selector id 'regionB' -->\n");
      out.write("            ");
      if (_jspx_meth_p_005fregion_005f4(_jspx_page_context))
        return;
      out.write("\n");
      out.write("            <hr class=\"cleaner\"/>\n");
      out.write("         </div>\n");
      out.write("      </div>\n");
      out.write("   </div>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("<div id=\"footer-container\" class=\"portal-copyright\">");
      out.print( rb.getString("POWERED_BY") );
      out.write("\n");
      out.write("<a class=\"portal-copyright\" href=\"http://www.redhat.com/jboss/platforms/portals/\">JBoss Enterprise Portal Platform</a><br/>\n");
      out.write("</div>\n");
      out.write("\n");
      if (_jspx_meth_p_005fregion_005f5(_jspx_page_context))
        return;
      out.write("\n");
      out.write("\n");
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

  private boolean _jspx_meth_p_005ftheme_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:theme
    org.jboss.portal.theme.tag.ThemeTagHandler _jspx_th_p_005ftheme_005f0 = new org.jboss.portal.theme.tag.ThemeTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005ftheme_005f0);
    _jspx_th_p_005ftheme_005f0.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(14,3) name = themeName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005ftheme_005f0.setThemeName("renewal");
    _jspx_th_p_005ftheme_005f0.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005ftheme_005f0);
    return false;
  }

  private boolean _jspx_meth_p_005fheaderContent_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:headerContent
    org.jboss.portal.theme.tag.HeaderContentTagHandler _jspx_th_p_005fheaderContent_005f0 = new org.jboss.portal.theme.tag.HeaderContentTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fheaderContent_005f0);
    _jspx_th_p_005fheaderContent_005f0.setJspContext(_jspx_page_context);
    _jspx_th_p_005fheaderContent_005f0.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fheaderContent_005f0);
    return false;
  }

  private boolean _jspx_meth_p_005fregion_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:region
    org.jboss.portal.theme.tag.RegionTagHandler _jspx_th_p_005fregion_005f0 = new org.jboss.portal.theme.tag.RegionTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f0);
    _jspx_th_p_005fregion_005f0.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(21,0) name = regionName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f0.setRegionName("AJAXScripts");
    // /layouts/generic/index.jsp(21,0) name = regionID type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f0.setRegionID("AJAXScripts");
    _jspx_th_p_005fregion_005f0.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f0);
    return false;
  }

  private boolean _jspx_meth_p_005fregion_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:region
    org.jboss.portal.theme.tag.RegionTagHandler _jspx_th_p_005fregion_005f1 = new org.jboss.portal.theme.tag.RegionTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f1);
    _jspx_th_p_005fregion_005f1.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(32,18) name = regionName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f1.setRegionName("dashboardnav");
    // /layouts/generic/index.jsp(32,18) name = regionID type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f1.setRegionID("dashboardnav");
    _jspx_th_p_005fregion_005f1.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f1);
    return false;
  }

  private boolean _jspx_meth_p_005fregion_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:region
    org.jboss.portal.theme.tag.RegionTagHandler _jspx_th_p_005fregion_005f2 = new org.jboss.portal.theme.tag.RegionTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f2);
    _jspx_th_p_005fregion_005f2.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(35,18) name = regionName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f2.setRegionName("navigation");
    // /layouts/generic/index.jsp(35,18) name = regionID type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f2.setRegionID("navigation");
    _jspx_th_p_005fregion_005f2.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f2);
    return false;
  }

  private boolean _jspx_meth_p_005fregion_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:region
    org.jboss.portal.theme.tag.RegionTagHandler _jspx_th_p_005fregion_005f3 = new org.jboss.portal.theme.tag.RegionTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f3);
    _jspx_th_p_005fregion_005f3.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(42,12) name = regionName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f3.setRegionName("left");
    // /layouts/generic/index.jsp(42,12) name = regionID type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f3.setRegionID("regionA");
    _jspx_th_p_005fregion_005f3.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f3);
    return false;
  }

  private boolean _jspx_meth_p_005fregion_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:region
    org.jboss.portal.theme.tag.RegionTagHandler _jspx_th_p_005fregion_005f4 = new org.jboss.portal.theme.tag.RegionTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f4);
    _jspx_th_p_005fregion_005f4.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(44,12) name = regionName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f4.setRegionName("center");
    // /layouts/generic/index.jsp(44,12) name = regionID type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f4.setRegionID("regionB");
    _jspx_th_p_005fregion_005f4.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f4);
    return false;
  }

  private boolean _jspx_meth_p_005fregion_005f5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  p:region
    org.jboss.portal.theme.tag.RegionTagHandler _jspx_th_p_005fregion_005f5 = new org.jboss.portal.theme.tag.RegionTagHandler();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f5);
    _jspx_th_p_005fregion_005f5.setJspContext(_jspx_page_context);
    // /layouts/generic/index.jsp(55,0) name = regionName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f5.setRegionName("AJAXFooter");
    // /layouts/generic/index.jsp(55,0) name = regionID type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_p_005fregion_005f5.setRegionID("AJAXFooter");
    _jspx_th_p_005fregion_005f5.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_p_005fregion_005f5);
    return false;
  }
}
