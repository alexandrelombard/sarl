/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.psi.impl;

import com.google.inject.Inject;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.idea.nodemodel.ASTNodeExtension;
import org.eclipse.xtext.psi.PsiEObjectFactory;
import org.eclipse.xtext.psi.PsiEObjectIdentifier;
import org.eclipse.xtext.psi.PsiNamedEObject;
import org.eclipse.xtext.psi.impl.PsiEObjectIdentifierImpl;
import org.eclipse.xtext.psi.impl.PsiEObjectImpl;
import org.eclipse.xtext.psi.stubs.PsiNamedEObjectStub;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class PsiNamedEObjectImpl<PsiE extends PsiNamedEObject, T extends PsiNamedEObjectStub<PsiE>> extends PsiEObjectImpl<PsiE, T> implements PsiNamedEObject {
  @Inject
  @Extension
  private ASTNodeExtension _aSTNodeExtension;
  
  @Inject
  @Extension
  private PsiEObjectFactory psiEObjectFactory;
  
  public PsiNamedEObjectImpl(final T stub, final IStubElementType<T, PsiE> nodeType) {
    super(stub, nodeType);
  }
  
  public PsiNamedEObjectImpl(final ASTNode node) {
    super(node);
  }
  
  @Override
  public PsiEObjectIdentifier getNameIdentifier() {
    PsiEObjectIdentifierImpl _xblockexpression = null;
    {
      final ASTNode nameNode = this.findNameNode();
      if ((nameNode == null)) {
        return null;
      }
      PsiElement _psi = nameNode.getPsi();
      _xblockexpression = new PsiEObjectIdentifierImpl(_psi);
    }
    return _xblockexpression;
  }
  
  @Override
  public String getName() {
    final T stub = this.getStub();
    if ((stub != null)) {
      return stub.getName();
    }
    final ITextRegion significantTextRegion = this.getSignificantTextRegion();
    final int startIndex = significantTextRegion.getOffset();
    int _length = significantTextRegion.getLength();
    final int endIndex = (startIndex + _length);
    final String result = this.getContainingFile().getText().substring(startIndex, endIndex);
    return result;
  }
  
  @Override
  public PsiNamedEObject setName(final String name) throws IncorrectOperationException {
    PsiNamedEObjectImpl<PsiE, T> _xblockexpression = null;
    {
      final ASTNode nameNode = this.findNameNode();
      if ((nameNode == null)) {
        return this;
      }
      final EObject grammarElement = this._aSTNodeExtension.getGrammarElement(nameNode);
      if ((grammarElement == null)) {
        return this;
      }
      boolean _isTerminalRuleCall = GrammarUtil.isTerminalRuleCall(grammarElement);
      if (_isTerminalRuleCall) {
        final ASTNode newNameNode = this.psiEObjectFactory.createLeafIdentifier(name, nameNode);
        nameNode.getTreeParent().replaceChild(nameNode, newNameNode);
      } else {
        final ASTNode newNameNode_1 = this.psiEObjectFactory.createCompositeIdentifier(name, this.getName(), nameNode).getFirstChildNode();
        nameNode.replaceAllChildrenToChildrenOf(newNameNode_1);
      }
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  protected ASTNode findNameNode() {
    ASTNode _xblockexpression = null;
    {
      final EStructuralFeature nameFeature = this.getNameFeature();
      if ((nameFeature == null)) {
        return null;
      }
      _xblockexpression = IterableExtensions.<ASTNode>head(this._aSTNodeExtension.findNodesForFeature(this.getNode(), nameFeature));
    }
    return _xblockexpression;
  }
  
  protected EStructuralFeature getNameFeature() {
    EClass _eClass = this._aSTNodeExtension.getEClass(this.getNode());
    EStructuralFeature _eStructuralFeature = null;
    if (_eClass!=null) {
      _eStructuralFeature=_eClass.getEStructuralFeature("name");
    }
    final EStructuralFeature feature = _eStructuralFeature;
    if ((((feature instanceof EAttribute) && (!feature.isMany())) && String.class.isAssignableFrom(feature.getEType().getInstanceClass()))) {
      return feature;
    }
    return null;
  }
  
  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.xtext.psi.impl.PsiNamedEObjectImpl(�elementType�:�findNameNode?.elementType ?: \'null\'�)�IF class !== PsiNamedEObjectImpl�(\'anonymous\')�ENDIF�");
    return _builder.toString();
  }
}
