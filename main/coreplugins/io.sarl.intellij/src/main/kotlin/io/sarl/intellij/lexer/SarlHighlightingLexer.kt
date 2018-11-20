package io.sarl.intellij.lexer

import com.intellij.lexer.LayeredLexer
import com.intellij.lexer.Lexer

class SarlHighlightingLexer() : LayeredLexer(SarlLexer())
