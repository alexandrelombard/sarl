package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.PSITokenSource
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import org.antlr.runtime.CommonTokenStream
import org.antlr.runtime.debug.ParseTreeBuilder
import org.antlr.runtime.tree.ParseTree

class SarlParser(private val internalSarlParser: InternalSARLParser) : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        ProgressIndicatorProvider.checkCanceled()

        val source = PSITokenSource(builder)
        val tokens = CommonTokenStream(source)

        val rollbackMarker = builder.mark()

        // TODO Generate parse tree

        rollbackMarker.rollbackTo()

        val rootMarker = builder.mark()

        // TODO Walk parse tree

        if(root is IFileElementType) {

            internalSarlParser.parse().rootNode.
            for(it in internalSarlParser.parse().rootASTElement.eAllContents()) {
                it.
            }
        }

        while (!builder.eof()) {
            ProgressIndicatorProvider.checkCanceled()

            if(builder.tokenType)

            builder.advanceLexer()
        }

        rootMarker.done(root)
        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    protected fun parse(parser: InternalSARLParser, root: IElementType): ParseTree {
        // start rule depends on root passed in; sometimes we want to create an ID node etc...
        return if (root is IFileElementType) {
            return parser.parse().rootASTElement.
        } else (parser as SarlParser).sarlFile()
        // let's hope it's an ID as needed by "rename function"
        // FIXME
    }


}