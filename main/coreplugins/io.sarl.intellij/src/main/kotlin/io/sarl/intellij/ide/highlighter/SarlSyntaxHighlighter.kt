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

import io.sarl.lang.parser.antlr.internal.InternalSARLLexer.*

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
                    T__81, T__33, T__44, T__38, T__64, T__62, T__63, T__34, T__60,
                    T__31, T__101, T__143, T__39, T__61, T__92, T__90, T__69, T__83, T__135,
                    T__133, T__43, T__26, T__28, T__45, T__137, T__84, T__99, T__53, T__67,
                    T__132, T__36, T__96, T__122, T__42, T__86, T__48, T__139, T__54,
                    T__91, T__25, T__79, T__80, T__78, T__58, T__142, T__35, T__37,
                    T__82, T__85, T__136, T__68, T__88, T__141, T__51, T__89, T__138, T__98,
                    T__140, T__57, T__66, T__65, T__87, T__134, T__52 -> SarlColor.KEYWORD
                    RULE_DECIMAL -> SarlColor.NUMBER
                    RULE_ML_COMMENT, RULE_SL_COMMENT -> SarlColor.COMMENT
                    RULE_STRING -> SarlColor.STRING
                    else -> SarlColor.DEFAULT
                }
            }

            return SarlColor.DEFAULT
        }
    }
}
