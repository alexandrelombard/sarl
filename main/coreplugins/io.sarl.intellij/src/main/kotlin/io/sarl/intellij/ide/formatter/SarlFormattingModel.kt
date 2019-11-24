package io.sarl.intellij.ide.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.FormattingDocumentModel
import com.intellij.formatting.FormattingModel
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings

class SarlFormattingModel(val element: PsiElement, val settings: CodeStyleSettings) : FormattingModel {

    init {

    }

    override fun commitChanges() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDocumentModel(): FormattingDocumentModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRootBlock(): Block {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shiftIndentInsideRange(node: ASTNode?, range: TextRange?, indent: Int): TextRange {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun replaceWhiteSpace(textRange: TextRange?, whiteSpace: String?): TextRange {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}