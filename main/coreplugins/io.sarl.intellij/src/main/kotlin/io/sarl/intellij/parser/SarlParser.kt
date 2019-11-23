package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlPlugin
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.intellij.antlr.lexer.PsiTokenSource
import io.sarl.intellij.psi.PsiEObjectElementType
import io.sarl.intellij.psi.PsiSarlClassElementType
import io.sarl.intellij.psi.PsiSarlConstructorElementType
import io.sarl.intellij.psi.PsiSarlFieldElementType
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.parser.antlr.internal.InternalSARLParser
import io.sarl.lang.sarl.impl.SarlClassImplCustom
import io.sarl.lang.sarl.impl.SarlConstructorImpl
import io.sarl.lang.sarl.impl.SarlFieldImplCustom
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.nodemodel.ILeafNode
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.nodemodel.impl.LeafNodeWithSyntaxError
import org.eclipse.xtext.parser.IAstFactory
import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider
import org.eclipse.xtext.parser.antlr.IUnorderedGroupHelper
import org.eclipse.xtext.parser.antlr.XtextTokenStream
import java.util.*

class SarlParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val source = PsiTokenSource(builder)
        val tokens = XtextTokenStream(source, 0)

        val parser = InternalSARLParser(tokens, SarlPlugin.sarlGrammarAccess())

//        val parser = object : InternalSARLParser(tokens, SarlPlugin.sarlGrammarAccess()) {
//            private val stack = Stack<Pair<INode, PsiBuilder.Marker>>()
//
//            override fun enterRule() {
//                super.enterRule()
//
//                stack.push(Pair(currentNode, builder.mark()))
//            }
//
//            override fun leaveRule() {
//                val pair = stack.pop()
//
//                val psiType = getPsiElementType(pair.first.semanticElement)
//                pair.second.done(psiType)
//
//                super.leaveRule()
//            }
//
//            override fun recoverFromMismatchedToken(input: IntStream?, ttype: Int, follow: BitSet?): Any {
//                return super.recoverFromMismatchedToken(input, ttype, follow)
//            }
//        }

        parser.semanticModelBuilder = SarlPlugin.injector.getInstance(IAstFactory::class.java)
        parser.syntaxErrorProvider = SarlPlugin.injector.getInstance(ISyntaxErrorMessageProvider::class.java)
        parser.unorderedGroupHelper = SarlPlugin.injector.getInstance(IUnorderedGroupHelper::class.java)
        parser.unorderedGroupHelper.initializeWith(SarlPlugin.injector.getInstance(InternalSARLLexer::class.java))
        parser.setTokenTypeMap(SarlPsiElementType.getTokenTypeMap())

        parseTreeBasedPsiBuilding(root, builder, parser)

        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...
    }

    private fun parseTreeContextDone(stack: Stack<Pair<INode, PsiBuilder.Marker>>) {
        val pair = stack.pop()

        val psiElement = getPsiElementType(pair.first.semanticElement)

        pair.second.done(psiElement)
    }

    private fun parseTreeBasedPsiBuilding(root: IElementType, builder: PsiBuilder, parser: InternalSARLParser) {
        val stack = Stack<Pair<INode, PsiBuilder.Marker>>()
        var currentContainer: INode

        val tmp = builder.mark()
        val parseResult = parser.parse()
        tmp.rollbackTo()

        val rootMarker = builder.mark()
        if(parseResult != null) {
            val rootNode = parseResult.rootNode

            currentContainer = rootNode
            val rootNodeMarker = builder.mark()
            stack.push(Pair(rootNode, rootNodeMarker))

            for(n in rootNode.asTreeIterable) {
                if(n == rootNode)
                    continue    // This case is already managed by the enclosing context

                if(n is ILeafNode) {
                    val marker = builder.mark()
                    val psiElementType = getPsiElementType(n.semanticElement)
                    if(n is LeafNodeWithSyntaxError) {
                        builder.error(n.syntaxErrorMessage.message)
                    }
                    builder.advanceLexer()
                    marker.done(psiElementType)
                }

                if(n.parent != currentContainer) {
                    // We are changing the context
                    if(stack.map { it.first }.contains(n.parent)) {
                        // We already have been in this context
                        while(stack.peek().first != n.parent) {
                            parseTreeContextDone(stack)
                        }
                    } else {
                        // We are going deeper
                        val marker = builder.mark()
                        stack.push(Pair(n.parent, marker))
                    }

                    currentContainer = n.parent
                }

                // TODO Manage leaves and errors
            }
        }

        // Clear the remaining stack up to the root
        while(stack.isNotEmpty()) {
            parseTreeContextDone(stack)
        }

        println("D: ROOT")
        rootMarker.done(root)
    }

    private fun getPsiElementType(obj: EObject): PsiEObjectElementType {
        return when(obj::class) {
            EObject::class -> PsiEObjectElementType(obj)
            SarlClassImplCustom::class -> PsiSarlClassElementType(obj as SarlClassImplCustom)
            SarlFieldImplCustom::class -> PsiSarlFieldElementType(obj as SarlFieldImplCustom)
            SarlConstructorImpl::class -> PsiSarlConstructorElementType(obj as SarlConstructorImpl)
            else -> PsiEObjectElementType(obj)
        }
    }

}
