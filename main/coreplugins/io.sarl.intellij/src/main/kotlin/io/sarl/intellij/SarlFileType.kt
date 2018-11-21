package io.sarl.intellij

import com.intellij.openapi.fileTypes.LanguageFileType

import javax.swing.*

object SarlFileType : LanguageFileType(SarlLanguage.INSTANCE) {

    override fun getName(): String {
        return "Sarl"
    }

    override fun getDescription(): String {
        return "Sarl Files"
    }

    override fun getDefaultExtension(): String {
        return DEFAULTS.EXTENSION
    }

    override fun getIcon(): Icon? {
        return SarlIcons.SARL_PLUGIN
    }

    object DEFAULTS {
        const val EXTENSION = "sarl"
    }
}