<%@ page import="javax.portlet.ResourceURL" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<!--This tag establishes three objects; renderRequest, renderResponse and portletConfig for use in this JSP page.-->
<portlet:defineObjects/>
<%
ResourceURL resourceURL = renderResponse.createResourceURL();
%>
<script type="text/javascript">
   function ajaxRequest(url,target,params,highlightTarget)
   {
      new Ajax.Updater(target, url, {method: 'GET', asynchronous:true, parameters:params});
      
      if(highlightTarget){
        new Effect.Highlight(document.getElementById(target));
      }

   }
</script>


<div class='full-width' style='padding:5px'>
   <h4>Partial Refresh Repeater Demo</h4>

   <div class='half-width float-left'>

      <!--Simple ajax repeater via a form, note that the action does not matter-->
      <form method='post' id="testrepeatform" name="testrepeatform" action=""
            onsubmit="ajaxRequest('<%=resourceURL%>','repeat-div',Form.serialize(this),true); return false;">
         <font class='portlet-font'>Repeat Demo:</font><br/>
         <input class='portlet-form-input-field' type='text' value='' size='12' name="repeat" id="repeat"
                onkeyup="this.form.submit2.click();"/>
         <input class='portlet-form-input-field hidden' type='submit' name='submit2' value='submit'>
      </form>
   </div>

   <div id="repeat-container">
      <div id="repeat-div" class='half-width float-left' style='height:50px'></div>
   </div>

   <br/><br/>
   <hr/>
   <h4>Partial Refresh Product Catalog</h4>

   <div class='full-width'>

      <div class='float-left third-width'>
         <!--Parameters set on a resource URL are not render parameters but-->
         <!--parameters for serving this resource and will last only for the current serveResource request. -->

         <!--Example 1-->
         <!--Here we set the parameters on the URL via the ajax request-->
         <a href="javascript: ajaxRequest('<%=resourceURL%>','output-div','prodId=1',true);">Product1</a><br/>

         <!--Example 2-->
         <!--For the next 2, we use the resourceURL to set the params-->
         <%resourceURL.setParameter("prodId","2");%>
         <a href="javascript: ajaxRequest('<%=resourceURL%>','output-div','',true);">Product2</a><br/>
         <%resourceURL.setParameter("prodId","3");%>
         <a href="javascript: ajaxRequest('<%=resourceURL%>','output-div','',true);">Product3</a><br/>

         <br class='clear'/></div>

      <div class='float-left two-third-width'>
         <h4 class='zero'>Product Details</h4>

         <div id="output-div"></div>

         <br class='clear'/></div>
   </div>
</div>