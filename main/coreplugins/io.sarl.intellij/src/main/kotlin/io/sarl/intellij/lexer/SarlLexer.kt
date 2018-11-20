package io.sarl.intellij.lexer

import com.intellij.lexer.FlexAdapter
import io.sarl.intellij.parser._SarlLexer

class SarlLexer : FlexAdapter(_SarlLexer())