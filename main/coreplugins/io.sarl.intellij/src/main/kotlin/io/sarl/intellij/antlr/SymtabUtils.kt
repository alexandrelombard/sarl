package io.sarl.intellij.antlr

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import io.sarl.intellij.antlr.psi.ScopeNode

object SymtabUtils {
    fun getContextFor(element: PsiElement): ScopeNode? {
        val parent = element.parent
        if (parent is ScopeNode) {
            return parent
        }
        return if (parent is PsiErrorElement) {
            null
        } else parent.context as ScopeNode?
    }
}
