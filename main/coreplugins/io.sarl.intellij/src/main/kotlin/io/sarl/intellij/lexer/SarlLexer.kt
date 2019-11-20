package io.sarl.intellij.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CommonToken
import org.antlr.runtime.Token

/**
 * Adapter for SARL lexer
 * @author Alexandre Lombard
 */
class SarlLexer(private val internalSarlLexer: InternalSARLLexer) : LexerBase() {

    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentToken: Token? = null

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
        this.currentToken = null
        this.tokenStart = 0
        this.tokenEnd = 0

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

        return SarlPsiElementType.getTokenIElementType(currentToken.type)
    }

    override fun advance() {
        locateToken()
        this.currentToken = null
    }

    override fun getTokenStart(): Int {
        return this.tokenStart
    }

    override fun getTokenEnd(): Int {
        locateToken()
        return this.tokenEnd
    }

    private fun locateToken() {
        if (currentToken != null) return

        val token = this.internalSarlLexer.nextToken() as CommonToken
        this.currentToken = token

        if(token != Token.EOF_TOKEN) {
            this.tokenStart = this.startOffset + token.startIndex
            this.tokenEnd = this.startOffset + token.stopIndex + 1
        } else {
            this.tokenEnd = this.endOffset
        }
    }

}