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
package com.hp.sdf.ngp.cms.model;

import java.io.Serializable;
import java.util.List;

public class Folder extends CMSObject implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2629963539077591292L;
   protected List<Folder> folders;
   protected List<File> files;

   public List<Folder> getFolders()
   {
      return folders;
   }

   public void setFolders(List<Folder> folders)
   {
      this.folders = folders;
   }

   public List<File> getFiles()
   {
      return files;
   }

   public void setFiles(List<File> files)
   {
      this.files = files;
   }
}

