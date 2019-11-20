package io.sarl.intellij.antlr.parser

import com.intellij.lang.PsiBuilder
import io.sarl.intellij.antlr.psi.CompositeMarker
import org.antlr.runtime.Parser
import org.antlr.runtime.TokenStream

abstract class AbstractPsiAntlrParser(input: TokenStream) : Parser(input) {

    private val readableTokenNames: List<String> = arrayListOf()

    private var builder: PsiBuilder? = null
    private var compositeMarkers = arrayListOf<CompositeMarker>()



}