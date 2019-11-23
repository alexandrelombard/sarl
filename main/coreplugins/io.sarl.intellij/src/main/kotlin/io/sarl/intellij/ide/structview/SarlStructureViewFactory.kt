package io.sarl.intellij.ide.structview

import com.intellij.lang.PsiStructureViewFactory
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.psi.PsiFile
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.editor.Editor
import io.sarl.intellij.psi.SarlPsiFileRoot


class SarlStructureViewFactory : PsiStructureViewFactory {

    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return SarlStructureViewModel(psiFile as SarlPsiFileRoot)
            }
        }
    }
}