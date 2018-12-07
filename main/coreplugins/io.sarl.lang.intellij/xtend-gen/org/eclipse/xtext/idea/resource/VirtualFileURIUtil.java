/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.resource;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Utilities to bridge URIs and VirtualFiles
 */
@SuppressWarnings("all")
public class VirtualFileURIUtil {
  /**
   * @return an EMF URI for a virtual file
   */
  public static URI getURI(final VirtualFile file) {
    boolean _isDirectory = file.isDirectory();
    if (_isDirectory) {
      String _url = file.getUrl();
      String _plus = (_url + "/");
      return URI.createURI(_plus);
    }
    return URI.createURI(file.getUrl());
  }
  
  /**
   * Returns a VirtualFile for the given EMF URI, or <code>null</code> if no VirtualFile exists.
   * Note that a non-existing VirtualFile could be returned as well.
   * 
   * @return a VirtualFile for the given EMF URI, or <code>null</code> if no VirtualFile exists.
   */
  public static VirtualFile getVirtualFile(final URI uri) {
    final String url = VfsUtil.fixURLforIDEA(uri.toString());
    return VirtualFileManager.getInstance().findFileByUrl(url);
  }
  
  /**
   * Will return a VirtualFile and create one if it doesn't already exist.
   * This method will also create all parent folders when needed.
   */
  public static VirtualFile getOrCreateVirtualFile(final URI uri) {
    return VirtualFileURIUtil.getOrCreateFile(uri, false);
  }
  
  private static VirtualFile getOrCreateFile(final URI uri, final boolean isDirectory) {
    try {
      final VirtualFile file = VirtualFileURIUtil.getVirtualFile(uri);
      if ((file != null)) {
        return file;
      }
      int _segmentCount = uri.segmentCount();
      boolean _equals = (_segmentCount == 0);
      if (_equals) {
        throw new IllegalStateException(("couldn\'t find virtual file for " + uri));
      }
      final VirtualFile parent = VirtualFileURIUtil.getOrCreateFile(uri.trimSegments(1), true);
      if (isDirectory) {
        return VfsUtil.createDirectoryIfMissing(parent, uri.lastSegment());
      } else {
        return parent.findOrCreateChildData(uri, uri.lastSegment());
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
