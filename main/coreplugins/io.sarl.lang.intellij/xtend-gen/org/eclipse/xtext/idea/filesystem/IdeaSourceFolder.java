/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.filesystem;

import com.intellij.openapi.roots.SourceFolder;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.idea.extensions.RootModelExtensions;
import org.eclipse.xtext.util.UriUtil;
import org.eclipse.xtext.workspace.ISourceFolder;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class IdeaSourceFolder implements ISourceFolder {
  private final SourceFolder folder;
  
  @Override
  public String getName() {
    return RootModelExtensions.getRelativePath(this.folder);
  }
  
  @Override
  public URI getPath() {
    return UriUtil.toFolderURI(URI.createURI(this.folder.getFile().getUrl()));
  }
  
  public IdeaSourceFolder(final SourceFolder folder) {
    super();
    this.folder = folder;
  }
  
  @Override
  @Pure
  public int hashCode() {
    return 31 * 1 + ((this.folder== null) ? 0 : this.folder.hashCode());
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    IdeaSourceFolder other = (IdeaSourceFolder) obj;
    if (this.folder == null) {
      if (other.folder != null)
        return false;
    } else if (!this.folder.equals(other.folder))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("folder", this.folder);
    return b.toString();
  }
  
  @Pure
  public SourceFolder getFolder() {
    return this.folder;
  }
}
