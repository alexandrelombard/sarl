package io.sarl.intellij.ide.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.antlr.lexer.TokenIElementType
import io.sarl.intellij.ide.colors.SarlColor
import io.sarl.intellij.lexer.SarlLexer
import io.sarl.intellij.parser.SarlParser
import io.sarl.lang.parser.antlr.SARLParser
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.parser.antlr.internal.InternalSARLParser

class SarlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return SarlLexer(InternalSARLLexer())
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
            pack(map(tokenType).textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): SarlColor {
            if(tokenType is TokenIElementType) {
                return when (tokenType.antlrTokenType) {
                    InternalSARLLexer.RULE_DECIMAL -> SarlColor.NUMBER
                    InternalSARLLexer.RULE_ML_COMMENT -> SarlColor.COMMENT
                    InternalSARLLexer.RULE_SL_COMMENT -> SarlColor.COMMENT
                    InternalSARLLexer.RULE_STRING -> SarlColor.STRING
                    else -> SarlColor.DEFAULT
                }
            }

            return SarlColor.DEFAULT
        }
    }
}
