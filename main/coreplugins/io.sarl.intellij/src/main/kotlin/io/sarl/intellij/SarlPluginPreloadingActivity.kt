package io.sarl.intellij

import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.progress.ProgressIndicator
import io.sarl.lang.SARLStandaloneSetup
import io.sarl.lang.parser.antlr.SARLParser
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import io.sarl.lang.services.SARLGrammarAccess
import java.util.logging.Logger

/**
 * Pre-loading activity to initialize the injector
 * @author Alexandre Lombard
 */
class SarlPluginPreloadingActivity : PreloadingActivity() {
    private val LOG = Logger.getLogger(SarlPluginPreloadingActivity::class.simpleName)

    override fun preload(indicator: ProgressIndicator) {
        try {
            SarlPlugin.injector
        } catch (t: Throwable) {
            LOG.severe("Error while initializing the SARL plugin")
            t.printStackTrace()
        }
    }
}