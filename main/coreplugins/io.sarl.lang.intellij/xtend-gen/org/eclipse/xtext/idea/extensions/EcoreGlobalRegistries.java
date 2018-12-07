/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.extensions;

import com.intellij.openapi.application.ApplicationManager;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.idea.extensions.EPackageEP;
import org.eclipse.xtext.idea.extensions.ResourceFactoryEP;
import org.eclipse.xtext.idea.extensions.ResourceServiceProviderEP;
import org.eclipse.xtext.resource.IResourceServiceProvider;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class EcoreGlobalRegistries {
  public EcoreGlobalRegistries() {
    final EPackage.Registry packageRegistry = EPackage.Registry.INSTANCE;
    List<EPackageEP> _extensionList = EPackageEP.EP_NAME.getExtensionList();
    for (final EPackageEP ext : _extensionList) {
      packageRegistry.put(ext.getNsURI(), ext.createDescriptor());
    }
    final Map<String, Object> extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    List<ResourceFactoryEP> _extensionList_1 = ResourceFactoryEP.EP_NAME.getExtensionList();
    for (final ResourceFactoryEP ext_1 : _extensionList_1) {
      extensionToFactoryMap.put(ext_1.getType(), ext_1.createDescriptor());
    }
    final IResourceServiceProvider.Registry registry = IResourceServiceProvider.Registry.INSTANCE;
    List<ResourceServiceProviderEP> _extensionList_2 = ResourceServiceProviderEP.EP_NAME.getExtensionList();
    for (final ResourceServiceProviderEP ext_2 : _extensionList_2) {
      {
        String _uriExtension = ext_2.getUriExtension();
        boolean _tripleNotEquals = (_uriExtension != null);
        if (_tripleNotEquals) {
          registry.getExtensionToFactoryMap().put(ext_2.getUriExtension(), ext_2.createDescriptor());
        }
        String _protocolName = ext_2.getProtocolName();
        boolean _tripleNotEquals_1 = (_protocolName != null);
        if (_tripleNotEquals_1) {
          registry.getProtocolToFactoryMap().put(ext_2.getProtocolName(), ext_2.createDescriptor());
        }
        String _contentTypeIdentifier = ext_2.getContentTypeIdentifier();
        boolean _tripleNotEquals_2 = (_contentTypeIdentifier != null);
        if (_tripleNotEquals_2) {
          registry.getContentTypeToFactoryMap().put(ext_2.getContentTypeIdentifier(), ext_2.createDescriptor());
        }
      }
    }
  }
  
  public static EcoreGlobalRegistries ensureInitialized() {
    return ApplicationManager.getApplication().<EcoreGlobalRegistries>getComponent(EcoreGlobalRegistries.class);
  }
}
