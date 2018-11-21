package io.sarl.intellij

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import javax.swing.Icon

class SarlModuleType : ModuleType<SarlModuleBuilder>(ID) {
    override fun createModuleBuilder(): SarlModuleBuilder {
        return SarlModuleBuilder.INSTANCE
    }

    override fun getName(): String {
        return "Sarl"
    }

    override fun getDescription(): String {
        return "Sarl module"
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return SarlIcons.SARL_PLUGIN
    }

    companion object {
        private const val ID = "SARL_MODULE"
        val INSTANCE: SarlModuleType by lazy { ModuleTypeManager.getInstance().findByID(ID) as SarlModuleType }
    }
}
