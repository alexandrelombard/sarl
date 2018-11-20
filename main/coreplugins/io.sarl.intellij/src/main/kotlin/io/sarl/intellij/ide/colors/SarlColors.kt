package io.sarl.intellij.ide.colors

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class SarlColor(val humanName: String, val default: TextAttributesKey) {
    DEFAULT("Default", Default.STRING);

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("io.sarl.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}