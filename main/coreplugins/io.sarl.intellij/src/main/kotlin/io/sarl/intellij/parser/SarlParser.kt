package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.SarlPlugin
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.antlr.lexer.PsiTokenSource
import io.sarl.lang.parser.antlr.SARLParser
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import org.antlr.runtime.BaseRecognizer
import org.antlr.runtime.CommonTokenStream
import org.eclipse.xtext.parser.DefaultEcoreElementFactory
import org.eclipse.xtext.parser.IAstFactory
import org.eclipse.xtext.parser.antlr.IUnorderedGroupHelper
import org.eclipse.xtext.parser.antlr.XtextTokenStream
import java.util.*

class SarlParser : PsiParser {

//    private val markers: Deque<PsiBuilder.Marker> = ArrayDeque()
//    private val ruleElementTypes = PsiElementTypeFactory.getRuleIElementTypes(SarlLanguage.INSTANCE)

    private var internalParser: InternalSARLParser? = null

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
//        ProgressIndicatorProvider.checkCanceled()
//
//        val source = PsiTokenSource(builder)
//        val tokens = CommonTokenStream(source)
//
//        internalSarlParser.input = tokens
//
//        // Generate parse tree
//        val rollbackMarker = builder.mark()
//        val parseResult = internalSarlParser.parse()
//        val tree = parseResult.rootNode
//        rollbackMarker.rollbackTo()
//
//        val rootMarker = builder.mark()
//        walk(tree, builder)
//
//        while (!builder.eof()) {
//            ProgressIndicatorProvider.checkCanceled()
//            builder.advanceLexer()
//        }
//
//        rootMarker.done(root)



        val source = PsiTokenSource(builder)
        val tokens = XtextTokenStream(source, 0)

        val parser = InternalSARLParser(tokens, SarlPlugin.sarlGrammarAccess())
        parser.semanticModelBuilder = SarlPlugin.injector.getInstance(IAstFactory::class.java)
        parser.unorderedGroupHelper = SarlPlugin.injector.getInstance(IUnorderedGroupHelper::class.java)
        parser.unorderedGroupHelper.initializeWith(SarlPlugin.injector.getInstance(InternalSARLLexer::class.java))

        val parseResult = parser.parse()

        if(parseResult != null) {
            val rootNode = parseResult.rootNode
            for(abstractNode in rootNode.asTreeIterable) {
                println(abstractNode.text)
            }
        }

        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...
    }

//    fun walk(node: ICompositeNode, builder: PsiBuilder) {
//        markers.push(builder.mark())
//
//        node.semanticElement
//
//        if(node.hasChildren()) {
//            for(child in node.children) {
//                if(child is ICompositeNode) {
//                    walk(child, builder)
//                }
//            }
//        }
//
//        val marker = markers.pop()
//        //marker.done()
//    }
//
//    fun parseLight(t: IElementType, b: PsiBuilder) {
//
//    }
}