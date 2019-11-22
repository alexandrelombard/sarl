package io.sarl.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
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
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.parser.IAstFactory
import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider
import org.eclipse.xtext.parser.antlr.IUnorderedGroupHelper
import org.eclipse.xtext.parser.antlr.XtextTokenStream
import java.lang.StringBuilder
import java.util.*

class SarlParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val source = PsiTokenSource(builder)
        val tokens = XtextTokenStream(source, 0)

        val parser = InternalSARLParser(tokens, SarlPlugin.sarlGrammarAccess())
        parser.semanticModelBuilder = SarlPlugin.injector.getInstance(IAstFactory::class.java)
        parser.syntaxErrorProvider = SarlPlugin.injector.getInstance(ISyntaxErrorMessageProvider::class.java)
        parser.unorderedGroupHelper = SarlPlugin.injector.getInstance(IUnorderedGroupHelper::class.java)
        parser.unorderedGroupHelper.initializeWith(SarlPlugin.injector.getInstance(InternalSARLLexer::class.java))
        parser.setTokenTypeMap(SarlPsiElementType.getTokenTypeMap())

//        astBasedPsiBuilding(root, builder, parser)
        parseTreeBasedPsiBuilding(root, builder, parser)

        return builder.treeBuilt // calls the ASTFactory.createComposite() etc...
    }

    private fun parseTreeContextDone(stack: Stack<Pair<INode, PsiBuilder.Marker>>) {
        val pair = stack.pop()

        println("${debugTab(stack.size)}D: ${pair.first}")

        pair.second.done(IElementType(pair.first.toString(), SarlLanguage.INSTANCE))
    }

    private fun parseTreeBasedPsiBuilding(root: IElementType, builder: PsiBuilder, parser: InternalSARLParser) {
        val stack = Stack<Pair<INode, PsiBuilder.Marker>>()
        var currentContainer: INode

        builder.setDebugMode(true)

        println("M: ROOT")
        val rootMarker = builder.mark()
        val parseResult = parser.parse()
        if(parseResult != null) {
            val rootNode = parseResult.rootNode

            currentContainer = rootNode
            val rootNodeMarker = builder.mark()
            println("${debugTab(stack.size)}M: $rootNode")
            stack.push(Pair(rootNode, rootNodeMarker))

            for(n in rootNode.asTreeIterable) {
                if(n == rootNode)
                    continue    // This case is already managed by the enclosing context

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
                        println("${debugTab(stack.size)}M: ${n.parent}")
                        stack.push(Pair(n.parent, marker))
                    }

                    currentContainer = n.parent
                }

                println("${debugTab(stack.size)}E: $n :: ${n.semanticElement}")

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

    private fun astContextDone(stack: Stack<Pair<EObject, PsiBuilder.Marker>>) {
        val pair = stack.pop()

        val psiElement = when(pair.first::class) {
            EObject::class -> PsiEObjectElementType(pair.first)
            SarlClassImplCustom::class -> PsiSarlClassElementType(pair.first as SarlClassImplCustom)
            SarlFieldImplCustom::class -> PsiSarlFieldElementType(pair.first as SarlFieldImplCustom)
            SarlConstructorImpl::class -> PsiSarlConstructorElementType(pair.first as SarlConstructorImpl)
            else -> PsiEObjectElementType(pair.first)
        }
        println("D: ${pair.first}")
        pair.second.done(psiElement)
    }

    private fun astBasedPsiBuilding(root: IElementType, builder: PsiBuilder, parser: InternalSARLParser) {
        val stack = Stack<Pair<EObject, PsiBuilder.Marker>>()
        var currentContainer: EObject

        builder.setDebugMode(true)

        println("M: ROOT")
        val rootMarker = builder.mark()
        val parseResult = parser.parse()
        if(parseResult != null) {
            val rootAst = parseResult.rootASTElement

            currentContainer = rootAst
            val rootAstMarker = builder.mark()
            println("M: $rootAst")
            stack.push(Pair(rootAst, rootAstMarker))

            for(n in rootAst.eAllContents()) {
                if(n.eContainer() != currentContainer) {
                    // We are changing the context
                    if(stack.map { it.first }.contains(n.eContainer())) {
                        // We already have been in this context
                        while(stack.peek().first != n.eContainer()) {
                            astContextDone(stack)
                        }
                    } else {
                        // We are going deeper
                        val marker = builder.mark()
                        println("M: ${n.eContainer()}")
                        stack.push(Pair(n.eContainer(), marker))
                    }

                    currentContainer = n.eContainer()
                }

                println("E: $n")

                // TODO Manage leaves and errors
            }
        }

        // Clear the remaining stack up to the root
        while(stack.isNotEmpty()) {
            astContextDone(stack)
        }

        println("D: ROOT")
        rootMarker.done(root)
    }

    private fun debugTab(size: Int): String {
        val str = StringBuilder()
        for(i in 0..size) {
            str.append("\t")
        }
        return str.toString()
    }

}