package io.sarl.intellij.ide.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.ANTLRLexerAdaptor
import io.sarl.intellij.antlr.lexer.ANTLRLexerAdaptor
import io.sarl.intellij.ide.colors.SarlColor
import io.sarl.intellij.lexer.SarlHighlightingLexer
import io.sarl.intellij.parser.SarlParserUtil
import io.sarl.intellij.psi.SarlElementTypes
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.parser.antlr.internal.InternalSARLParser

class SarlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        val lexer = InternalSARLLexer()
        return ANTLRLexerAdaptor(SarlLanguage.INSTANCE, lexer)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
            pack(map(tokenType).textAttributesKey)

    companion object {
        init {

        }

        fun map(tokenType: IElementType): SarlColor = when (tokenType) {
            InternalSARLLexer.RULE_DECIMAL -> SarlColor.NUMBER
            SarlElementTypes.DOCUMENTATION_BLOCK -> SarlColor.DOCUMENTATION_COMMENT
            SarlElementTypes.LINE_COMMENT -> SarlColor.COMMENT
            SarlElementTypes.BLOCK_COMMENT -> SarlColor.COMMENT
            else -> SarlColor.DEFAULT
        }
    }
}
