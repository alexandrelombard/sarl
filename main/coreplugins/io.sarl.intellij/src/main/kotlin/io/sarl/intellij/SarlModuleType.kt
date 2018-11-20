package io.sarl.intellij

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager

import javax.swing.*

class SarlModuleType
//    public static SarlModuleType getInstance() {
//        return (SarlModuleType) ModuleTypeManager.getInstance().findByID(ID);
//    }

protected constructor() : ModuleType<SarlModuleBuilder>(ID) {

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

        private val ID = "SARL_MODULE_TYPE"

        val INSTANCE = SarlModuleType()
    }
}
