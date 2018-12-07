/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.filesystem;

import com.google.common.base.Objects;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.idea.filesystem.IdeaProjectConfig;
import org.eclipse.xtext.idea.resource.VirtualFileURIUtil;
import org.eclipse.xtext.util.internal.Log;
import org.eclipse.xtext.workspace.IProjectConfig;
import org.eclipse.xtext.workspace.IWorkspaceConfig;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@Log
@SuppressWarnings("all")
public class IdeaWorkspaceConfig implements IWorkspaceConfig {
  private final Project project;
  
  @Override
  public Set<? extends IProjectConfig> getProjects() {
    final Computable<com.intellij.openapi.module.Module[]> _function = () -> {
      return ModuleManager.getInstance(this.project).getModules();
    };
    final com.intellij.openapi.module.Module[] modules = ApplicationManager.getApplication().<com.intellij.openapi.module.Module[]>runReadAction(_function);
    final Function1<com.intellij.openapi.module.Module, IdeaProjectConfig> _function_1 = (com.intellij.openapi.module.Module it) -> {
      return this.toIdeaProjectConfig(it);
    };
    return IterableExtensions.<IdeaProjectConfig>toSet(ListExtensions.<com.intellij.openapi.module.Module, IdeaProjectConfig>map(((List<com.intellij.openapi.module.Module>)Conversions.doWrapArray(modules)), _function_1));
  }
  
  @Override
  public IdeaProjectConfig findProjectByName(final String name) {
    final Computable<com.intellij.openapi.module.Module> _function = () -> {
      return ModuleManager.getInstance(this.project).findModuleByName(name);
    };
    final com.intellij.openapi.module.Module module = ApplicationManager.getApplication().<com.intellij.openapi.module.Module>runReadAction(_function);
    if ((module != null)) {
      return this.toIdeaProjectConfig(module);
    } else {
      return null;
    }
  }
  
  private IdeaProjectConfig toIdeaProjectConfig(final com.intellij.openapi.module.Module module) {
    return new IdeaProjectConfig(module);
  }
  
  @Override
  public IdeaProjectConfig findProjectContaining(final URI member) {
    final VirtualFile file = VirtualFileURIUtil.getVirtualFile(member);
    if ((file == null)) {
      return null;
    }
    final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(this.project).getFileIndex();
    final com.intellij.openapi.module.Module module = fileIndex.getModuleForFile(file, true);
    if ((module == null)) {
      return null;
    }
    final VirtualFile contentRoot = fileIndex.getContentRootForFile(file, true);
    if ((contentRoot == null)) {
      return null;
    }
    final IdeaProjectConfig result = new IdeaProjectConfig(module);
    VirtualFile _contentRoot = result.getContentRoot();
    boolean _notEquals = (!Objects.equal(contentRoot, _contentRoot));
    if (_notEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("The file (�member�) should belong to the first content root (�result.contentRoot�) but belongs to �contentRoot�.");
      IdeaWorkspaceConfig.LOG.error(_builder);
    }
    return result;
  }
  
  public IdeaWorkspaceConfig(final Project project) {
    super();
    this.project = project;
  }
  
  @Override
  @Pure
  public int hashCode() {
    return 31 * 1 + ((this.project== null) ? 0 : this.project.hashCode());
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
    IdeaWorkspaceConfig other = (IdeaWorkspaceConfig) obj;
    if (this.project == null) {
      if (other.project != null)
        return false;
    } else if (!this.project.equals(other.project))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("project", this.project);
    return b.toString();
  }
  
  @Pure
  public Project getProject() {
    return this.project;
  }
  
  private final static Logger LOG = Logger.getLogger(IdeaWorkspaceConfig.class);
}
