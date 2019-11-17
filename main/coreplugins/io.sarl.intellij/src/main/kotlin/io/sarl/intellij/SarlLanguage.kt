package io.sarl.intellij

import com.intellij.lang.Language
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory

class SarlLanguage private constructor() : Language("Sarl", "text/x-sarl") {
    companion object {
        val INSTANCE = SarlLanguage()
    }
}
