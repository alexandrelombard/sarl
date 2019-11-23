package io.sarl.intellij

import com.intellij.lang.Language

class SarlLanguage private constructor() : Language("Sarl", "text/x-sarl") {
    companion object {
        val INSTANCE = SarlLanguage()
    }
}
