/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.psi.impl;

import com.google.inject.Inject;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import javax.swing.Icon;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.idea.lang.IXtextLanguage;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.psi.PsiEObject;
import org.eclipse.xtext.psi.XtextPsiReference;
import org.eclipse.xtext.psi.impl.BaseXtextFile;
import org.eclipse.xtext.psi.tree.IGrammarAwareElementType;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.service.OperationCanceledError;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class PsiEObjectImpl<PsiT extends PsiElement, T extends StubElement<PsiT>> extends StubBasedPsiElementBase<T> implements PsiEObject {
  @Inject
  @Extension
  protected ILocationInFileProvider locationInFileProvider;
  
  private final IElementType elementType;
  
  protected PsiEObjectImpl(final T stub, final IStubElementType<T, PsiT> nodeType) {
    super(stub, nodeType);
    this.elementType = nodeType;
    this.getXtextLanguage().injectMembers(this);
  }
  
  public PsiEObjectImpl(final ASTNode node) {
    super(node);
    this.elementType = node.getElementType();
    this.getXtextLanguage().injectMembers(this);
  }
  
  @Override
  public EClass getEClass() {
    INode node = this.getINode();
    boolean _hasDirectSemanticElement = node.hasDirectSemanticElement();
    if (_hasDirectSemanticElement) {
      return node.getSemanticElement().eClass();
    }
    EReference eReference = this.getEReference();
    if ((eReference != null)) {
      return eReference.getEReferenceType();
    }
    return null;
  }
  
  @Override
  public EReference getEReference() {
    if ((this.elementType instanceof IGrammarAwareElementType)) {
      EObject grammarElement = ((IGrammarAwareElementType)this.elementType).getGrammarElement();
      if ((grammarElement instanceof CrossReference)) {
        return GrammarUtil.getReference(((CrossReference)grammarElement));
      }
    }
    INode node = this.getINode();
    if ((node == null)) {
      return null;
    }
    EObject grammarElement_1 = node.getGrammarElement();
    if ((grammarElement_1 instanceof CrossReference)) {
      return GrammarUtil.getReference(((CrossReference)grammarElement_1));
    }
    boolean _hasDirectSemanticElement = node.hasDirectSemanticElement();
    if (_hasDirectSemanticElement) {
      EObject semanticElement = node.getSemanticElement();
      return semanticElement.eContainmentFeature();
    }
    return null;
  }
  
  @Override
  public INode getINode() {
    return this.getXtextFile().getINode(this.getNode());
  }
  
  @Override
  public boolean isRoot() {
    PsiElement _parent = this.getParent();
    return (_parent instanceof BaseXtextFile);
  }
  
  @Override
  public EObject getEObject() {
    return this.getINode().getSemanticElement();
  }
  
  @Override
  public Resource getResource() {
    return this.getXtextFile().getResource();
  }
  
  @Override
  public IXtextLanguage getXtextLanguage() {
    Language _language = this.getLanguage();
    return ((IXtextLanguage) _language);
  }
  
  @Override
  public BaseXtextFile getXtextFile() {
    PsiFile _containingFile = this.getContainingFile();
    return ((BaseXtextFile) _containingFile);
  }
  
  @Override
  protected Icon getElementIcon(final int flags) {
    return AllIcons.General.GearPlain;
  }
  
  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.xtext.psi.impl.PsiEObjectImpl(�elementType�)�IF class !== PsiEObjectImpl�(\'anonymous\')�ENDIF�");
    return _builder.toString();
  }
  
  @Override
  public int getTextOffset() {
    final PsiReference reference = this.getReference();
    if ((reference instanceof XtextPsiReference)) {
      final int textOffset = super.getTextOffset();
      final TextRange rangeToHighlightInElement = ((XtextPsiReference)reference).getRangeToHighlightInElement();
      if ((rangeToHighlightInElement != null)) {
        int _startOffset = rangeToHighlightInElement.getStartOffset();
        return (textOffset + _startOffset);
      }
      return textOffset;
    }
    final ITextRegion textRegion = this.getSignificantTextRegion();
    if ((textRegion != null)) {
      return textRegion.getOffset();
    }
    return super.getTextOffset();
  }
  
  protected ITextRegion getSignificantTextRegion() {
    ITextRegion _xtrycatchfinallyexpression = null;
    try {
      _xtrycatchfinallyexpression = this.locationInFileProvider.getSignificantTextRegion(this.getEObject());
    } catch (final Throwable _t) {
      if (_t instanceof OperationCanceledError) {
        final OperationCanceledError e = (OperationCanceledError)_t;
        throw e.getWrapped();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
}
