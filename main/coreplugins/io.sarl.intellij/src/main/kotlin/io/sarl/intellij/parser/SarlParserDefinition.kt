package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.intellij.lexer.SarlLexer
import io.sarl.intellij.psi.EObjectPsiElement
import io.sarl.intellij.psi.PsiEObjectElementType
import io.sarl.intellij.psi.SarlPsiElement
import io.sarl.intellij.psi.SarlPsiFile
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer

class SarlParserDefinition : ParserDefinition {

    override fun createParser(project: Project?): PsiParser {
        return SarlParser()
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return SarlPsiFile(viewProvider)
    }

    override fun getWhitespaceTokens(): TokenSet {
        return WS
    }

    override fun getStringLiteralElements(): TokenSet {
        return STRING
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createLexer(project: Project?): Lexer {
        val internalLexer = InternalSARLLexer(null)
        return SarlLexer(internalLexer)
    }

    override fun createElement(node: ASTNode): PsiElement {
        val elType = node.elementType

        if (elType is PsiEObjectElementType) {
            return EObjectPsiElement(node, elType)
        }

        return SarlPsiElement(node)
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }

    companion object {
        val FILE = IFileElementType(SarlLanguage.INSTANCE)
        val COMMENTS = SarlPsiElementType.createTokenSet(
                InternalSARLLexer.RULE_ML_COMMENT,
                InternalSARLLexer.RULE_SL_COMMENT)
        val WS = SarlPsiElementType.createTokenSet(
                InternalSARLLexer.RULE_WS)
        val STRING = SarlPsiElementType.createTokenSet(
                InternalSARLLexer.RULE_STRING)
    }
}