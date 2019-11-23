package io.sarl.intellij.antlr.lexer

import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.openapi.progress.ProgressIndicatorProvider
import org.antlr.runtime.CharStream
import org.antlr.runtime.CommonToken
import org.antlr.runtime.Token
import org.antlr.runtime.TokenSource

/** Make a PsiBuilder look like a source of ANTLR tokens. PsiBuilder
 * provides tokens created by the lexer created in
 * [ParserDefinition.createLexer]. This is the bridge
 * between the ANTLR lexer and parser objects. Normally we just create
 * a [org.antlr.runtime.CommonTokenStream] but the IDE has
 * control and asks our ParserDefinition for the lexer and parser. This
 * is how we hook them together. When IDE ask ParserDefinition for a
 * parser, we will create one of these attached to the PsiBuilder.
 */
class PsiTokenSource(protected var builder: PsiBuilder) : TokenSource {

    override fun getSourceName(): String? {
        return null
    }

    /** Create an ANTLR Token from the current token type of the builder
     * then advance the builder to next token (which ultimately calls an
     * ANTLR lexer). The SARL Lexer creates tokens via
     * an ANTLR lexer but converts to [TokenIElementType] and here
     * we have to convert back to an ANTLR token using what info we
     * can get from the builder. We lose info such as the original channel.
     * So, whitespace and comments (typically hidden channel) will look like
     * real tokens. Jetbrains uses [ParserDefinition.getWhitespaceTokens]
     * and [ParserDefinition.getCommentTokens] to strip these before
     * our ANTLR parser sees them.
     */
    override fun nextToken(): Token {
        ProgressIndicatorProvider.checkCanceled()

        val ideaTType = builder.tokenType as TokenIElementType?
        val type = ideaTType?.antlrTokenType ?: Token.EOF

        val channel = Token.DEFAULT_CHANNEL
        val source = Pair<TokenSource, CharStream?>(this, null)
        val text = builder.tokenText
        val start = builder.currentOffset
        val length = text?.length ?: 0
        val stop = start + length - 1
        // PsiBuilder doesn't provide line, column info
        val line = 0
        val charPositionInLine = 0
        val t = createToken(source, type, text, channel, start, stop, line, charPositionInLine)
        builder.advanceLexer()
        return t
    }

    private fun createToken(
            source: Pair<TokenSource, CharStream?>,
            type: Int,
            text: String?,
            channel: Int,
            start: Int,
            stop: Int,
            line: Int,
            charPositionInLine: Int): CommonToken {
        val token = CommonToken(type,  text)
        token.startIndex = start
        token.stopIndex = stop
        token.line = line
        token.charPositionInLine = charPositionInLine
        token.channel = channel
        token.inputStream = source.second
        return token
    }

}
