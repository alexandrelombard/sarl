package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.antlr.lexer.PsiTokenSource
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import org.antlr.runtime.CommonTokenStream
import org.eclipse.xtext.nodemodel.ICompositeNode
import java.util.*

class SarlParser(private val internalSarlParser: InternalSARLParser) : PsiParser {

    private val markers: Deque<PsiBuilder.Marker> = ArrayDeque()
    private val ruleElementTypes = PsiElementTypeFactory.getRuleIElementTypes(SarlLanguage.INSTANCE)

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        ProgressIndicatorProvider.checkCanceled()

        val source = PsiTokenSource(builder)
        val tokens = CommonTokenStream(source)

        internalSarlParser.input = tokens

        // Generate parse tree
        val rollbackMarker = builder.mark()
        val parseResult = internalSarlParser.parse()
        val tree = parseResult.rootNode
        rollbackMarker.rollbackTo()

        val rootMarker = builder.mark()
        walk(tree, builder)

        while (!builder.eof()) {
            ProgressIndicatorProvider.checkCanceled()
            builder.advanceLexer()
        }

        rootMarker.done(root)
        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...
    }

    fun walk(node: ICompositeNode, builder: PsiBuilder) {
        markers.push(builder.mark())

        node.semanticElement

        if(node.hasChildren()) {
            for(child in node.children) {
                if(child is ICompositeNode) {
                    walk(child, builder)
                }
            }
        }

        val marker = markers.pop()
        //marker.done()
    }
}