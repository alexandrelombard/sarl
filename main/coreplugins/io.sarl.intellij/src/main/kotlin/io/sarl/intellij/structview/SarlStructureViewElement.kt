package io.sarl.intellij.structview

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import io.sarl.intellij.psi.SarlPsiFileRoot

open class SarlStructureViewElement(protected val element: PsiElement) : StructureViewTreeElement, SortableTreeElement {

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
        if (element is SarlPsiFileRoot) {
            // FIXME
//            val topLevelElements = XPath.findAll(SarlLanguage.INSTANCE, element, "/sarlFile/topLevelObject/*/simpleIdentifier")
//            val treeElements = ArrayList<TreeElement>(topLevelElements.size)
//            for (el in topLevelElements) {
//                treeElements.add(SarlStructureViewElement(el))
//            }
//            return treeElements.toArray(arrayOfNulls<TreeElement>(topLevelElements.size))
        }
        return arrayOf()
    }
}