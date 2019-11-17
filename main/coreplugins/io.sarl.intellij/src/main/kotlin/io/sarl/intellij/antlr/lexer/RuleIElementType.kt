package io.sarl.intellij.antlr.lexer

import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/** Represents a specific ANTLR rule invocation in the language of the plug-in and is the
 * intellij "token type" of an interior PSI tree node. The IntelliJ equivalent
 * of ANTLR RuleNode.getRuleIndex() method or maybe RuleNode itself.
 *
 * Intellij Lexer token types are instances of IElementType.
 * We differentiate between parse tree subtree roots and tokens with
 * [RuleIElementType] and [TokenIElementType].
 */
class RuleIElementType(val ruleIndex: Int,
                       @NonNls debugName: String,
                       language: Language?) : IElementType(debugName, language)
