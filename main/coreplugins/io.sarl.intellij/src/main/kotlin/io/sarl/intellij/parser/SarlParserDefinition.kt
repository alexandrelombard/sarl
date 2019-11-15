package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.lexer.SarlLexer
import io.sarl.lang.parser.antlr.internal.InternalSARLParser


class SarlParserDefinition : ParserDefinition {
    override fun createParser(project: Project?): PsiParser {
        val internalParser = InternalSARLParser(null)
        return SarlParser(internalParser)
    }

    override fun createFile(viewProvider: FileViewProvider?): PsiFile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringLiteralElements(): TokenSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createLexer(project: Project?): Lexer {
        val internalLexer = InternalSARLLexer(null)
        return SarlLexer(internalLexer)
    }

    override fun createElement(node: ASTNode?): PsiElement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommentTokens(): TokenSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val FILE = IFileElementType(SarlLanguage.INSTANCE)
        val COMMENTS = TokenSet.create()
    }
}