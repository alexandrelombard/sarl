package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.SarlPlugin
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.intellij.antlr.lexer.PsiElementTypeFactory
import io.sarl.intellij.antlr.lexer.PsiTokenSource
import io.sarl.lang.parser.antlr.SARLParser
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import org.antlr.runtime.BaseRecognizer
import org.antlr.runtime.CommonTokenStream
import org.eclipse.xtext.nodemodel.BidiIterable
import org.eclipse.xtext.nodemodel.BidiTreeIterable
import org.eclipse.xtext.nodemodel.ICompositeNode
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.parser.DefaultEcoreElementFactory
import org.eclipse.xtext.parser.IAstFactory
import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider
import org.eclipse.xtext.parser.antlr.IUnorderedGroupHelper
import org.eclipse.xtext.parser.antlr.XtextTokenStream
import java.util.*

class SarlParser : PsiParser {

//    private val markers: Deque<PsiBuilder.Marker> = ArrayDeque()
//    private val ruleElementTypes = PsiElementTypeFactory.getRuleIElementTypes(SarlLanguage.INSTANCE)

    private var internalParser: InternalSARLParser? = null

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val source = PsiTokenSource(builder)
        val tokens = XtextTokenStream(source, 0)

        val parser = InternalSARLParser(tokens, SarlPlugin.sarlGrammarAccess())
        parser.semanticModelBuilder = SarlPlugin.injector.getInstance(IAstFactory::class.java)
        parser.syntaxErrorProvider = SarlPlugin.injector.getInstance(ISyntaxErrorMessageProvider::class.java)
        parser.unorderedGroupHelper = SarlPlugin.injector.getInstance(IUnorderedGroupHelper::class.java)
        parser.unorderedGroupHelper.initializeWith(SarlPlugin.injector.getInstance(InternalSARLLexer::class.java))
        parser.setTokenTypeMap(SarlPsiElementType.getTokenTypeMap())

//        val initMarker = builder.mark()
//        val parseResult = parser.parse()
//        initMarker.rollbackTo()
//
//        if(parseResult != null) {
//            val rootNode = parseResult.rootNode
//            val rootMarker = builder.mark()
//            for(child in rootNode.children) {
//                if(child is ICompositeNode) {
//                    walk(child, builder)
//                } else {
//                    val leafMarker = builder.mark()
//                    leafMarker.done(IElementType(child.text, SarlLanguage.INSTANCE))
//                }
//            }
//            rootMarker.done(IFileElementType(SarlLanguage.INSTANCE))
//        }

//        val initMarker = builder.mark()
//        val parseResult = parser.parse()
//        initMarker.rollbackTo()

        val rootMarker = builder.mark()

        val parseResult = parser.parse()
        if(parseResult != null) {
            // TODO Do the AST reconstruction
        }

        rootMarker.done(root)

        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...
    }

    private fun walk(node: ICompositeNode, builder: PsiBuilder) {
        val m = builder.mark()

        for(child in node.children) {
            if(child is ICompositeNode) {
                walk(child, builder)
            } else {
                val leafMarker = builder.mark()
//                builder.advanceLexer()
                leafMarker.done(IElementType(child.text, SarlLanguage.INSTANCE))
            }
        }

        m.done(IElementType(node.text, SarlLanguage.INSTANCE))
    }

}