package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.antlr.lexer.PSITokenSource
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import org.antlr.runtime.CommonTokenStream
import java.util.*


class SarlParser(private val internalSarlParser: InternalSARLParser) : PsiParser {

    protected val markers: Deque<PsiBuilder.Marker> = ArrayDeque()

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        ProgressIndicatorProvider.checkCanceled()

        val source = PSITokenSource(builder)
        val tokens = CommonTokenStream(source)

        internalSarlParser.input = tokens

        // Generate parse tree
        val rollbackMarker = builder.mark()
        val parseResult = internalSarlParser.parse()
        rollbackMarker.rollbackTo()

        // TODO Walk parse tree
        val rootMarker = builder.mark()
        for (iter in parseResult.rootNode.asTreeIterable) {
            iter.
        }

        while (!builder.eof()) {
            ProgressIndicatorProvider.checkCanceled()
            builder.advanceLexer()
        }

        rootMarker.done(root)
        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...
    }
}