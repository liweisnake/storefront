<%@page import="org.jboss.portal.widget.netvibes.directory.NetvibesWidgetCategory"%>
<%@page import="org.jboss.portal.widget.netvibes.provider.NetvibesProvider"%>
<%@page import="org.jboss.portal.widget.netvibes.NetvibesPreferenceInfo"%>
<%@page import="org.jboss.portal.widget.netvibes.provider.NetvibesQuery"%>
<%@page import="org.jboss.portal.common.util.IteratorStatus"%>
<%@page import="org.jboss.portal.widget.DirectoryQueryResultEntry"%>
<%@page import="org.jboss.portal.widget.netvibes.NetvibesWidget"%>
<%@page import="org.jboss.portal.widget.netvibes.type.NVDataType"%>
<%@page import="org.jboss.portal.widget.netvibes.type.NVRangeType"%>
<%@page import="org.jboss.portal.widget.netvibes.type.NVListType"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="p" %>
<%@ page isELIgnored="false" %>
<%@page import="org.jboss.portal.widget.Widget"%>
<%@page import="org.jboss.portal.widget.exceptions.WidgetFailure"%>
<%@page import="org.jboss.portal.widget.netvibes.NetvibesWidgetInfo"%>
<%@page import="org.jboss.portal.widget.DirectoryQueryResult"%>
<%@page import="org.jboss.portal.widget.netvibes.provider.NetvibesQueryResult"%>
<%@page import="org.jboss.portal.widget.exceptions.DirectoryResultFailure"%>

<%@page import="org.jboss.portal.widget.netvibes.provider.NetvibesQueryResultEntry"%><p:defineObjects/>
<div>
<%
	NetvibesProvider provider = (NetvibesProvider)request.getAttribute("provider");

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

	//
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
	
	// Type
	String sort = request.getParameter("sort");
	if (sort == null)
	{
		sort = "top";
	}

	// Get category term
	String catTerm = request.getParameter("cat");
	if (catTerm == null)
	{
	   NetvibesWidgetCategory category = (NetvibesWidgetCategory)provider.getCategories().iterator().next();
		catTerm = category.getId();
	}

	// Compute query
	NetvibesQuery query = new NetvibesQuery(currentPage, numberOfResults, catTerm, sort, queryTerm);
	DirectoryQueryResult queryResults = provider.search(query);
	   
	boolean uriPickMethod = false;
    String nvPickMethod = request.getParameter("nv_pick_method");
    if (nvPickMethod != null && nvPickMethod.equals("uri"))
    {
       uriPickMethod = true;
    }
	
%>
<div id="<p:namespace/>selection">
   <input type="RADIO" name="nv_pick_method" value="directory" <%= !uriPickMethod ? "CHECKED" : "" %>
          onclick="document.getElementById('<p:namespace/>directory_search_div').style.display = 'block'; document.getElementById('<p:namespace/>gadget_url_div').style.display = 'none'; ">
   Widget Directory
   <input type="RADIO" name="nv_pick_method" value="uri" <%= uriPickMethod ? "CHECKED" : "" %>
          onclick="document.getElementById('<p:namespace/>directory_search_div').style.display = 'none'; document.getElementById('<p:namespace/>gadget_url_div').style.display = 'block'; ">
   Widget URI
   <br>
   <hr>
</div>

<div id="<p:namespace/>gadget_url_div" style="display: <%= uriPickMethod ? "block" : "none" %>;">
   <%
            PortletURL contentURL = renderResponse.createActionURL();
            contentURL.setParameter("content.action.select", "content.action.select");
            contentURL.setParameter("nv_pick_method", "uri");
   %>
   <form action="<%= contentURL %>" method="post">
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
      %>
      <p style="color: blue;">
         Selected Widget: 
         <%= selWidget.getTitle().getDefaultString() %>
      </p>
      <%
      }
      %>
      <input type="text" name="content.uri" value="<%= uri != null ? uri : "" %>" class="portlet-form-field"/>
      <input type="submit" value="Select Widget" class="portlet-form-button"/>
   </form>
</div>
<div id="<p:namespace/>directory_search_div" style="display: <%= !uriPickMethod ? "block" : "none" %>;">

   <form action="<p:renderURL></p:renderURL>" method="post">
      <input type="text" name="query" value="<%= queryTerm %>" class="portlet-form-field"/>&nbsp;
      <select name="cat" class="portlet-form-field">
         <%
         		// Category dropdown
                         for (Iterator i = provider.getCategories().iterator(); i.hasNext();)
                         {
                            NetvibesWidgetCategory cat = (NetvibesWidgetCategory)i.next();
                            boolean selected = cat.getId().equals(catTerm);
         %>
         <option value="<%= cat.getId() %>" <%= selected ? "selected=\"selected\"" : "" %>><%=cat.getDescription()%>
         </option>
         <%
         				 }
         %>
      </select>&nbsp;
      <select name="numberOfResults" class="portlet-form-field">
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
      <input type="submit" value="Search Widgets" class="portlet-form-button"/>&nbsp;
   </form>
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
      %>
      <p style="color: blue;">
         Selected Widget: 
         <%= selWidget.getTitle().getDefaultString() %>
      </p>
      <%
      }
      %>
   
   <% 
    if (queryResults == null)
    {
       
    } // If widget directory lookup was successful
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
      		int j=0;
            for (IteratorStatus i = new IteratorStatus(queryResults.entries()); i.hasNext();)
            {
               if ( (i.getIndex() + 1) >= numberOfResults)
               {
                  break;
               }
                  
               DirectoryQueryResultEntry result = (DirectoryQueryResultEntry) i.next();
               NetvibesQueryResultEntry nvResult = (NetvibesQueryResultEntry)result; 
               
               // Netvibes API is broken and despite filtering on uwa some non-uwa widget get through, last chance to filter them out
               if ("uwa".equals(nvResult.getType()))
               {
                  j++;
               boolean selected = false;
               if ( selWidget != null
                     && selWidget instanceof NetvibesWidget
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
               selectURL.setParameter("sort", sort);
               selectURL.setParameter("currentPage", String.valueOf(currentPage));
               selectURL.setParameter("numberOfResults", String.valueOf(numberOfResults));

               //
               String rowClass = selected ? "portlet-section-selected" : (j % 2 == 0 ? "portlet-section-body" : "portlet-section-alternate");
                  
      %>
      <tr class="<%= rowClass %>">
         <td>
         
         <a
            href="<%= selectURL %>"><%= (result.getTitle() != null)  ? result.getTitle() : "Untitled" %>
         </a>
         </td>
      </tr>
      <%
            }}
      	if ( queryResults.resultSize() == 0 )
      	{
      	   %><tr>
      	   	<td>Your search did not match any widgets.</td>
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
   				prevURL.setParameter("sort", sort);
   				prevURL.setParameter("numberOfResults", String.valueOf(numberOfResults));
   	   			
   	        	String prevPage = String.valueOf(currentPage - 1);
   	        	
   	        	prevURL.setParameter("currentPage", prevPage);
   			   %><a href="<%=prevURL %>">previous page</a><%
   			}
   		%>
   		</td>
   		<td style="text-align: right;">
   		<%  // next page
   			if ( queryResults.resultSize() == numberOfResults )
   			{
	   			PortletURL nextURL = renderResponse.createRenderURL();
	   			nextURL.setParameter("cat", catTerm);
	   			nextURL.setParameter("query", queryTerm);
	   			nextURL.setParameter("sort", sort);
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
   	} // instanceof NetvibesQueryResult
   	%>

</div>


<%
   if (selWidget != null
         && selWidget instanceof NetvibesWidget )
   {
      NetvibesWidgetInfo widgetInfo = ((NetvibesWidget) selWidget).getWidgetInfo();

      if (! widgetInfo.getPreferencesInfo().getPreferences().isEmpty() )
      {
		PortletURL selectURL = renderResponse.createActionURL();
		// Set parameters for selection
		selectURL.setParameter("content.action.select", "content.action.select");
		selectURL.setParameter("content.uri", selWidget.getId());
      
%>
<div>
	<p>
	<form action="<%= selectURL %>" method="post"><%
		for(Iterator i = widgetInfo.getPreferencesInfo().getPreferences().iterator(); i.hasNext(); )
		{
		   // hidden preferences
		   NetvibesPreferenceInfo prefInfo = (NetvibesPreferenceInfo) widgetInfo.getPreferencesInfo().getPreference((String) i.next());
		   if ( NVDataType.HIDDEN == prefInfo.getType().getOrdinal())
		   {
		      %><input type="hidden" name="content.param.<%= prefInfo.getName() %>"
		      		value="<%= prefInfo.getDefaultValue() != null ? prefInfo.getDefaultValue() : "" %>"/><%
		   }
		 }	
		%>
		<table><%
		   for(Iterator i = widgetInfo.getPreferencesInfo().getPreferences().iterator(); i.hasNext(); )
		   {
		   	String key = (String) i.next();
		   	NetvibesPreferenceInfo prefInfo = (NetvibesPreferenceInfo) widgetInfo.getPreferencesInfo().getPreference(key);

		   	int prefDataType = prefInfo.getType().getOrdinal();
		   	// If preference is not hidden
		   	if ( prefDataType != NVDataType.HIDDEN )
		   	{
	   	
			   	String prefValue = request.getParameter("content.param." + prefInfo.getName());
			   	if ( prefValue == null )
			   	{
			   	   prefValue = prefInfo.getDefaultValue();
			   	}
		 %><tr>
		<td><%= prefInfo.getLabel() != null ? prefInfo.getLabel() : prefInfo.getName() %></td>
		<td><%	// input
				switch(prefDataType) {
				   // RANGE
				   case NVDataType.RANGE: %><select name="content.param.<%= prefInfo.getName() %>" class="portlet-form-field"><%
							NVRangeType range = (NVRangeType) prefInfo.getType();
				   			int start = range.getMin();
				   			int end = range.getMax();
				   			int step = range.getStep();
				   			
				   			for ( int ri = start; ri <= end; ri = ri + step )
				   			{
				   			   boolean selected = String.valueOf(ri).equals(prefValue);
				   			   %><option <%= selected ? "selected=\"selected\"" : "" %> 
				   			   		value="<%= ri %>"><%= ri %></option><%
				   			}
				   %></select><%
				   			break;
				   // BOOLEAN
				   case NVDataType.BOOLEAN:
				   			boolean checked = "true".equals(prefValue);
				   			%><input type="checkbox" name="content.param.<%= prefInfo.getName() %>" value="true" <%= checked ? "checked=\"checked\"" : "" %>/><%
				      		break;
				   // LIST
				   case NVDataType.LIST: %><select name="content.param.<%= prefInfo.getName() %>" class="portlet-form-field"><%
				   			NVListType list = (NVListType) prefInfo.getType();
				   			for(int j = 0; j < list.getSize(); j++)
				   			{
				   			   NVListType.NVListValue value = list.getValue(j);
				   			   boolean selected = value.getValue().equals(prefValue);
				   			   %><option <%= selected ? "selected=\"selected\"" : "" %> 
			   			   		value="<%= value.getValue() %>"><%= value.getLabel().getDefaultString() %></option><%
				   			}
					%></select><%
				      		break;
				   // PASSWORD
				   case NVDataType.PASSWORD: %><input type="password" name="content.param.<%= prefInfo.getName() %>" class="portlet-form-field" 
					 	value="<%= prefValue != null ? prefValue.replace("\"", "'") : "" %>" maxlength="255"/><%
				      		break;
				    // DEFAULT TEXT
				   	default: %><input type="text" name="content.param.<%= prefInfo.getName() %>" class="portlet-form-field" 
							 	value="<%= prefValue != null ? prefValue.replace("\"", "'") : "" %>" /><%
							break;
				}
		%></td>
		</tr><%
		   	} // end if != HIDDEN
		   } // end for preferences
		%>
         <tr>
            <td colspan="2"><input type="submit" value="Update" class="portlet-form-button"/></td>
         </tr>
		</table>
	</form>
	</p>
</div><%
      }
	}
%>
</div>