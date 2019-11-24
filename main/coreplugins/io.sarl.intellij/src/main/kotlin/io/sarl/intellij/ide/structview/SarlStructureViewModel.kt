package io.sarl.intellij.ide.structview

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter.ALPHA_SORTER
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.util.treeView.smartTree.Sorter
import io.sarl.intellij.psi.EObjectPsiElement
import io.sarl.intellij.psi.SarlPsiFile
import io.sarl.lang.sarl.SarlClass
import io.sarl.lang.sarl.SarlScript


class SarlStructureViewModel(root: SarlPsiFile) : StructureViewModelBase(root, SarlStructureViewRootElement(root)), StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> {
        return arrayOf(ALPHA_SORTER)
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return !isAlwaysShowsPlus(element)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        val value = element.value

        if(value is SarlPsiFile)
            return true

        if(value is EObjectPsiElement) {
            if(value.elementType.element is SarlScript || value.elementType.element is SarlClass) {
                return true
            }
        }

        return false
    }

}