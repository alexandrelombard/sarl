package io.sarl.intellij.structview

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.util.PlatformIcons
import javax.swing.Icon

class SarlItemPresentation constructor(protected val element: PsiElement) : ItemPresentation {

    override fun getIcon(unused: Boolean): Icon {
        return PlatformIcons.FUNCTION_ICON
    }

    override fun getPresentableText(): String {
        val node = element.node
        return node.text
    }

    override fun getLocationString(): String? {
        return null
    }
}