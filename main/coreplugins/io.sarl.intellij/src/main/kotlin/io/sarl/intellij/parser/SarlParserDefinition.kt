package io.sarl.intellij.parser

import com.google.inject.Inject
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
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.lexer.SarlLexer
import io.sarl.intellij.psi.SarlPsiFileRoot
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import io.sarl.intellij.antlr.psi.AntlrPsiNode
import io.sarl.intellij.antlr.lexer.RuleIElementType
import io.sarl.intellij.antlr.lexer.TokenIElementType
import io.sarl.lang.SARLStandaloneSetup
import io.sarl.lang.services.SARLGrammarAccess
import org.eclipse.core.internal.runtime.Activator

class SarlParserDefinition : ParserDefinition {

    //@Inject lateinit var sarlGrammarAccess: SARLGrammarAccess

    override fun createParser(project: Project?): PsiParser {
        val internalParser = InternalSARLParser(null)
        return SarlParser(internalParser)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return SarlPsiFileRoot(viewProvider)
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

        if (elType is TokenIElementType) {
            return AntlrPsiNode(node)
        }
        if (elType !is RuleIElementType) {
            return AntlrPsiNode(node)
        }

        // FIXME: this is a kind of "default" behavior
        return AntlrPsiNode(node)
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