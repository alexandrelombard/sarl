package io.sarl.intellij.ide.structview

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import io.sarl.intellij.psi.EObjectPsiElement
import io.sarl.intellij.psi.SarlPsiFileRoot
import io.sarl.lang.sarl.SarlClass
import io.sarl.lang.sarl.SarlField
import org.eclipse.xtend.core.xtend.XtendFunction
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration

open class SarlStructureViewElement(protected val element: PsiElement) : StructureViewTreeElement, SortableTreeElement {

    private val allowedItemsClasses = hashSetOf(
            SarlPsiFileRoot::class, SarlClass::class, SarlField::class, MethodDeclaration::class,
            XtendFunction::class)

    override fun getValue(): Any {
        return element
    }

    override fun navigate(requestFocus: Boolean) {
        if (element is NavigationItem) {
            (element as NavigationItem).navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean {
        return element is NavigationItem && (element as NavigationItem).canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return element is NavigationItem && (element as NavigationItem).canNavigateToSource()
    }

    override fun getAlphaSortKey(): String {
        return (if (element is PsiNamedElement) element.name else null) ?: return "unknown key"
    }

    override fun getPresentation(): ItemPresentation {
        return SarlItemPresentation(element)
    }

    override fun getChildren(): Array<TreeElement> {
        val result = arrayListOf<TreeElement>()

        for(c in element.children) {
            if(isStructureViewElement(c)) {
                result.add(SarlStructureViewElement(c))
            }
        }

        return result.toTypedArray()
    }

    private fun isStructureViewElement(element: PsiElement): Boolean {
        val parent = element.parent

        if(parent is SarlPsiFileRoot) {
            return true
        }

        if(element is EObjectPsiElement && parent is EObjectPsiElement) {
            // We remove the objects whose only semantic is not in the allowed set
            val allowed = allowedItemsClasses.map {
                it.java.isAssignableFrom(element.elementType.element::class.java)
            }.reduce { acc, value -> acc || value}

            if(!allowed) {
                return false
            }

            // And we remove the objects who don't carry more semantic than their parents
            return element.elementType.element != parent.elementType.element
        }

        return false
    }
}