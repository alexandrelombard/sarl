package io.sarl.intellij.ide.structview

import com.intellij.psi.PsiFile
import com.intellij.navigation.ItemPresentation


class SarlStructureViewRootElement(element: PsiFile) : SarlStructureViewElement(element) {
    override fun getPresentation(): ItemPresentation {
        return SarlRootPresentation(element as PsiFile)
    }
}