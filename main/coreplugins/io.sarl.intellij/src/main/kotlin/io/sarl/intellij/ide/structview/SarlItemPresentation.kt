package io.sarl.intellij.ide.structview

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.util.PlatformIcons
import io.sarl.intellij.SarlIcons
import io.sarl.intellij.psi.EObjectPsiElement
import io.sarl.lang.sarl.SarlClass
import io.sarl.lang.sarl.SarlField
import io.sarl.lang.sarl.SarlScript
import org.eclipse.xtend.core.xtend.XtendFunction
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import javax.swing.Icon

class SarlItemPresentation constructor(protected val element: PsiElement) : ItemPresentation {

    override fun getIcon(unused: Boolean): Icon {
        if(element is EObjectPsiElement) {
            if(element.elementType.element is SarlScript) {
                return PlatformIcons.FILE_ICON
            }
            if(element.elementType.element is SarlClass)  {
                return PlatformIcons.CLASS_ICON
            }
            if(element.elementType.element is SarlField) {
                return PlatformIcons.FIELD_ICON
            }
            if(element.elementType.element is MethodDeclaration) {
                return PlatformIcons.METHOD_ICON
            }
            if(element.elementType.element is XtendFunction) {
                return PlatformIcons.FUNCTION_ICON
            }
        }

        return SarlIcons.SARL_PLUGIN
    }

    override fun getPresentableText(): String {
        if(element is EObjectPsiElement) {
            if(element.elementType.element is SarlScript) {
                return element.elementType.element.`package`
            }
            if(element.elementType.element is SarlClass) {
                return element.elementType.element.name
            }
            if(element.elementType.element is SarlField) {
                return element.elementType.element.name
            }
            if(element.elementType.element is MethodDeclaration) {
                return element.elementType.element.simpleName
            }
            if(element.elementType.element is XtendFunction) {
                return element.elementType.element.name
            }
        }


        val node = element.node
        return node.text
    }

    override fun getLocationString(): String? {
        return null
    }
}