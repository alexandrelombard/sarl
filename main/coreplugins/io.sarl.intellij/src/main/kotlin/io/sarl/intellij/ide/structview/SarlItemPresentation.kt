package io.sarl.intellij.ide.structview

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.util.PlatformIcons
import io.sarl.intellij.SarlIcons
import io.sarl.intellij.psi.EObjectPsiElement
import io.sarl.lang.sarl.*
import org.eclipse.xtend.core.xtend.XtendFunction
import org.eclipse.xtend.core.xtend.XtendTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import javax.swing.Icon

class SarlItemPresentation constructor(protected val element: PsiElement) : ItemPresentation {

    override fun getIcon(unused: Boolean): Icon {
        if(element is EObjectPsiElement) {
            val iElement = element.elementType.element
            if(iElement is SarlScript) {
                return PlatformIcons.FILE_ICON
            }
            if(iElement is SarlClass)  {
                return PlatformIcons.CLASS_ICON
            }
            if(iElement is SarlInterface) {
                return PlatformIcons.INTERFACE_ICON
            }
            if(iElement is SarlField) {
                return PlatformIcons.FIELD_ICON
            }
            if(iElement is MethodDeclaration) {
                return PlatformIcons.METHOD_ICON
            }
            if(iElement is XtendFunction) {
                return PlatformIcons.FUNCTION_ICON
            }
        }

        return SarlIcons.SARL_PLUGIN
    }

    override fun getPresentableText(): String {
        if(element is EObjectPsiElement) {
            val iElement = element.elementType.element
            if(iElement is SarlScript) {
                if(iElement.`package` != null) {
                    return iElement.`package`
                }
            }
            if(iElement is XtendTypeDeclaration) {
                // This one includes SarlClass, SarlInterface, SarlAgent, etc.
                if(iElement.name != null) {
                    return iElement.name
                }
            }
            if(iElement is SarlField) {
                return "${iElement.name}: ${iElement.type.simpleName}"
            }
            if(iElement is MethodDeclaration) {
                return "${iElement.simpleName}(${iElement.parameters.joinToString(",") { it.type.simpleName }}): ${iElement.returnType?.simpleName ?: ""}"
            }
            if(iElement is XtendFunction) {
                return "${iElement.name}(${iElement.parameters.joinToString(",") { it.parameterType?.simpleName ?: "" }}): ${iElement.returnType?.simpleName}"
            }
        }


        val node = element.node
        return node.text
    }

    override fun getLocationString(): String? {
        return null
    }
}