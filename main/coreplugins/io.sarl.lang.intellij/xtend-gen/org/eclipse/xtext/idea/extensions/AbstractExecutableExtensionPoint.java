/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.extensions;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionFactory;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.util.xmlb.annotations.Attribute;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public abstract class AbstractExecutableExtensionPoint extends AbstractExtensionPointBean {
  @Attribute("factoryClass")
  private String factoryClass;
  
  @Attribute("class")
  private String implementationClass;
  
  public String getContributor() {
    PluginDescriptor _pluginDescriptor = this.getPluginDescriptor();
    PluginId _pluginId = null;
    if (_pluginDescriptor!=null) {
      _pluginId=_pluginDescriptor.getPluginId();
    }
    String _string = null;
    if (_pluginId!=null) {
      _string=_pluginId.toString();
    }
    return _string;
  }
  
  public Object createInstance() {
    try {
      Object _xblockexpression = null;
      {
        if ((this.implementationClass == null)) {
          throw this.asRuntimeException("Class is not specified");
        }
        Object _xifexpression = null;
        if ((this.factoryClass == null)) {
          _xifexpression = this.<Object>findClass(this.implementationClass).newInstance();
        } else {
          _xifexpression = this.<ExtensionFactory>findClass(this.factoryClass).newInstance().createInstance(null, this.implementationClass);
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected RuntimeException asRuntimeException(final CharSequence message) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�message�, plugin id: �contributor�.");
    return new RuntimeException(_builder.toString());
  }
}
