/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.common.types.shared;

import com.google.inject.Binder;
import com.google.inject.name.Names;
import org.eclipse.xtext.Constants;
import org.eclipse.xtext.common.types.xtext.JvmIdentifiableQualifiedNameProvider;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.psi.IPsiModelAssociations;
import org.eclipse.xtext.psi.PsiModelAssociations;
import org.eclipse.xtext.service.LanguageSpecific;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class IdeaSharedCommonTypesModule implements com.google.inject.Module {
  @Override
  public void configure(final Binder binder) {
    binder.bindConstant().annotatedWith(Names.named(Constants.FILE_EXTENSIONS)).to("java");
    binder.<IQualifiedNameProvider>bind(IQualifiedNameProvider.class).to(JvmIdentifiableQualifiedNameProvider.class);
    binder.<IPsiModelAssociations>bind(IPsiModelAssociations.class).annotatedWith(LanguageSpecific.class).to(PsiModelAssociations.class);
  }
}
