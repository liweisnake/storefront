<%--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ~ JBoss, a division of Red Hat                                             ~
  ~ Copyright 2006, Red Hat Middleware, LLC, and individual                  ~
  ~ contributors as indicated by the @authors tag. See the                   ~
  ~ copyright.txt in the distribution for a full listing of                  ~
  ~ individual contributors.                                                 ~
  ~                                                                          ~
  ~ This is free software; you can redistribute it and/or modify it          ~
  ~ under the terms of the GNU Lesser General Public License as              ~
  ~ published by the Free Software Foundation; either version 2.1 of         ~
  ~ the License, or (at your option) any later version.                      ~
  ~                                                                          ~
  ~ This software is distributed in the hope that it will be useful,         ~
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of           ~
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU         ~
  ~ Lesser General Public License for more details.                          ~
  ~                                                                          ~
  ~ You should have received a copy of the GNU Lesser General Public         ~
  ~ License along with this software; if not, write to the Free              ~
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA       ~
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.                 ~
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~--%>

<!-- this is static markup to test themes for wsrp css selector support -->
<div id="wsrp_section_selectors">
   <table border="0" width="100%" cellpadding="2">
      <tr>
         <td class="portlet-section-header">portlet-section-header</td>
      </tr>
      <tr>
         <td class="portlet-section-subheader">portlet-section-subheader</td>
      </tr>
      <tr>
         <td class="portlet-section-body">portlet-section-body</td>
      </tr>
      <tr>
         <td class="portlet-section-text">portlet-section-text</td>
      </tr>
      <tr>
         <td class="portlet-section-alternate">portlet-section-alternate</td>
      </tr>
      <tr>
         <td class="portlet-section-selected">portlet-section-selected</td>
      </tr>
      <tr>
         <td class="portlet-section-footer">portlet-section-footer</td>
      </tr>
   </table>
</div>
<br><span class="portlet-section-body">portlet-horizontal-seperator:</span><br>
<hr class="portlet-horizontal-separator">
<br>

<div id="wsrp_table_selectors">
   <table border="1" width="100%" cellpadding="2" cellspacing="0">
      <tr>
         <td class="portlet-table-header">portlet-table-header</td>
      </tr>
      <tr>
         <td class="portlet-table-subheader">portlet-table-subheader</td>
      </tr>
      <tr>
         <td class="portlet-table-alternate">portlet-table-alternate</td>
      </tr>
      <tr>
         <td class="portlet-table-selected">portlet-table-selected</td>
      </tr>
      <tr>
         <td class="portlet-table-body">portlet-table-body</td>
      </tr>
      <tr>
         <td class="portlet-table-text">portlet-table-text</td>
      </tr>
      <tr>
         <td class="portlet-table-footer">portlet-table-footer</td>
      </tr>
   </table>
</div>
<br>

<div id="wsrp_form_selectors">
   <form action="">
      <fieldset>
         <legend>A Sample Form:</legend>
         <table border="0" width="100%" cellpadding="2">
            <tr>
               <td class="portlet-form-label" colspan="2">portlet-form-label</td>
            </tr>
            <tr>
               <td class="portlet-form-field-label" width="140px"><label class="portlet-form-field-label"
                                                                         for="text-field">
                  portlet-form-field-label:</label></td>
               <td align="left"><input type="text" id="text-field" class="portlet-form-input-field"
                                       value="portlet-form-input-field" size="50"/></td>
            </tr>
            <tr>
               <td class="portlet-form-field-label" width="140px"><label class="portlet-form-field-label"
                                                                         for="select-control">
                  portlet-form-field-label:</label></td>
               <td>
                  <select id="select-control" class="portlet-form-field">
                     <option>portlet-form-field</option>
                     <option>
                        Option 2
                     </option>
                  </select>
               </td>
            </tr>
            <tr>
               <td colspan="2"><input type="button" class="portlet-form-button" value="portlet-form-button"/></td>
            </tr>
            <tr>
               <td colspan="2"></td>
            </tr>
         </table>
      </fieldset>
   </form>
</div>
<br>

<div id="wsrp_message_selectors">
   <table border="0" width="100%" cellpadding="2">
      <tr>
         <td class="portlet-msg-status">portlet-msg-status</td>
      </tr>
      <tr>
         <td class="portlet-msg-info">portlet-msg-info</td>
      </tr>
      <tr>
         <td class="portlet-msg-error">portlet-msg-error</td>
      </tr>
      <tr>
         <td class="portlet-msg-alert">portlet-msg-alert</td>
      </tr>
      <tr>
         <td class="portlet-font-dim">portlet-font-dim</td>
      </tr>
      <tr>
         <td class="portlet-font">portlet-font</td>
      </tr>
   </table>
</div>
