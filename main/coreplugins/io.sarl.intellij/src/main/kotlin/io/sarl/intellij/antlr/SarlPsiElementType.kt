package io.sarl.intellij.antlr

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.RuleIElementType
import io.sarl.intellij.antlr.lexer.TokenIElementType
import io.sarl.lang.parser.antlr.SARLAntlrTokenFileProvider
import org.antlr.runtime.Token

/**
 * @author Alexandre Lombard
 */
object SarlPsiElementType {
    private val tokenTypeMap: MutableMap<Int, String> = hashMapOf()
    private val tokenNameMap: MutableMap<String, Int> = hashMapOf()

    private val ruleIElementTypesCache: Map<Int, IElementType?>
    private val tokenIElementTypesCache: Map<Int, IElementType?>
    private val eofIElementTypesCache: IElementType = TokenIElementType(Token.EOF, "EOF", SarlLanguage.INSTANCE)

    init {
        val resultTokenTypeMap = hashMapOf<Int, String>()
        val resultTokenCache = hashMapOf<Int, IElementType?>()
        val resultRuleCache = hashMapOf<Int, IElementType?>()

        // region Get all tokens and convert them to IElementType for IDEA
        SARLAntlrTokenFileProvider().antlrTokenFile.bufferedReader().use {
            for(line in it.lines()) {
                val beforeEqual = line.substringBeforeLast("=")
                val afterEqual = line.substringAfterLast("=")

                val type = afterEqual.toInt()
                val text = beforeEqual.removeSurrounding("'")

                this.tokenTypeMap[type] = text

                if(!this.tokenNameMap.containsKey(text))
                    this.tokenNameMap[text] = type

                val token = TokenIElementType(type, text, SarlLanguage.INSTANCE)

                if(resultTokenCache[type] == null) {
                    resultTokenCache[type] = token
                }

                val rule = RuleIElementType(type, text, SarlLanguage.INSTANCE)

                if(resultRuleCache[type] == null) {
                    resultRuleCache[type] = rule
                }
            }
        }
        // endregion

        this.tokenIElementTypesCache = resultTokenCache
        this.ruleIElementTypesCache = resultRuleCache
    }

    fun getTokenIElementType(tokenType: Int): IElementType? {
        return tokenIElementTypesCache[tokenType]
    }

    fun getTokenIElementTypeByName(tokenName: String): IElementType? {
        val tokenType = getTokenNameMap()[tokenName] ?: return null
        return getTokenIElementType(tokenType)
    }

    fun getTokenTypeMap(): Map<Int, String> {
        return this.tokenTypeMap
    }

    fun getTokenNameMap(): Map<String, Int> {
        return this.tokenNameMap
    }

    fun getRuleIElementType(ruleType: Int): IElementType? {
        return ruleIElementTypesCache[ruleType]
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