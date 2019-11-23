package io.sarl.intellij.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import io.sarl.intellij.SarlFileType.DEFAULTS.EXTENSION
import io.sarl.intellij.SarlPlugin
import io.sarl.intellij.psi.SarlPsiFileRoot
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.resource.XtextSyntaxDiagnostic
import org.eclipse.xtext.util.StringInputStream
import org.eclipse.xtext.validation.IResourceValidator

class ErrorAnnotator : Annotator {

    private val resourceSet: ResourceSet
    private val validator: IResourceValidator

    init {
        this.resourceSet = SarlPlugin.injector.getInstance(ResourceSet::class.java)
        this.validator = SarlPlugin.injector.getInstance(IResourceValidator::class.java)
        SarlPlugin.injector.injectMembers(this.validator)
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if(element is SarlPsiFileRoot) {
            // FIXME We only validate the whole file, and we are not using the reference to the modified PsiElement
            val r = resourceSet.createResource(URI.createURI("dummy.$EXTENSION"))
            r.load(StringInputStream(element.text), null)
            if(r.errors.isNotEmpty()) {
                for(e in r.errors) {
                    if(e is XtextSyntaxDiagnostic) {
                        holder.createErrorAnnotation(TextRange(e.offset, e.offset + e.length), e.message)
                    } else {
                        // FIXME Ignoring right now
                    }
                }
            }
            r.delete(null)
        }
    }

}