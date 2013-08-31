/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package com.hp.sdf.ngp.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Helper class for file functions.
 *
 */ 
public class FileUtil
{

   private static int BUFFER_SIZE = 1024;

   /**
    * Generic bytestream util that returns a byte array from an InputStream.
    *
    * @param inputStream
    * @return byte array from stream
    */
   public static byte[] getBytes(InputStream inputStream)
   {
      int bytes = 0;
      ByteArrayOutputStream bytesout = new ByteArrayOutputStream();
      try
      {
         byte in[] = new byte[BUFFER_SIZE];
         while ((bytes = inputStream.read(in, 0, BUFFER_SIZE)) != -1)
         {
            bytesout.write(in, 0, bytes);
         }
         bytesout.flush();
         bytesout.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return null;
      }
      return bytesout.toByteArray();
   }

   public static String cleanDoubleSlashes(String sPath)
   {
      sPath = sPath.replaceAll("//", "/");
      return sPath;
   }
}
