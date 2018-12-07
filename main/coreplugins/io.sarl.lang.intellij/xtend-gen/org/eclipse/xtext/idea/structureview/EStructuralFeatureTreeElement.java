/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.structureview;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.intellij.psi.PsiElement;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.idea.structureview.AbstractStructureViewTreeElement;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class EStructuralFeatureTreeElement extends AbstractStructureViewTreeElement {
  private URI uri;
  
  @Accessors
  private EObject owner;
  
  @Accessors
  private EStructuralFeature feature;
  
  @Override
  public Object getValue() {
    return Pair.<URI, EStructuralFeature>of(this.uri, this.feature);
  }
  
  public void setOwner(final EObject owner) {
    this.owner = owner;
    this.uri = EcoreUtil.getURI(owner);
  }
  
  @Override
  protected PsiElement getInternalNavigationElement() {
    final Function1<INode, Iterable<ILeafNode>> _function = (INode it) -> {
      return it.getLeafNodes();
    };
    final Function1<ILeafNode, Boolean> _function_1 = (ILeafNode it) -> {
      boolean _isHidden = it.isHidden();
      return Boolean.valueOf((!_isHidden));
    };
    return this.xtextFile.getASTNode(IterableExtensions.<ILeafNode>head(IterableExtensions.<ILeafNode>filter(Iterables.<ILeafNode>concat(ListExtensions.<INode, Iterable<ILeafNode>>map(NodeModelUtils.findNodesForFeature(this.owner, this.feature), _function)), _function_1))).getPsi();
  }
  
  @Override
  protected Object getObjectToPresent() {
    return this.owner;
  }
  
  @Override
  public boolean equals(final Object obj) {
    boolean _xifexpression = false;
    if ((this == obj)) {
      _xifexpression = true;
    } else {
      boolean _xifexpression_1 = false;
      if ((obj instanceof EStructuralFeatureTreeElement)) {
        _xifexpression_1 = (Objects.equal(this.uri, ((EStructuralFeatureTreeElement)obj).uri) && Objects.equal(this.feature, ((EStructuralFeatureTreeElement)obj).feature));
      } else {
        _xifexpression_1 = false;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  @Override
  public int hashCode() {
    int _hashCode = this.uri.hashCode();
    int _hashCode_1 = this.feature.hashCode();
    return (_hashCode * _hashCode_1);
  }
  
  @Pure
  public EObject getOwner() {
    return this.owner;
  }
  
  @Pure
  public EStructuralFeature getFeature() {
    return this.feature;
  }
  
  public void setFeature(final EStructuralFeature feature) {
    this.feature = feature;
  }
}
