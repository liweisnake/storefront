/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.cms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class CMSMimeMappings
{

   /** Definition of default mime type mappings to file extension. */
   private static final String MAPPINGS_FILE = "org/jboss/portal/cms/mime-mappings.xml";

   /** . */
   private final HashMap mappings = new HashMap();

   public CMSMimeMappings()
   {
      InputStream is = null;
      try
      {
         is = Thread.currentThread().getContextClassLoader().getResourceAsStream(MAPPINGS_FILE);
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

         Document dmt = factory.newDocumentBuilder().parse(is);
         Element docElmt = dmt.getDocumentElement();
      	 NodeList mappingsList = docElmt.getElementsByTagName("mime-mapping");
         for (int i = 0; i < mappingsList.getLength(); i++)
         {
            Element mapping = (Element)mappingsList.item(i);
            String extension = this.asString(this.getUniqueChild(mapping, "extension"));
            String mimeType = this.asString(this.getUniqueChild(mapping, "mime-type"));
            mappings.put(extension, mimeType);
         }
      }
      catch (Exception e)
      {
         throw new Error("Cannot load mime mapping file", e);
      }
      finally
      {
    	  if (is != null) {
              try {
                  is.close();
              } catch (IOException e) {
                  // ignore 
              }
          }
      }
   }

   public String getMimeType(String fileExtension)
   {
      return (String)mappings.get(fileExtension);
   }
   
   /**
    * Get the element's content as a string.
    *
    * @param element the container
    * @throws IllegalArgumentException if the element content is mixed or null
    */
   private static String asString(Element element) throws IllegalArgumentException
   {
      if (element == null)
      {
         throw new IllegalArgumentException("No null element allowed");
      }

      //
      StringBuffer buffer = new StringBuffer();
      NodeList children = element.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
      {
         Node child = children.item(i);
         switch (child.getNodeType())
         {
            case Node.CDATA_SECTION_NODE:
            case Node.TEXT_NODE:
               buffer.append(((Text)child).getData());
               break;
            case Node.ELEMENT_NODE:
               throw new IllegalArgumentException("Mixed content not allowed");
            default:
               break;
         }
      }
      String result = buffer.toString();
      return result.trim();
   }

   /**
    * Return the optional unique child of an element.
    *
    * @param element the parent element
    * @param string  name of the child tag
    * @return the child element or null if it does not exist and strict is false
    * @throws IllegalArgumentException if an argument is null
    */
   private static Element getUniqueChild(Element element, String name) 
       throws IllegalArgumentException
   {
      if (element == null)
      {
         throw new IllegalArgumentException("No element specified");
      }
      Element childElt = null;
      NodeList list = element.getElementsByTagName(name);
      for (int i = 0; i < list.getLength(); i++)
      {
         Node childNode = list.item(i);
         if (childNode instanceof Element)
         {
            if (childElt == null)
            {
               childElt = (Element)childNode;
            }
            else
            {
               throw new IllegalArgumentException("More than one child element for element " + element.getNodeName());
            }
         }
      }
      if (childElt == null)
      {
         throw new IllegalArgumentException("No child element for element " + element.getNodeName());
      }
      return childElt;
   }

}
