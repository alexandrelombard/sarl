/*******************************************************************************
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.idea.extensions

import com.intellij.openapi.application.ApplicationManager
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.resource.IResourceServiceProvider

/**
 * @author kosyakov - Initial contribution and API
 */
class EcoreGlobalRegistries {

	new() {
		val packageRegistry = EPackage.Registry.INSTANCE
		for (ext : EPackageEP.EP_NAME.extensionList) {
			packageRegistry.put(ext.nsURI, ext.createDescriptor)
		}

		val extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.extensionToFactoryMap
		for (ext : ResourceFactoryEP.EP_NAME.extensionList) {
			extensionToFactoryMap.put(ext.type, ext.createDescriptor)
		}

		val registry = IResourceServiceProvider.Registry.INSTANCE
		for (ext : ResourceServiceProviderEP.EP_NAME.extensionList) {
			if (ext.uriExtension !== null)
				registry.extensionToFactoryMap.put(ext.uriExtension, ext.createDescriptor)
			if (ext.protocolName !== null)
				registry.protocolToFactoryMap.put(ext.protocolName, ext.createDescriptor)
			if (ext.contentTypeIdentifier !== null)
				registry.contentTypeToFactoryMap.put(ext.contentTypeIdentifier, ext.createDescriptor)
		} 
	}

	static def ensureInitialized() {
		ApplicationManager.application.getComponent(EcoreGlobalRegistries)
	}

}