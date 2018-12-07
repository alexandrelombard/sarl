/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.structureview;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.NodeProvider;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.idea.structureview.AbstractStructureViewTreeElement;
import org.eclipse.xtext.idea.structureview.AlphaSorter;
import org.eclipse.xtext.idea.structureview.DefaultComparator;
import org.eclipse.xtext.idea.structureview.IStructureViewTreeElementProvider;
import org.eclipse.xtext.idea.structureview.XtextFileTreeElement;
import org.eclipse.xtext.psi.PsiEObject;
import org.eclipse.xtext.psi.impl.BaseXtextFile;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class XtextFileTreeModel extends TextEditorBasedStructureViewModel implements StructureViewModel.ElementInfoProvider {
  @Accessors
  private IStructureViewTreeElementProvider structureViewTreeElementProvider;
  
  protected final List<Sorter> sorters;
  
  protected final List<Filter> filters;
  
  protected final List<Grouper> groupers;
  
  protected final List<Class<?>> suitableClasses;
  
  protected final List<NodeProvider> nodeProviders;
  
  public XtextFileTreeModel(final BaseXtextFile xtextFile, final Editor editor) {
    super(editor, xtextFile);
    xtextFile.getXtextLanguage().injectMembers(this);
    this.sorters = CollectionLiterals.<Sorter>newArrayList();
    final Comparator<TreeElement> comparator = this.getComparator();
    if ((comparator != null)) {
      AlphaSorter _alphaSorter = new AlphaSorter();
      final Procedure1<AlphaSorter> _function = (AlphaSorter it) -> {
        it.setComparator(comparator);
      };
      AlphaSorter _doubleArrow = ObjectExtensions.<AlphaSorter>operator_doubleArrow(_alphaSorter, _function);
      this.sorters.add(_doubleArrow);
    }
    this.filters = CollectionLiterals.<Filter>newArrayList();
    this.groupers = CollectionLiterals.<Grouper>newArrayList();
    this.nodeProviders = CollectionLiterals.<NodeProvider>newArrayList();
    this.suitableClasses = CollectionLiterals.<Class<?>>newArrayList(PsiEObject.class);
  }
  
  @Override
  protected BaseXtextFile getPsiFile() {
    PsiFile _psiFile = super.getPsiFile();
    return ((BaseXtextFile) _psiFile);
  }
  
  @Override
  public StructureViewTreeElement getRoot() {
    XtextFileTreeElement _xblockexpression = null;
    {
      BaseXtextFile _psiFile = this.getPsiFile();
      final XtextFileTreeElement rootTreeElement = new XtextFileTreeElement(_psiFile);
      rootTreeElement.setStructureViewTreeElementProvider(this.structureViewTreeElementProvider);
      _xblockexpression = rootTreeElement;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean isAlwaysLeaf(final StructureViewTreeElement element) {
    boolean _xifexpression = false;
    if ((element instanceof AbstractStructureViewTreeElement)) {
      _xifexpression = ((AbstractStructureViewTreeElement)element).isLeaf();
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  @Override
  public boolean isAlwaysShowsPlus(final StructureViewTreeElement element) {
    return false;
  }
  
  @Override
  public Filter[] getFilters() {
    return ((Filter[])Conversions.unwrapArray(this.filters, Filter.class));
  }
  
  @Override
  public Sorter[] getSorters() {
    return ((Sorter[])Conversions.unwrapArray(this.sorters, Sorter.class));
  }
  
  @Override
  public Grouper[] getGroupers() {
    return ((Grouper[])Conversions.unwrapArray(this.groupers, Grouper.class));
  }
  
  @Override
  public Collection<NodeProvider> getNodeProviders() {
    return this.nodeProviders;
  }
  
  public Comparator<TreeElement> getComparator() {
    return new DefaultComparator();
  }
  
  @Override
  public Object getCurrentEditorElement() {
    Object _xblockexpression = null;
    {
      final Object element = super.getCurrentEditorElement();
      Object _xifexpression = null;
      if ((element instanceof PsiEObject)) {
        EObject _eObject = ((PsiEObject)element).getEObject();
        URI _uRI = null;
        if (_eObject!=null) {
          _uRI=EcoreUtil.getURI(_eObject);
        }
        _xifexpression = _uRI;
      } else {
        _xifexpression = element;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected Object getSuperCurrentEditorElement() {
    return super.getCurrentEditorElement();
  }
  
  @Override
  protected Class[] getSuitableClasses() {
    return ((Class[])Conversions.unwrapArray(this.suitableClasses, Class.class));
  }
  
  @Pure
  public IStructureViewTreeElementProvider getStructureViewTreeElementProvider() {
    return this.structureViewTreeElementProvider;
  }
  
  public void setStructureViewTreeElementProvider(final IStructureViewTreeElementProvider structureViewTreeElementProvider) {
    this.structureViewTreeElementProvider = structureViewTreeElementProvider;
  }
}
