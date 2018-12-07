/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.tree.IElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MissingTokenException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.UnwantedTokenException;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.idea.lang.GrammarAwareErrorElementType;
import org.eclipse.xtext.idea.parser.CompositeMarker;
import org.eclipse.xtext.idea.parser.DFAUtil;
import org.eclipse.xtext.idea.parser.PsiTokenStream;
import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider;
import org.eclipse.xtext.parser.antlr.IUnorderedGroupHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public abstract class AbstractPsiAntlrParser extends Parser {
  private List<String> readableTokenNames;
  
  @Accessors
  @Extension
  private ISyntaxErrorMessageProvider _iSyntaxErrorMessageProvider;
  
  @Accessors
  private IUnorderedGroupHelper unorderedGroupHelper;
  
  @Accessors(AccessorType.PROTECTED_SETTER)
  private PsiBuilder psiBuilder;
  
  private final LinkedList<CompositeMarker> compositeMarkers = CollectionLiterals.<CompositeMarker>newLinkedList();
  
  private final PsiTokenStream psiInput;
  
  public AbstractPsiAntlrParser(final TokenStream input) {
    this(input, new RecognizerSharedState());
  }
  
  public AbstractPsiAntlrParser(final TokenStream input, final RecognizerSharedState state) {
    super(input, state);
    this.psiInput = ((PsiTokenStream) input);
  }
  
  protected abstract String getFirstRuleName();
  
  public void parse() throws RecognitionException {
    this.parse(this.getFirstRuleName());
  }
  
  public void parse(final String entryRuleName) throws RecognitionException {
    try {
      final String antlrEntryRuleName = this.normalizeEntryRuleName(entryRuleName);
      try {
        this.invokeEntryRule(antlrEntryRuleName);
        this.psiInput.appendAllTokens();
      } catch (final Throwable _t) {
        if (_t instanceof InvocationTargetException) {
          final InvocationTargetException ite = (InvocationTargetException)_t;
          Throwable _targetException = ite.getTargetException();
          final Throwable targetException = _targetException;
          boolean _matched = false;
          if (targetException instanceof RecognitionException) {
            _matched=true;
            this.psiInput.appendAllTokens();
            throw ((RecognitionException)targetException);
          }
          if (!_matched) {
            if (targetException instanceof ProcessCanceledException) {
              _matched=true;
              throw ((ProcessCanceledException)targetException);
            }
          }
          throw ite;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      } finally {
        this.doneComposite();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected Object invokeEntryRule(final String antlrEntryRuleName) {
    try {
      Object _xblockexpression = null;
      {
        final Method method = this.getClass().getMethod(antlrEntryRuleName);
        method.setAccessible(true);
        _xblockexpression = method.invoke(this);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected String normalizeEntryRuleName(final String entryRuleName) {
    String _xblockexpression = null;
    {
      boolean _startsWith = entryRuleName.startsWith("entryRule");
      if (_startsWith) {
        return entryRuleName;
      }
      boolean _startsWith_1 = entryRuleName.startsWith("rule");
      if (_startsWith_1) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("entry�entryRuleName.toFirstUpper�");
        return _builder.toString();
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("entryRule�entryRuleName�");
      _xblockexpression = _builder_1.toString();
    }
    return _xblockexpression;
  }
  
  @Override
  public String getSourceName() {
    return this.input.getSourceName();
  }
  
  protected void markComposite(final IElementType elementType) {
    PsiBuilder.Marker _mark = this.psiBuilder.mark();
    int _currentLookAhead = this.psiInput.getCurrentLookAhead();
    CompositeMarker _compositeMarker = new CompositeMarker(_mark, _currentLookAhead, elementType);
    this.compositeMarkers.push(_compositeMarker);
  }
  
  protected void markLeaf(final IElementType elementType) {
    this.psiInput.remapToken(elementType);
  }
  
  protected void associateWithSemanticElement() {
    CompositeMarker _peek = this.compositeMarkers.peek();
    _peek.setHasSemanticElement(true);
  }
  
  protected void precedeComposite(final IElementType elementType) {
    final CompositeMarker compositeMarker = this.compositeMarkers.pop();
    this.compositeMarkers.push(compositeMarker.precede(elementType));
    this.compositeMarkers.push(compositeMarker);
  }
  
  protected void doneComposite() {
    this.compositeMarkers.pop().done();
  }
  
  protected void doneLeaf(final Token matchedToken) {
    final IElementType tokenType = this.psiInput.remapToken(null);
    if ((matchedToken == null)) {
      PsiBuilder.Marker _mark = this.psiBuilder.mark();
      GrammarAwareErrorElementType _grammarAwareErrorElementType = new GrammarAwareErrorElementType(tokenType);
      _mark.done(_grammarAwareErrorElementType);
      return;
    }
  }
  
  @Override
  protected Object recoverFromMismatchedToken(final IntStream input, final int ttype, final BitSet follow) {
    try {
      boolean _mismatchIsUnwantedToken = this.mismatchIsUnwantedToken(input, ttype);
      if (_mismatchIsUnwantedToken) {
        final IElementType tokenType = this.psiInput.remapToken(null);
        UnwantedTokenException _unwantedTokenException = new UnwantedTokenException(ttype, input);
        this.reportError(_unwantedTokenException);
        this.beginResync();
        input.consume();
        this.endResync();
        this.psiInput.remapToken(tokenType);
        Object matchedSymbol = this.getCurrentInputSymbol(input);
        input.consume();
        return matchedSymbol;
      }
      boolean _mismatchIsMissingToken = this.mismatchIsMissingToken(input, follow);
      if (_mismatchIsMissingToken) {
        Object inserted = this.getMissingSymbol(input, null, ttype, follow);
        MissingTokenException _missingTokenException = new MissingTokenException(ttype, input, inserted);
        this.reportError(_missingTokenException);
        return null;
      }
      throw new MismatchedTokenException(ttype, input);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void recover(final IntStream input, final RecognitionException re) {
    final Function0<String> _function = () -> {
      return this.getErrorMessage(re, ((String[])Conversions.unwrapArray(this.readableTokenNames, String.class)));
    };
    this.psiInput.reportError(_function);
    this.psiInput.remapToken(null);
    super.recover(input, re);
  }
  
  @Override
  public void reportError(final RecognitionException e) {
    if (this.state.errorRecovery) {
      return;
    }
    this.state.syntaxErrors++;
    this.state.errorRecovery = true;
    final Function0<String> _function = () -> {
      return this.getErrorMessage(e, ((String[])Conversions.unwrapArray(this.readableTokenNames, String.class)));
    };
    this.psiInput.reportError(_function);
  }
  
  @Override
  public void emitErrorMessage(final String msg) {
    throw new UnsupportedOperationException();
  }
  
  public void setTokenTypeMap(final Map<Integer, String> tokenTypeMap) {
    String[] tokenNames = this.getTokenNames();
    this.readableTokenNames = ((List<String>)Conversions.doWrapArray(new String[tokenNames.length]));
    for (int i = 0; (i < tokenNames.length); i++) {
      boolean _containsKey = tokenTypeMap.containsKey(Integer.valueOf(i));
      if (_containsKey) {
        this.readableTokenNames.set(i, tokenTypeMap.get(Integer.valueOf(i)));
      } else {
        this.readableTokenNames.set(i, tokenNames[i]);
      }
    }
  }
  
  protected static short[][] unpackEncodedStringArray(final String[] arr) {
    return DFAUtil.unpackEncodedStringArray(arr);
  }
  
  @Pure
  public ISyntaxErrorMessageProvider get_iSyntaxErrorMessageProvider() {
    return this._iSyntaxErrorMessageProvider;
  }
  
  public void set_iSyntaxErrorMessageProvider(final ISyntaxErrorMessageProvider _iSyntaxErrorMessageProvider) {
    this._iSyntaxErrorMessageProvider = _iSyntaxErrorMessageProvider;
  }
  
  @Pure
  public IUnorderedGroupHelper getUnorderedGroupHelper() {
    return this.unorderedGroupHelper;
  }
  
  public void setUnorderedGroupHelper(final IUnorderedGroupHelper unorderedGroupHelper) {
    this.unorderedGroupHelper = unorderedGroupHelper;
  }
  
  protected void setPsiBuilder(final PsiBuilder psiBuilder) {
    this.psiBuilder = psiBuilder;
  }
}
