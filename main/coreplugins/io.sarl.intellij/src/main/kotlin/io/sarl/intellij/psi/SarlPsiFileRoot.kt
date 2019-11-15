package io.sarl.intellij.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import io.sarl.intellij.SarlFileType
import io.sarl.intellij.SarlIcons
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.psi.ScopeNode
import javax.swing.Icon

class SarlPsiFileRoot(viewProvider: FileViewProvider) :
        PsiFileBase(viewProvider, SarlLanguage.INSTANCE), ScopeNode {
    override fun getFileType(): FileType {
        return SarlFileType
    }

    override fun resolve(element: PsiNamedElement?): PsiElement? {
        // FIXME: What should I return?
        return null
    }

    override fun getContext(): ScopeNode? {
        return null
    }

    override fun getIcon(flags: Int): Icon? {
        return SarlIcons.SARL_PLUGIN
    }
}