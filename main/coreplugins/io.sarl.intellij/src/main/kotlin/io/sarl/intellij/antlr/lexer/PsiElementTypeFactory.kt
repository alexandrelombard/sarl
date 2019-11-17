package io.sarl.intellij.antlr.lexer

import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.runtime.Token

import java.util.*

/** The factory that automatically maps all tokens and rule names into
 * IElementType objects: [TokenIElementType] and [RuleIElementType].
 *
 * This caches all mappings for each Language that use this factory. I.e.,
 * it's not keeping an instance per plugin/Language.
 */
object PsiElementTypeFactory {
    private val tokenIElementTypesCache = HashMap<Language, List<TokenIElementType>>()
    private val ruleIElementTypesCache = HashMap<Language, List<RuleIElementType>>()
    private val tokenNamesCache = HashMap<Language, Map<String, Int>>()
    private val ruleNamesCache = HashMap<Language, Map<String, Int>>()
    private val eofIElementTypesCache = HashMap<Language, TokenIElementType>()

    fun defineLanguageIElementTypes(language: Language,
                                    tokenNames: Array<String>) {
        synchronized(PsiElementTypeFactory::class.java) {
            if (tokenIElementTypesCache[language] == null) {
                var types: List<TokenIElementType>? = tokenIElementTypesCache[language]
                if (types == null) {
                    types = createTokenIElementTypes(language, tokenNames)
                    tokenIElementTypesCache[language] = types
                }
            }
            if (tokenNamesCache[language] == null) {
                tokenNamesCache[language] = createTokenTypeMap(tokenNames)
            }
        }
    }

    fun defineLanguageIElementTypes(language: Language,
                                    tokenNames: Array<String>,
                                    ruleNames: Array<String>) {
        synchronized(PsiElementTypeFactory::class.java) {
            if (tokenIElementTypesCache[language] == null) {
                var types: List<TokenIElementType>? = tokenIElementTypesCache[language]
                if (types == null) {
                    types = createTokenIElementTypes(language, tokenNames)
                    tokenIElementTypesCache[language] = types
                }
            }
            if (ruleIElementTypesCache[language] == null) {
                var result: List<RuleIElementType>? = ruleIElementTypesCache[language]
                if (result == null) {
                    result = createRuleIElementTypes(language, ruleNames)
                    ruleIElementTypesCache[language] = result
                }
            }
            if (tokenNamesCache[language] == null) {
                tokenNamesCache[language] = createTokenTypeMap(tokenNames)
            }
            if (ruleNamesCache[language] == null) {
                ruleNamesCache[language] = createRuleIndexMap(ruleNames)
            }
        }
    }

    fun getEofElementType(language: Language): TokenIElementType {
        var result: TokenIElementType? = eofIElementTypesCache[language]
        if (result == null) {
            result = TokenIElementType(Token.EOF, "EOF", language)
            eofIElementTypesCache[language] = result
        }

        return result
    }

    fun getTokenIElementTypes(language: Language): List<TokenIElementType> {
        return tokenIElementTypesCache[language]!!
    }

    fun getRuleIElementTypes(language: Language): List<RuleIElementType> {
        return ruleIElementTypesCache[language]!!
    }

    fun getRuleNameToIndexMap(language: Language): Map<String, Int> {
        return ruleNamesCache[language]!!
    }

    fun getTokenNameToTypeMap(language: Language): Map<String, Int> {
        return tokenNamesCache[language]!!
    }

    /** Get a map from token names to token types.  */
    fun createTokenTypeMap(tokenNames: Array<String>): Map<String, Int> {
        return toMap(tokenNames)
    }

    /** Get a map from rule names to rule indexes.  */
    fun createRuleIndexMap(ruleNames: Array<String>): Map<String, Int> {
        return toMap(ruleNames)
    }

    fun createTokenIElementTypes(language: Language, tokenNames: Array<String>): List<TokenIElementType> {
        val result = arrayListOf<TokenIElementType>()
        for (i in tokenNames.indices) {
            result.add(TokenIElementType(i, tokenNames[i], language))
        }

        return  Collections.unmodifiableList(result)
    }

    fun createRuleIElementTypes(language: Language, ruleNames: Array<String>): List<RuleIElementType> {
        val result = arrayListOf<RuleIElementType>()
        for (i in ruleNames.indices) {
            result.add(RuleIElementType(i, ruleNames[i], language))
        }

        return Collections.unmodifiableList(result)
    }

    fun createTokenSet(language: Language, vararg types: Int): TokenSet {
        val tokenIElementTypes = getTokenIElementTypes(language)

        val elementTypes = arrayOfNulls<IElementType>(types.size)
        for (i in types.indices) {
            if (types[i] == Token.EOF) {
                elementTypes[i] = getEofElementType(language)
            } else {
                elementTypes[i] = tokenIElementTypes[types[i]]
            }
        }

        return TokenSet.create(*elementTypes)
    }

    private fun toMap(keys: Array<String>): Map<String, Int> {
        val m = HashMap<String, Int>()
        for (i in keys.indices) {
            m[keys[i]] = i
        }
        return m
    }
}
