package io.sarl.intellij.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.PSIElementTypeFactory
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.Token

/**
 * Adapter for SARL lexer
 * @author Alexandre Lombard
 */
class SarlLexer(private val internalSarlLexer: InternalSARLLexer) : LexerBase() {

    private val tokenElementTypes = PSIElementTypeFactory.getTokenIElementTypes(SarlLanguage.INSTANCE)

    private var buffer: CharSequence = ""
    private var endOffset: Int = 0
    private var currentToken: Token? = null

    override fun getState(): Int {
        return 0
    }

    override fun getTokenStart(): Int {
        return internalSarlLexer.charIndex
    }

    override fun getBufferEnd(): Int {
        return endOffset
    }

    override fun getBufferSequence(): CharSequence {
        return buffer
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.endOffset = endOffset
        this.buffer = buffer

        this.internalSarlLexer.charStream = ANTLRStringStream(buffer.toString())
    }

    override fun getTokenType(): IElementType? {
        val currentToken = this.currentToken ?: return null

        if(currentToken.type == Token.EOF) {
            return null
        }

        return tokenElementTypes[currentToken.type]
    }

    override fun advance() {
        this.currentToken = internalSarlLexer.nextToken()
    }

    override fun getTokenEnd(): Int {
        return internalSarlLexer.charIndex + internalSarlLexer.text.length
    }

}