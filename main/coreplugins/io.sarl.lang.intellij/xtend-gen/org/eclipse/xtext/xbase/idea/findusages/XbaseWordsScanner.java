/**
 * Copyright (c) 2015, 2016 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.xbase.idea.findusages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.intellij.lang.cacheBuilder.SimpleWordsScanner;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.Processor;
import org.eclipse.xtext.idea.findusages.WordsScannerProvider;
import org.eclipse.xtext.idea.parser.TokenTypeProvider;
import org.eclipse.xtext.xbase.idea.parser.OperatorTokenTypeProvider;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class XbaseWordsScanner implements WordsScanner {
  @Singleton
  public static class XbaseWordsScannerProvider implements WordsScannerProvider {
    @Inject
    private Provider<XbaseWordsScanner> provider;
    
    @Override
    public WordsScanner get() {
      return this.provider.get();
    }
  }
  
  @Inject
  private Lexer lexer;
  
  @Inject
  private TokenTypeProvider tokenTypeProvider;
  
  @Inject
  private OperatorTokenTypeProvider operatorTokenTypeProvider;
  
  private final SimpleWordsScanner simpleWordsScanner = new SimpleWordsScanner();
  
  @Override
  public void processWords(final CharSequence fileText, final Processor<WordOccurrence> processor) {
    this.lexer.start(fileText);
    while ((this.lexer.getTokenType() != null)) {
      {
        this.scanOperator(processor);
        this.scanWords(processor);
        this.lexer.advance();
      }
    }
  }
  
  protected void scanOperator(final Processor<WordOccurrence> processor) {
    boolean _isOperator = this.isOperator();
    boolean _not = (!_isOperator);
    if (_not) {
      return;
    }
    final int start = this.lexer.getTokenStart();
    int end = this.lexer.getTokenEnd();
    this.lexer.advance();
    while (this.isOperator()) {
      {
        end = this.lexer.getTokenEnd();
        this.lexer.advance();
      }
    }
    CharSequence _bufferSequence = this.lexer.getBufferSequence();
    final WordOccurrence occurrence = new WordOccurrence(_bufferSequence, start, end, WordOccurrence.Kind.CODE);
    processor.process(occurrence);
  }
  
  protected boolean isOperator() {
    return this.operatorTokenTypeProvider.getOperatorTokens().contains(this.lexer.getTokenType());
  }
  
  protected void scanWords(final Processor<WordOccurrence> processor) {
    IElementType _tokenType = this.lexer.getTokenType();
    boolean _tripleEquals = (_tokenType == null);
    if (_tripleEquals) {
      return;
    }
    final WordOccurrence.Kind kind = this.getOccurrenceKind();
    final int tokenStart = this.lexer.getTokenStart();
    final Processor<WordOccurrence> _function = (WordOccurrence occurrence) -> {
      boolean _xblockexpression = false;
      {
        CharSequence _bufferSequence = this.lexer.getBufferSequence();
        int _start = occurrence.getStart();
        int _plus = (tokenStart + _start);
        int _end = occurrence.getEnd();
        int _plus_1 = (tokenStart + _end);
        occurrence.init(_bufferSequence, _plus, _plus_1, kind);
        _xblockexpression = processor.process(occurrence);
      }
      return _xblockexpression;
    };
    this.simpleWordsScanner.processWords(this.lexer.getTokenSequence(), _function);
  }
  
  protected WordOccurrence.Kind getOccurrenceKind() {
    boolean _isComments = this.isComments();
    if (_isComments) {
      return WordOccurrence.Kind.COMMENTS;
    }
    boolean _isLiterals = this.isLiterals();
    if (_isLiterals) {
      return WordOccurrence.Kind.LITERALS;
    }
    return WordOccurrence.Kind.CODE;
  }
  
  protected boolean isComments() {
    return this.tokenTypeProvider.getCommentTokens().contains(this.lexer.getTokenType());
  }
  
  protected boolean isLiterals() {
    return this.tokenTypeProvider.getStringLiteralTokens().contains(this.lexer.getTokenType());
  }
}
