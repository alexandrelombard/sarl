package io.sarl.intellij.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.antlr.lexer.TokenIElementType
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CommonToken
import org.antlr.runtime.Token

/**
 * Adapter for SARL lexer
 * @author Alexandre Lombard
 */
class SarlLexer(private val internalSarlLexer: InternalSARLLexer) : LexerBase() {

    //private val tokenElementTypes = PsiElementTypeFactory.getTokenIElementTypes(SarlLanguage.INSTANCE)

    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentToken: CommonToken? = null

    override fun getState(): Int {
        return 0
    }

    override fun getBufferEnd(): Int {
        return endOffset
    }

    override fun getBufferSequence(): CharSequence {
        return buffer
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.buffer = buffer

        this.internalSarlLexer.charStream = ANTLRStringStream(buffer.substring(startOffset, endOffset))
    }

    override fun getTokenType(): IElementType? {
        locateToken()

        val currentToken = this.currentToken ?: return null

        if(currentToken.type == Token.EOF) {
            return null
        }

        if(tokenElementTypes[currentToken] == null) {
            tokenElementTypes[currentToken] =
                    TokenIElementType(currentToken.type, currentToken.text, SarlLanguage.INSTANCE)
        }

        return tokenElementTypes[currentToken]
    }

    override fun advance() {
        locateToken()
        this.currentToken = null
    }

    override fun getTokenStart(): Int {
        return this.startOffset + this.currentToken!!.startIndex
    }

    override fun getTokenEnd(): Int {
        return this.startOffset + this.currentToken!!.stopIndex + 1
    }

    private fun locateToken() {
        if (currentToken != null) return;

        this.currentToken = this.internalSarlLexer.nextToken() as CommonToken
    }

    companion object {
        // FIXME Avoid using a map if possible
        val tokenElementTypes = hashMapOf<Token, TokenIElementType>()
    }

}