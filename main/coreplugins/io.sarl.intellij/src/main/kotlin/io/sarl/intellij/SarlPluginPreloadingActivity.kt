package io.sarl.intellij

import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.progress.ProgressIndicator
import io.sarl.lang.SARLStandaloneSetup
import io.sarl.lang.services.SARLGrammarAccess

/**
 * Pre-loading activity to initialize the injector
 * @author Alexandre Lombard
 */
class SarlPluginPreloadingActivity : PreloadingActivity() {
    override fun preload(indicator: ProgressIndicator) {
        val injector = SARLStandaloneSetup().createInjectorAndDoEMFRegistration()
        injector.getInstance(SARLGrammarAccess::class.java)
    }
}