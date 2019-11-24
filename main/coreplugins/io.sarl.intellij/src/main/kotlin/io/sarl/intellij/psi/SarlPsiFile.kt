package io.sarl.intellij.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import io.sarl.intellij.SarlFileType
import io.sarl.intellij.SarlIcons
import io.sarl.intellij.SarlLanguage
import io.sarl.intellij.antlr.psi.ScopeNode
import javax.swing.Icon
import org.eclipse.emf.ecore.util.EcoreUtil.getEObject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.resource.XtextResource
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.service.OperationCanceledError
import org.eclipse.xtext.EcoreUtil2
import java.io.IOException
import java.io.ByteArrayInputStream
import com.intellij.xml.index.IndexedRelevantResource.getResources
import org.eclipse.emf.ecore.util.EcoreUtil.getURI
import org.eclipse.emf.ecore.resource.ResourceSet
import com.intellij.openapi.module.ModuleUtilCore
import org.intellij.plugins.relaxNG.validation.RngSchemaValidator.findVirtualFile
import com.intellij.openapi.roots.ProjectFileIndex




// Mostly taken from https://github.com/alexandrelombard/sarl/blob/idea_plugin/main/coreplugins/io.sarl.lang.intellij/src/org/eclipse/xtext/psi/impl/BaseXtextFile.java
class SarlPsiFile(viewProvider: FileViewProvider) :
        PsiFileBase(viewProvider, SarlLanguage.INSTANCE), ScopeNode {

    override fun getFileType(): FileType {
        return SarlFileType
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        // FIXME: What should I return?
        return null
    }

    override fun getContext(): ScopeNode? {
        return null
    }

    override fun getIcon(flags: Int): Icon? {
        return SarlIcons.SARL_PLUGIN
    }
}