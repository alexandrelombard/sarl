package io.sarl.intellij.ide.structview

import javax.swing.Icon
import com.intellij.psi.PsiFile
import com.intellij.navigation.ItemPresentation
import io.sarl.intellij.SarlIcons


class SarlRootPresentation constructor(protected val element: PsiFile) : ItemPresentation {

    override fun getIcon(unused: Boolean): Icon? {
        return SarlIcons.SARL_PLUGIN
    }

    override fun getPresentableText(): String {
        return element.virtualFile.nameWithoutExtension
    }

    override fun getLocationString(): String? {
        return null
    }
}