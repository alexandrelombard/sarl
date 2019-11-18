package io.sarl.intellij.antlr

import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.antlr.lexer.TokenIElementType
import io.sarl.lang.parser.antlr.SARLAntlrTokenFileProvider
import org.antlr.runtime.Token

/**
 * @author Alexandre Lombard
 */
object SarlPsiElementType {
    private val tokenIElementTypesCache: Map<Int, IElementType?>
    private val eofIElementTypesCache: IElementType = TokenIElementType(Token.EOF, "EOF", SarlLanguage.INSTANCE)

    init {
        // region Get all tokens and convert them to IElementType for IDEA
        val resultTokenCache = hashMapOf<Int, IElementType?>()

        SARLAntlrTokenFileProvider().antlrTokenFile.bufferedReader().use {
            for(line in it.lines()) {
                val beforeEqual = line.substringBeforeLast("=")
                val afterEqual = line.substringAfterLast("=")

                val type = afterEqual.toInt()
                val text = beforeEqual.removeSurrounding("'")

                val token = TokenIElementType(type, text, SarlLanguage.INSTANCE)

                if(resultTokenCache[type] == null) {
                    resultTokenCache[type] = token
                }
            }
        }

        tokenIElementTypesCache = resultTokenCache
        // endregion
    }

    fun getIElementType(tokenType: Int): IElementType? {
        return tokenIElementTypesCache[tokenType]
    }

    fun getEofElementType(): IElementType {
        return eofIElementTypesCache
    }

    fun createTokenSet(vararg types: Int): TokenSet {
        val elementTypes = arrayOfNulls<IElementType>(types.size)
        for (i in types.indices) {
            if (types[i] == Token.EOF) {
                elementTypes[i] = eofIElementTypesCache
            } else {
                elementTypes[i] = tokenIElementTypesCache[types[i]]
            }
        }

        return TokenSet.create(*elementTypes)
    }
}