package io.sarl.intellij.grammar

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
import io.sarl.intellij.lexer.SarlLexer
import io.sarl.intellij.parser.SarlParser
import io.sarl.intellij.psi.SarlElementTypes
import org.intellij.grammar.psi.BnfTypes



class SarlParserDefinition : ParserDefinition {

    val LITERALS = TokenSet.create(SarlElementTypes.STRING)
    val COMMENTS = TokenSet.create(SarlElementTypes.BLOCK_COMMENT, SarlElementTypes.LINE_COMMENT)

    override fun createParser(project: Project?): PsiParser {
        return SarlParser()
    }

    override fun createFile(viewProvider: FileViewProvider?): PsiFile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringLiteralElements(): TokenSet {
        return LITERALS
    }

    override fun getFileNodeType(): IFileElementType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createLexer(project: Project?): Lexer {
        return SarlLexer()
    }

    override fun createElement(node: ASTNode?): PsiElement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }

}