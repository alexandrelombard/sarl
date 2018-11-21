package io.sarl.intellij.ide.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.ide.colors.SarlColor
import io.sarl.intellij.lexer.SarlHighlightingLexer
import io.sarl.intellij.parser.SarlParserUtil
import io.sarl.intellij.psi.SarlElementTypes

class SarlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return SarlHighlightingLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
            pack(map(tokenType).textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): SarlColor = when (tokenType) {
            SarlElementTypes.NUMBER -> SarlColor.NUMBER
            SarlElementTypes.DOCUMENTATION_BLOCK -> SarlColor.DOCUMENTATION_COMMENT
            SarlElementTypes.LINE_COMMENT -> SarlColor.COMMENT
            SarlElementTypes.BLOCK_COMMENT -> SarlColor.COMMENT
            else -> SarlColor.DEFAULT
        }
    }
}
