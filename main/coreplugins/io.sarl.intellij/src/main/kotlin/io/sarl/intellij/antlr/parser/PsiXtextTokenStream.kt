package io.sarl.intellij.antlr.parser

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import org.antlr.runtime.BaseRecognizer
import org.antlr.runtime.TokenSource
import org.eclipse.xtext.parser.antlr.ITokenDefProvider
import org.eclipse.xtext.parser.antlr.TokenDefProvider
import org.eclipse.xtext.parser.antlr.XtextTokenStream

class PsiXtextTokenStream(
        private val builder: PsiBuilder,
        tokenSource: TokenSource,
        tokenDefProvider: ITokenDefProvider) : XtextTokenStream(tokenSource, tokenDefProvider), PsiTokenStream {

    private var afterSeek = false
    private val states = arrayListOf<PsiTokenStreamState>()
    private val psiToOriginalMarkers = hashMapOf<Int, Int>()
    private var errorMessage: String? = null
    private var tokenType: IElementType? = null

    override fun reportRerror(reporter: () -> String) {
        if (errorMessage == null) {
            errorMessage = reporter.invoke()
        }
    }

    override fun remapToken(tokenType: IElementType): IElementType {
        val currentTokenType = this.tokenType
        this.tokenType = tokenType
        return currentTokenType!!
    }

    override fun appendAllTokens() {
        while(!builder.eof()) {
            consume()
        }
        // FIXME: Not thread safe
        val error = errorMessage
        if (error != null) {
            builder.error(error)
            errorMessage = null
        }
    }

    override fun consume() {
        if (afterSeek) {
            super.consume()
        } else {
            val rawTokenIndex = builder.rawTokenIndex()
            if (rawTokenIndex < p) {
                val n = p - rawTokenIndex
                for(i in 0 until n) {
                    advanceLexer(null)
                }
            }
            super.consume()
            advanceLexer(this.tokenType)
        }
    }

    protected fun advanceLexer(tokenType: IElementType?) {
        if (builder.eof()) {
            return
        }
        val token = get(builder.rawTokenIndex())
        val hidden = token.channel == BaseRecognizer.HIDDEN

        val currentTokenType = if (tokenType === null) builder.tokenType else tokenType
//        builder.remapCurrentToken(
//                new CreateElementType(currentTokenType) [
//                        putUserData(IASTNodeAwareNodeModelBuilder.HIDDEN_KEY, hidden)
//                ]
//        )
//
//        val errorMessage = token.errorMessage
//        if (errorMessage !== null) {
//            val errorMarker = builder.mark()
//            builder.advanceLexer()
//            errorMarker.error(errorMessage)
//        } else {
//            builder.advanceLexer()
//        }
    }
}

data class PsiTokenStreamState(
        val errorMessage: String,
        val tokenType: IElementType,
        val marker: PsiBuilder.Marker)