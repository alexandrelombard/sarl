package io.sarl.intellij.ide.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext

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

    companion object {

//        private fun header(name: String): ElementPattern<PsiElement> {
//            return psiElement()
//                    .afterLeaf(";")
//                    .withSuperParent(3, psiElement(Header::class.java).withName(name))
//        }
    }

}