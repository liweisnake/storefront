<%@ page import="org.jboss.portal.common.util.IteratorStatus" %>
<%@ page import="org.jboss.portal.widget.DirectoryQueryResultEntry" %>
<%@ page import="org.jboss.portal.widget.google.GGWidget" %>
<%@ page import="org.jboss.portal.widget.google.info.GGPreferenceInfo" %>
<%@ page import="org.jboss.portal.widget.google.info.GGWidgetCategoryInfo" %>
<%@ page import="org.jboss.portal.widget.google.provider.GGProvider" %>
<%@ page import="org.jboss.portal.widget.google.provider.GGQuery" %>
<%@ page import="org.jboss.portal.widget.google.provider.GGQueryResult" %>
<%@ page import="org.jboss.portal.widget.google.type.DataType" %>
<%@ page import="org.jboss.portal.widget.google.type.EnumType" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p" %>
<%@ page isELIgnored="false" %>

<%@page import="org.jboss.portal.widget.Widget"%>
<%@page import="org.jboss.portal.widget.exceptions.WidgetFailure"%>
<%@page import="org.jboss.portal.widget.google.info.GGWidgetInfo"%>
<%@page import="org.jboss.portal.widget.DirectoryQueryResult"%>
<%@page import="org.jboss.portal.widget.exceptions.DirectoryResultFailure"%>
<p:defineObjects/>
<div>

<%
   // Get useful request attributes
   GGProvider provider = (GGProvider)request.getAttribute("provider");

   // Get the selected widget if any
   Widget selWidget = null;
   String uri = request.getParameter("content.uri");
   if (uri != null)
   {
      selWidget = provider.getWidget(uri);
   }

   // Number of results to display
   int numberOfResults = 10;
   String resultSize= request.getParameter("numberOfResults");
   if (resultSize != null && !resultSize.equals(""))
   {
      numberOfResults = Integer.parseInt(resultSize);
   }

   // Pagination
   int currentPage = 0;
   String tempPage = request.getParameter("currentPage"); 
   if ( tempPage != null && !tempPage.equals(""))
   {
      int temp = Integer.parseInt(tempPage);
      currentPage = temp < 0 ? 0 : temp;
   }
   
   // Get query term
   String queryTerm = request.getParameter("query");
   if (queryTerm == null)
   {
      queryTerm = "";
   }

   // Get category term
   String catTerm = request.getParameter("cat");
   if (catTerm == null)
   {
      catTerm = "";
   }

   // Compute query
   int queryStart = currentPage * numberOfResults;
   GGQuery query = new GGQuery(queryStart, numberOfResults + 1, catTerm, queryTerm, request.getLocale());
   DirectoryQueryResult queryResults = provider.search(query);

   String ggPickMethod = request.getParameter("gg_pick_method");
   boolean uriPickMethod = false;
   if (ggPickMethod != null && ggPickMethod.equals("uri"))
   {
      uriPickMethod = true;
   }
   else
   {
   }
%>
<div id="<p:namespace/>selection">
   <input id="radio1" type="RADIO" name="gg_pick_method" value="directory" <%= !uriPickMethod ? "CHECKED" : "" %>
          onclick="document.getElementById('<p:namespace/>directory_search_div').style.display = 'block'; document.getElementById('<p:namespace/>gadget_url_div').style.display = 'none'; ">
   Gadget Directory
   <input id="raido2" type="RADIO" name="gg_pick_method" value="uri" <%= uriPickMethod ? "CHECKED" : "" %>
          onclick="document.getElementById('<p:namespace/>directory_search_div').style.display = 'none'; document.getElementById('<p:namespace/>gadget_url_div').style.display = 'block'; ">
   Gadget URI
   <br>
   <hr>
</div>

<div id="<p:namespace/>gadget_url_div" style="display: <%= uriPickMethod ? "block" : "none" %>;">
   <%
            PortletURL contentURL = renderResponse.createActionURL();
            contentURL.setParameter("content.action.select", "content.action.select");
            contentURL.setParameter("gg_pick_method", "uri");
   %>
   <form id="widget-form" action="<%= contentURL %>" method="post">

      <%
                     if (uri != null && selWidget instanceof WidgetFailure)
                     {
                        WidgetFailure failure = (WidgetFailure) selWidget;
      %>
      <p class="portlet-msg-error">
         <%= failure.getLocalizedErrorMessage( request.getLocale() ) %>
      </p>
      <%
                  }
                  else if (selWidget != null)
                  {
                     GGWidget widget = (GGWidget) selWidget; 
      %>
      <p style="color: blue;">
         Selected
         Widget: <%= widget.getDirectoryTitle().getDefaultString().length() > 0 ? widget.getDirectoryTitle().getDefaultString() : widget.getTitle().getDefaultString()%>
      </p>
      <%
      }
      %>

      <input id="text-input" type="text" name="content.uri" value="<%= uri != null ? uri : "" %>" class="portlet-form-field"/>
      <input id="submit" type="submit" value="Select Gadget" class="portlet-form-button"/>
   </form>
</div>

<div id="<p:namespace/>directory_search_div" style="display: <%= !uriPickMethod ? "block" : "none" %>;">

   <form id="widget-form1" action="<p:renderURL></p:renderURL>" method="post">
      <input id="query-input" type="text" name="query" value="<%= queryTerm %>" class="portlet-form-field"/>&nbsp;
      <select id="select-combo" name="cat" class="portlet-form-field">
         <%
         		// Category dropdown
                         for (Iterator i = provider.getCategories().iterator(); i.hasNext();)
                         {
                            GGWidgetCategoryInfo cat = (GGWidgetCategoryInfo)i.next();
                            boolean selected = cat.getName().equals(catTerm);
         %>
         <option value="<%= cat.getName() %>" <%= selected ? "selected=\"selected\"" : "" %>><%=cat.getDisplayName()%>
         </option>
         <%
         				 }
         %>
      </select>&nbsp;
      <select id="num-res-combo" name="numberOfResults" class="portlet-form-field">
      	<%
      			// Number of results dropdown
      			for(int i = 5; i <= 25; i = i + 5)
      			{
      			   	boolean selected = i == numberOfResults;
      				%><option value="<%= i %>" <%= selected ? "selected=\"selected\"" : "" %>><%= i %></option><%
      			}
      	%>
      </select>
      <br/>
      <input id="submit-button" type="submit" value="Search Gadgets" class="portlet-form-button"/>
      <%
                     if (uri != null && selWidget instanceof WidgetFailure)
                     {
                        WidgetFailure failure = (WidgetFailure) selWidget;
      %>
      <p class="portlet-msg-error">
         <%= failure.getLocalizedErrorMessage( request.getLocale() ) %>
      </p>
      <%
                  }
                  else if (selWidget != null)
                  {
                     GGWidget widget = (GGWidget) selWidget; 
      %>
      <p style="color: blue;">
         Selected
         Widget: <%= widget.getDirectoryTitle().getDefaultString().length() > 0 ? widget.getDirectoryTitle().getDefaultString() : widget.getTitle().getDefaultString()%>
      </p>
      <%
      }
      %>
   </form>

   <% // If widget directory lookup was successful
    if (queryResults == null)
    {
       
    }
    else if ( queryResults instanceof DirectoryResultFailure )
   	{
   		DirectoryResultFailure resultFailure = (DirectoryResultFailure) queryResults;
   	   %>
   	   <p class="portlet-msg-error"><%= resultFailure.getLocalizedErrorMessage( request.getLocale() ) %></p>
   	   <%
   	}
   	else
   	{
   %>
   <p>
   <table style="width:100%;" class="portlet-def-table datatable" cellspacing="0" cellpadding="0">
	<thead class="portlet-section-header">
		<tr>
			<td>Search results</td>
		</tr>
	</thead>
      <%
            for (IteratorStatus i = new IteratorStatus(queryResults.entries()); i.hasNext();)
            {
               if ( (i.getIndex() + 1) >= numberOfResults)
               {
                  break;
               }
                  
               DirectoryQueryResultEntry result = (DirectoryQueryResultEntry) i.next();
               boolean selected = false;
               if ( selWidget != null
                     && selWidget instanceof GGWidget
                	 && selWidget.getId().equals(result.getURL().toString())
               )
               {
                  selected = true;
               }
               PortletURL selectURL = renderResponse.createActionURL();
               
               // Set parameters for selection
               selectURL.setParameter("content.action.select", "content.action.select");
               selectURL.setParameter("content.uri", result.getURL().toString());

               // Set default parametrization state

               // Propagage search nav state
               selectURL.setParameter("cat", catTerm);
               selectURL.setParameter("query", queryTerm);
               selectURL.setParameter("currentPage", String.valueOf(currentPage));
               selectURL.setParameter("numberOfResults", String.valueOf(numberOfResults));

               //
               String rowClass = selected ? "portlet-section-selected" : (i.getIndex() % 2 == 0 ? "portlet-section-body" : "portlet-section-alternate");
      %>
      <tr class="<%= rowClass %>">
         <td><a
            href="<%= selectURL %>"><%= result.getTitle() %>
         </a></td>
      </tr>
      <%
            }
    	if ( queryResults.resultSize() == 0 )
      	{
      	   %><tr>
      	   	<td>Your search did not match any gadgets.</td>
      	   </tr><%
      	}
      %>
   </table>
   <table style="width:100%; border: none;" cellspacing="0" cellpadding="0">
   	<tr>
   		<td style="test-align: left;">
   		<% // previous page
   			if ( currentPage > 0 )
   			{
   				PortletURL prevURL = renderResponse.createRenderURL();
   				prevURL.setParameter("cat", catTerm);
   				prevURL.setParameter("query", queryTerm);
   				prevURL.setParameter("numberOfResults", String.valueOf(numberOfResults));
   	   			
   	        	String prevPage = String.valueOf(currentPage - 1);
   	        	
   	        	prevURL.setParameter("currentPage", prevPage);
   			   %><a href="<%=prevURL %>">previous page</a><%
   			}
   		%>
   		</td>
   		<td style="text-align: right;">
   		<%  // next page
   			if ( queryResults.resultSize() > numberOfResults )
   			{
	   			PortletURL nextURL = renderResponse.createRenderURL();
	   			nextURL.setParameter("cat", catTerm);
	   			nextURL.setParameter("query", queryTerm);
	   			nextURL.setParameter("numberOfResults", String.valueOf(numberOfResults));
	        	
	        	String nextPage = String.valueOf(currentPage + 1);
	        	
	        	nextURL.setParameter("currentPage", nextPage);
   		%><a href="<%= nextURL %>">next page</a><%
   			}
   		%>
   		</td>
   	</tr>
   </table>
   </p>
   <%
   	} // instanceof GGQueryResult
   	%>


</div>
<div>
<%
   if (selWidget != null && selWidget instanceof GGWidget) 
    {
      GGWidgetInfo widgetInfo = ((GGWidget) selWidget).getInfo();
      
      if (! widgetInfo.getPreferences().getPreferences().isEmpty())
      {
      PortletURL selectURL = renderResponse.createActionURL();

      // Set parameters for selection
      selectURL.setParameter("content.action.select", "content.action.select");
      selectURL.setParameter("content.uri", selWidget.getId());

      // Propagage search nav state
      selectURL.setParameter("cat", catTerm);
      selectURL.setParameter("query", queryTerm);
      selectURL.setParameter("currentPage", String.valueOf(currentPage));
      selectURL.setParameter("numberOfResults", String.valueOf(numberOfResults));
      
      //propagate visable div
      if (uriPickMethod)
      {
         selectURL.setParameter("gg_pick_method", "uri");
      }
%>
<p>

<form action="<%= selectURL %>" method="post">
   <%
      for (Iterator i = widgetInfo.getPreferences().getPreferences().iterator(); i.hasNext();)
      {
         GGPreferenceInfo prefInfo = (GGPreferenceInfo)i.next();
         if (prefInfo.getType().getOrdinal() == DataType.HIDDEN)
         {

   %>
   <input type="hidden" name="content.param.<%= prefInfo.getName() %>"
          value="<%= prefInfo.getDefaultValue() != null ? prefInfo.getDefaultValue() : "" %>"/>
   <%
         }
      }
   %>
   <table>
      <tbody>
         <%
            for (Iterator i = widgetInfo.getPreferences().getPreferences().iterator(); i.hasNext();)
            {
               GGPreferenceInfo prefInfo = (GGPreferenceInfo)i.next();

               // Get param value from nav state otherwise we use the default value
               String prefValue = request.getParameter("content.param." + prefInfo.getName());
               int prefType = prefInfo.getType().getOrdinal(); 
               if (prefValue == null)
               {
                  prefValue = prefInfo.getDefaultValue();
               }
               if (prefValue == null)
               {
                  prefValue = "";
               }
               
               if ( prefType != DataType.HIDDEN )
               {
         %>
         <tr>
            <td><%= prefInfo.getDisplayName() != null ? prefInfo.getDisplayName() : prefInfo.getName() %>:</td>
            <%
               switch (prefType)
               {
                  case DataType.HIDDEN:
                     break;
                  case DataType.ENUM:
            %>
            <td><select name="content.param.<%= prefInfo.getName() %>" class="portlet-form-field">
               <%
                  EnumType e = (EnumType)prefInfo.getType();
                  for (int j = 0; j < e.getSize(); j++)
                  {
                     EnumType.Value value = e.getValue(j);
                     boolean selected = value.getValue().equals(prefValue);
               %>
               <option <%= selected ? "selected=\"selected\"" : "" %>
                  value="<%= value.getValue() %>"><%= value.getDisplayValue() != null ? value.getDisplayValue() : value.getValue() %>
               </option>
               <%
                  }
               %>
            </select></td>
            <%
                  break;
               default:
            %>
            <td><input type="text" name="content.param.<%= prefInfo.getName() %>" class="portlet-form-field"
                       value="<%= prefValue.replace("\"", "'") %>"/></td>
            <%
                     break;
               } // endof switch
            %>
         </tr>
         <%
               } // endof if != hidden
            } // for preferences
         %>
         <tr>
            <td colspan="2"><input type="submit" value="Update" class="portlet-form-button"/></td>
         </tr>
      </tbody>
   </table>
</form>
</p>
<%
      }
   }
%>
</div>

</div>
   