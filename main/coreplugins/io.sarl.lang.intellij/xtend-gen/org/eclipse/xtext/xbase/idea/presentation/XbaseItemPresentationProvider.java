/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.xbase.idea.presentation;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import java.util.Arrays;
import javax.swing.Icon;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.idea.presentation.DefaultItemPresentationProvider;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver;
import org.eclipse.xtext.xbase.typesystem.IResolvedTypes;
import org.eclipse.xtext.xbase.typesystem.override.IResolvedConstructor;
import org.eclipse.xtext.xbase.typesystem.override.IResolvedField;
import org.eclipse.xtext.xbase.typesystem.override.IResolvedOperation;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;
import org.eclipse.xtext.xbase.validation.UIStrings;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class XbaseItemPresentationProvider extends DefaultItemPresentationProvider {
  @Inject
  private UIStrings uiStrings;
  
  @Inject
  private CommonTypeComputationServices services;
  
  @Inject
  private IBatchTypeResolver typeResolver;
  
  @Override
  protected Icon _image(final PsiElement element) {
    return element.getIcon(Iconable.ICON_FLAG_VISIBILITY);
  }
  
  protected Icon _image(final IResolvedConstructor constructor) {
    return this.image(constructor.getDeclaration());
  }
  
  protected Icon _image(final IResolvedField field) {
    return this.image(field.getDeclaration());
  }
  
  protected Icon _image(final IResolvedOperation operation) {
    return this.image(operation.getDeclaration());
  }
  
  protected String _text(final JvmGenericType genericType) {
    return genericType.getSimpleName();
  }
  
  protected String _text(final JvmOperation element) {
    return this.signature(element.getSimpleName(), element);
  }
  
  protected String _text(final IResolvedOperation element) {
    String _xblockexpression = null;
    {
      final String returnTypeString = element.getResolvedReturnType().getSimpleName();
      String decoratedPart = (" : " + returnTypeString);
      boolean _isEmpty = element.getTypeParameters().isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        String _string = this.uiStrings.toString(element.getTypeParameters());
        String _plus = (" <" + _string);
        String _plus_1 = (_plus + "> : ");
        String _plus_2 = (_plus_1 + returnTypeString);
        decoratedPart = _plus_2;
      }
      String _simpleName = element.getDeclaration().getSimpleName();
      String _plus_3 = (_simpleName + "(");
      final Function1<LightweightTypeReference, String> _function = (LightweightTypeReference it) -> {
        return it.getHumanReadableName();
      };
      String _join = IterableExtensions.join(ListExtensions.<LightweightTypeReference, String>map(element.getResolvedParameterTypes(), _function), ", ");
      String _plus_4 = (_plus_3 + _join);
      String _plus_5 = (_plus_4 + ")");
      _xblockexpression = (_plus_5 + decoratedPart);
    }
    return _xblockexpression;
  }
  
  protected String _text(final JvmConstructor constructor) {
    String _parameters = this.uiStrings.parameters(constructor);
    return ("new" + _parameters);
  }
  
  protected String _text(final IResolvedConstructor constructor) {
    final Function1<LightweightTypeReference, String> _function = (LightweightTypeReference it) -> {
      return it.getHumanReadableName();
    };
    String _join = IterableExtensions.join(ListExtensions.<LightweightTypeReference, String>map(constructor.getResolvedParameterTypes(), _function), ", ");
    String _plus = ("new(" + _join);
    return (_plus + ")");
  }
  
  protected String _text(final IResolvedField field) {
    String _simpleSignature = field.getSimpleSignature();
    String _plus = (_simpleSignature + " : ");
    String _humanReadableName = field.getResolvedType().getHumanReadableName();
    return (_plus + _humanReadableName);
  }
  
  protected String _text(final JvmField field) {
    String _simpleName = field.getSimpleName();
    String _plus = (_simpleName + " : ");
    String _simpleName_1 = field.getType().getSimpleName();
    return (_plus + _simpleName_1);
  }
  
  protected String _text(final JvmFormalParameter parameter) {
    String _xblockexpression = null;
    {
      final JvmTypeReference parameterType = parameter.getParameterType();
      String _xifexpression = null;
      if ((parameterType == null)) {
        _xifexpression = parameter.getName();
      } else {
        String _simpleName = parameterType.getSimpleName();
        String _plus = (_simpleName + " ");
        String _name = parameter.getName();
        _xifexpression = (_plus + _name);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected String _text(final XImportDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�importedTypeName��IF wildcard�.*�ELSEIF memberName !== null�.�memberName��ENDIF�");
    return _builder.toString();
  }
  
  protected String _text(final XImportSection element) {
    return "import declarations";
  }
  
  protected String _text(final XVariableDeclaration variableDeclaration) {
    String _xblockexpression = null;
    {
      final IResolvedTypes resolvedTypes = this.typeResolver.resolveTypes(variableDeclaration);
      final LightweightTypeReference type = resolvedTypes.getActualType(((JvmIdentifiableElement) variableDeclaration));
      String _xifexpression = null;
      if ((type != null)) {
        String _humanReadableName = type.getHumanReadableName();
        String _plus = (_humanReadableName + " ");
        String _name = variableDeclaration.getName();
        _xifexpression = (_plus + _name);
      } else {
        _xifexpression = variableDeclaration.getName();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected String signature(final String simpleName, final JvmIdentifiableElement element) {
    String _xblockexpression = null;
    {
      JvmTypeReference _xifexpression = null;
      if ((element instanceof JvmOperation)) {
        _xifexpression = ((JvmOperation)element).getReturnType();
      } else {
        JvmTypeReference _xifexpression_1 = null;
        if ((element instanceof JvmField)) {
          _xifexpression_1 = ((JvmField)element).getType();
        } else {
          _xifexpression_1 = null;
        }
        _xifexpression = _xifexpression_1;
      }
      final JvmTypeReference returnType = _xifexpression;
      final StandardTypeReferenceOwner owner = new StandardTypeReferenceOwner(this.services, element);
      String _xifexpression_2 = null;
      if ((returnType == null)) {
        _xifexpression_2 = "void";
      } else {
        _xifexpression_2 = owner.toLightweightTypeReference(returnType).getHumanReadableName();
      }
      final String returnTypeString = _xifexpression_2;
      String decoratedPart = (" : " + returnTypeString);
      String _elvis = null;
      String _typeParameters = this.uiStrings.typeParameters(element);
      if (_typeParameters != null) {
        _elvis = _typeParameters;
      } else {
        _elvis = "";
      }
      final String typeParam = _elvis;
      boolean _notEquals = (!Objects.equal(typeParam, ""));
      if (_notEquals) {
        decoratedPart = (((" " + typeParam) + " : ") + returnTypeString);
      }
      String _parameters = this.uiStrings.parameters(element);
      String _plus = (simpleName + _parameters);
      _xblockexpression = (_plus + decoratedPart);
    }
    return _xblockexpression;
  }
  
  @Override
  public Icon image(final Object constructor) {
    if (constructor instanceof IResolvedConstructor) {
      return _image((IResolvedConstructor)constructor);
    } else if (constructor instanceof IResolvedOperation) {
      return _image((IResolvedOperation)constructor);
    } else if (constructor instanceof PsiElement) {
      return _image((PsiElement)constructor);
    } else if (constructor instanceof EObject) {
      return _image((EObject)constructor);
    } else if (constructor instanceof IResolvedField) {
      return _image((IResolvedField)constructor);
    } else if (constructor == null) {
      return _image((Void)null);
    } else if (constructor != null) {
      return _image(constructor);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(constructor).toString());
    }
  }
  
  public String text(final Object constructor) {
    if (constructor instanceof JvmConstructor) {
      return _text((JvmConstructor)constructor);
    } else if (constructor instanceof JvmOperation) {
      return _text((JvmOperation)constructor);
    } else if (constructor instanceof JvmField) {
      return _text((JvmField)constructor);
    } else if (constructor instanceof JvmGenericType) {
      return _text((JvmGenericType)constructor);
    } else if (constructor instanceof JvmFormalParameter) {
      return _text((JvmFormalParameter)constructor);
    } else if (constructor instanceof XVariableDeclaration) {
      return _text((XVariableDeclaration)constructor);
    } else if (constructor instanceof IResolvedConstructor) {
      return _text((IResolvedConstructor)constructor);
    } else if (constructor instanceof IResolvedOperation) {
      return _text((IResolvedOperation)constructor);
    } else if (constructor instanceof XImportDeclaration) {
      return _text((XImportDeclaration)constructor);
    } else if (constructor instanceof XImportSection) {
      return _text((XImportSection)constructor);
    } else if (constructor instanceof EObject) {
      return _text((EObject)constructor);
    } else if (constructor instanceof IResolvedField) {
      return _text((IResolvedField)constructor);
    } else if (constructor == null) {
      return _text((Void)null);
    } else if (constructor != null) {
      return _text(constructor);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(constructor).toString());
    }
  }
}
