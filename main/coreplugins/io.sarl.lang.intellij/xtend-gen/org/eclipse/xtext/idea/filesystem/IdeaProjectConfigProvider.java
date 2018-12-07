/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.filesystem;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.idea.filesystem.IdeaProjectConfig;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.workspace.IProjectConfigProvider;

@SuppressWarnings("all")
public class IdeaProjectConfigProvider implements IProjectConfigProvider {
  @Override
  public IdeaProjectConfig getProjectConfig(final ResourceSet context) {
    if ((context instanceof XtextResourceSet)) {
      final Object uriContext = ((XtextResourceSet)context).getClasspathURIContext();
      if ((uriContext instanceof com.intellij.openapi.module.Module)) {
        return this.getProjectConfig(((com.intellij.openapi.module.Module)uriContext));
      }
    }
    throw new IllegalArgumentException("Could not determine the project");
  }
  
  public IdeaProjectConfig getProjectConfig(final com.intellij.openapi.module.Module module) {
    return new IdeaProjectConfig(module);
  }
}
