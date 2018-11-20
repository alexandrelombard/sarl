package io.sarl.intellij

import com.intellij.openapi.fileTypes.LanguageFileType

import javax.swing.*

class SarlFileType protected constructor() : LanguageFileType(SarlLanguage.INSTANCE) {

    override fun getName(): String {
        return "Sarl"
    }

    override fun getDescription(): String {
        // TODO return SarlFileType.message("sarl.file.type.description");
        return "Sarl file"
    }

    override fun getDefaultExtension(): String {
        return "sarl"
    }

    override fun getIcon(): Icon? {
        return SarlIcons.SARL_PLUGIN
    }

    companion object {
        val INSTANCE = SarlFileType()
    }
}