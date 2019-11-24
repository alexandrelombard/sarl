package io.sarl.intellij.ide.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.AllClassesGetter
import org.eclipse.xtext.util.ReplaceRegion
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.xbase.imports.RewritableImportSection
import com.intellij.usages.ChunkExtractor.getStartOffset
import org.eclipse.xtext.EcoreUtil2.getResourceSet
import org.eclipse.xtext.common.types.access.IJvmTypeProvider
import org.eclipse.xtext.resource.XtextResource
import com.intellij.psi.PsiFile
import com.intellij.codeInsight.completion.JavaPsiClassReferenceElement
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.InsertHandler
import io.sarl.intellij.psi.SarlPsiFile
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.CompletionProvider
import io.sarl.intellij.SarlPlugin
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer
import io.sarl.lang.ui.contentassist.SARLProposalProvider
import org.eclipse.xtext.RuleCall




// Example here: https://github.com/JetBrains/intellij-plugins/blob/master/osmorc/src/org/osmorc/manifest/completion/OsgiManifestCompletionContributor.java
class SarlCompletionContributor : CompletionContributor() {

    init {
        // TEST 1
        extend(CompletionType.BASIC, PATTERN_RULE_ID, object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
                this@SarlCompletionContributor.completeJavaTypes(parameters, result, false) { true }

                result.addElement(
                        LookupElementBuilder
                                .create("AUTOCOMPLETE_TEST"))
            }
        })

        // TEST 2
        extend(CompletionType.BASIC, PATTERN_OPEN_BRACKET, object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
                result.addElement(
                        LookupElementBuilder.create("}")
                )
            }

        })
    }

    // Extracted from this: https://github.com/alexandrelombard/sarl/blob/idea_plugin/main/coreplugins/io.sarl.lang.intellij/xtend-gen/org/eclipse/xtext/xbase/idea/completion/XbaseCompletionContributor.java
    protected fun completeJavaTypes(completionParameters: CompletionParameters, completionResultSet: CompletionResultSet, addImport: Boolean, filter: Function1<JavaPsiClassReferenceElement, Boolean>) {
        val _invocationCount = completionParameters.invocationCount
        val _lessEqualsThan = _invocationCount <= 2
        val _function = { it: LookupElement ->
            if (it is JavaPsiClassReferenceElement) {
                val _apply = filter.invoke(it)
                if (_apply) {
                    if (addImport) {
//                        it.setInsertHandler(this.importAddingInsertHandler)
                    }
                    completionResultSet.addElement(it)
                }
            }
        }
        JavaClassNameCompletionContributor.addAllClasses(completionParameters, _lessEqualsThan,
                JavaCompletionSorting.addJavaSorting(completionParameters, completionResultSet).getPrefixMatcher(), _function)
    }

    companion object {

        val PATTERN_RULE_ID = psiElement(SarlPsiElementType.getTokenIElementType(InternalSARLLexer.RULE_ID))
        val PATTERN_OPEN_BRACKET = psiElement().afterLeaf("{")


//        private fun header(name: String): ElementPattern<PsiElement> {
//            return psiElement()
//                    .afterLeaf(";")
//                    .withSuperParent(3, psiElement(Header::class.java).withName(name))
//        }
    }

}