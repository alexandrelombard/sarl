package io.sarl.intellij

import com.google.inject.Injector
import io.sarl.lang.SARLStandaloneSetup
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.services.SARLGrammarAccess

object SarlPlugin {
    val injector: Injector by lazy { SARLStandaloneSetup().createInjectorAndDoEMFRegistration() }

    fun sarlGrammarAccess() = SarlPlugin.injector.getInstance(SARLGrammarAccess::class.java)
    fun internalSarlLexer() = SarlPlugin.injector.getInstance(InternalSARLLexer::class.java)
}