package io.sarl.intellij.antlr.parser

import com.intellij.psi.tree.IElementType
import org.antlr.runtime.TokenStream

interface PsiTokenStream : TokenStream {
    fun getCurrentLookAhead(): Int
    fun reportRerror(reporter: ()->String)
    fun remapToken(tokenType: IElementType): IElementType
    fun appendAllTokens()
}