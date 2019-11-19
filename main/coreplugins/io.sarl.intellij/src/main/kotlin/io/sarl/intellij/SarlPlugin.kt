package io.sarl.intellij

import com.google.inject.Injector
import io.sarl.lang.parser.antlr.SARLParser
import io.sarl.lang.services.SARLGrammarAccess

object SarlPlugin {
    lateinit var injector: Injector
    lateinit var sarlGrammarAccess: SARLGrammarAccess
    lateinit var sarlParser: SARLParser
}