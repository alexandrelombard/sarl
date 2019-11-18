package io.sarl.intellij.ide.colors

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class SarlColor(val humanName: String, val default: TextAttributesKey) {
    KEYWORD("Keyword", Default.KEYWORD),
    DOCUMENTATION_COMMENT("DocumentationComment", Default.DOC_COMMENT),
    COMMENT("Comment", Default.BLOCK_COMMENT),
    NUMBER("Number", Default.NUMBER),
    STRING("String", Default.STRING),
    DEFAULT("Default", Default.CLASS_NAME);

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("io.sarl.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}