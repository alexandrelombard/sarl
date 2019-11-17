package io.sarl.intellij.antlr.lexer

import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/** Represents a token in the language of the plug-in. The "token type" of
 * leaf nodes in jetbrains PSI tree. Corresponds to ANTLR's int token type.
 * Intellij lexer token types are instances of IElementType:
 *
 * "Interface for token types returned from lexical analysis and for types
 * of nodes in the AST tree."
 *
 * We differentiate between parse tree subtree roots and tokens with
 * [RuleIElementType] and [TokenIElementType], respectively.
 */
class TokenIElementType(val antlrTokenType: Int,
                        @NonNls debugName: String,
                        language: Language?) : IElementType(debugName, language)
