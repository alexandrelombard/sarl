package io.sarl.intellij.ide.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionParameters



// Example here: https://github.com/JetBrains/intellij-plugins/blob/master/osmorc/src/org/osmorc/manifest/completion/OsgiManifestCompletionContributor.java
class SarlCompletionContributor : CompletionContributor() {

    init {
        extend(CompletionType.SMART, psiElement(), object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
                result.addElement(
                        LookupElementBuilder
                                .create("AUTOCOMPLETE TEST")
                                .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE))
            }

        })
    }

    // Extracted from this: https://github.com/alexandrelombard/sarl/blob/idea_plugin/main/coreplugins/io.sarl.lang.intellij/xtend-gen/org/eclipse/xtext/xbase/idea/completion/XbaseCompletionContributor.java
//    protected fun completeJavaTypes(completionParameters: CompletionParameters, completionResultSet: CompletionResultSet, addImport: Boolean, filter: Function1<JavaPsiClassReferenceElement, Boolean>) {
//        val _invocationCount = completionParameters.invocationCount
//        val _lessEqualsThan = _invocationCount <= 2
//        val _function = { it: LookupElement ->
//            if (it is JavaPsiClassReferenceElement) {
//                val _apply = filter.apply(it as JavaPsiClassReferenceElement)
//                if (_apply.booleanValue()) {
//                    if (addImport) {
//                        (it as JavaPsiClassReferenceElement).setInsertHandler(this.importAddingInsertHandler)
//                    }
//                    completionResultSet.addElement(it)
//                }
//            }
//        }
//        JavaClassNameCompletionContributor.addAllClasses(completionParameters, _lessEqualsThan,
//                JavaCompletionSorting.addJavaSorting(completionParameters, completionResultSet).getPrefixMatcher(), _function)
//    }

    companion object {

//        private fun header(name: String): ElementPattern<PsiElement> {
//            return psiElement()
//                    .afterLeaf(";")
//                    .withSuperParent(3, psiElement(Header::class.java).withName(name))
//        }
    }

}