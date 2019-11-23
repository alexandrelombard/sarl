package io.sarl.intellij.ide.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import io.sarl.intellij.antlr.SarlPsiElementType
import io.sarl.lang.parser.antlr.internal.InternalSARLLexer

// Example here: https://github.com/JetBrains/intellij-plugins/blob/master/osmorc/src/org/osmorc/manifest/completion/OsgiManifestCompletionContributor.java
class SarlCompletionContributor : CompletionContributor() {

    init {
//        extend(CompletionType.BASIC, )
    }

    companion object {

//        private fun header(name: String): ElementPattern<PsiElement> {
//            return psiElement()
//                    .afterLeaf(";")
//                    .withSuperParent(3, psiElement(Header::class.java).withName(name))
//        }
    }

}