package io.sarl.intellij.structview

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter.ALPHA_SORTER
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.util.treeView.smartTree.Sorter
import io.sarl.intellij.psi.SarlPsiFileRoot


class SarlStructureViewModel(root: SarlPsiFileRoot) : StructureViewModelBase(root, SarlStructureViewRootElement(root)), StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> {
        return arrayOf(ALPHA_SORTER)
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return !isAlwaysShowsPlus(element)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        val value = element.value
        return value is SarlPsiFileRoot
    }
}