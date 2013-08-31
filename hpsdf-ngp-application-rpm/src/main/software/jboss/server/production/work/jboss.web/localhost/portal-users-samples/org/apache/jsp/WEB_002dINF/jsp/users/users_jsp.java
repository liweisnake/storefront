package org.apache.jsp.WEB_002dINF.jsp.users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jboss.portal.core.CoreConstants;

public final class users_jsp extends org.jboss.portal.core.servlet.jsp.PortalJsp
    implements org.apache.jasper.runtime.JspSourceDependent {

static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_0;
static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_1;

static {
  _jspx_fnmap_0= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("n:i18n", org.jboss.portal.core.servlet.jsp.taglib.PortalLib.class, "getMessage", new Class[] {java.lang.String.class});
  _jspx_fnmap_1= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("n:out", org.jboss.portal.core.servlet.jsp.taglib.PortalLib.class, "out", new Class[] {java.lang.String.class});
}

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/WEB-INF/portal-lib.tld");
    _jspx_dependants.add("/WEB-INF/portlet_2_0.tld");
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
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      //  portlet:defineObjects
      org.jboss.portal.portlet.impl.jsr286.taglib.DefineObjects286Tag _jspx_th_portlet_005fdefineObjects_005f0 = new org.jboss.portal.portlet.impl.jsr286.taglib.DefineObjects286Tag();
      org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_portlet_005fdefineObjects_005f0);
      _jspx_th_portlet_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_portlet_005fdefineObjects_005f0 = _jspx_th_portlet_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_portlet_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _jspx_th_portlet_005fdefineObjects_005f0.release();
        org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_portlet_005fdefineObjects_005f0);
        return;
      }
      _jspx_th_portlet_005fdefineObjects_005f0.release();
      org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_portlet_005fdefineObjects_005f0);
      javax.portlet.PortletConfig portletConfig = null;
      javax.portlet.RenderRequest renderRequest = null;
      javax.portlet.RenderResponse renderResponse = null;
      javax.portlet.ActionRequest actionRequest = null;
      javax.portlet.ActionResponse actionResponse = null;
      javax.portlet.ResourceRequest resourceRequest = null;
      javax.portlet.ResourceResponse resourceResponse = null;
      javax.portlet.EventRequest eventRequest = null;
      javax.portlet.EventResponse eventResponse = null;
      javax.portlet.PortletSession portletSession = null;
      java.util.Map portletSessionScope = null;
      javax.portlet.PortletPreferences portletPreferences = null;
      java.util.Map portletPreferencesValues = null;
      portletConfig = (javax.portlet.PortletConfig) _jspx_page_context.findAttribute("portletConfig");
      renderRequest = (javax.portlet.RenderRequest) _jspx_page_context.findAttribute("renderRequest");
      renderResponse = (javax.portlet.RenderResponse) _jspx_page_context.findAttribute("renderResponse");
      actionRequest = (javax.portlet.ActionRequest) _jspx_page_context.findAttribute("actionRequest");
      actionResponse = (javax.portlet.ActionResponse) _jspx_page_context.findAttribute("actionResponse");
      resourceRequest = (javax.portlet.ResourceRequest) _jspx_page_context.findAttribute("resourceRequest");
      resourceResponse = (javax.portlet.ResourceResponse) _jspx_page_context.findAttribute("resourceResponse");
      eventRequest = (javax.portlet.EventRequest) _jspx_page_context.findAttribute("eventRequest");
      eventResponse = (javax.portlet.EventResponse) _jspx_page_context.findAttribute("eventResponse");
      portletSession = (javax.portlet.PortletSession) _jspx_page_context.findAttribute("portletSession");
      portletSessionScope = (java.util.Map) _jspx_page_context.findAttribute("portletSessionScope");
      portletPreferences = (javax.portlet.PortletPreferences) _jspx_page_context.findAttribute("portletPreferences");
      portletPreferencesValues = (java.util.Map) _jspx_page_context.findAttribute("portletPreferencesValues");
      out.write("\n");
      out.write("\n");
      out.write("<div class=\"box\">\n");
      out.write("   <table border=\"0\" class=\"portlet-font\" cellspacing=\"0\" cellpadding=\"2\">\n");
      out.write("      <tr>\n");
      out.write("         <td colspan=\"1\">\n");
      out.write("             <span class=\"portlet-text\">\n");
      out.write("             ");
 
             java.util.LinkedList linkedStack = (java.util.LinkedList)contextStack.get();
             org.jboss.portal.core.servlet.jsp.taglib.context.Context currentContext = 
         	 ((org.jboss.portal.core.servlet.jsp.taglib.context.NamedContext)linkedStack.getLast()).getContext();
             
             if (renderRequest.getPreferences().getValue("guestNumber", "").equals("true")) { 
             			if (Integer.parseInt(currentContext.get("USERS_COUNT")) > 1)
             			{
             			   
      out.write("\n");
      out.write("                ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_ONLINE_0\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("\n");
      out.write("                ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:out(\"USERS_COUNT\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_1, false));
      out.write("\n");
      out.write("                ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_ONLINE_1\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("     \n");
      out.write("\t\t\t\t\t\t");

             			}
             			else
             			{
             			   
      out.write("\n");
      out.write("\t\t\t\t");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"ONE_USER_ONLINE\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("\n");
      out.write("\t\t\t\t\t\t\t");

             			}
             			
      out.write("\n");
      out.write("             <br/><br/> \n");
      out.write("             ");

             }
             if (Integer.parseInt(currentContext.get("USERS_COUNT")) > 1 && !currentContext.get("USERS_LOGGED_COUNT").equals("0") && renderRequest.getPreferences().getValue("loggedNumber", "").equals("true")) { 
      out.write("             \n");
      out.write("\n");
      out.write("                   ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_WHICH_0\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("\n");
      out.write("                   ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:out(\"USERS_LOGGED_COUNT\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_1, false));
      out.write("\n");
      out.write("                   ");
 if (!currentContext.get("USERS_LOGGED_COUNT").equals("1")) { 
      out.write("  \n");
      out.write("                      ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_WHICH_1\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("\n");
      out.write("                   ");
 } else { 
      out.write("  \n");
      out.write("                      ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_WHICH_2\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write(" \n");
      out.write("                   ");
 } 
      out.write("\n");
      out.write("             <br/><br/>\n");
      out.write("             ");
 }
                if (Integer.parseInt(currentContext.get("USERS_COUNT")) > 1 && !currentContext.get("USERS").equals("[]") && renderRequest.getPreferences().getValue("loggedUsers", "").equals("true")) { 
      out.write("                 \n");
      out.write("\n");
      out.write("                ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_ARE_0\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("\n");
      out.write("                ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:out(\"USERS\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_1, false));
      out.write("\n");
      out.write("                ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${n:i18n(\"USERS_ARE_1\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
      out.write("  \n");
      out.write("             ");
 } 
      out.write("\n");
      out.write("             </span>\n");
      out.write("         </td>\n");
      out.write("      </tr>\n");
      out.write("   </table>\n");
      out.write("</div>");
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
