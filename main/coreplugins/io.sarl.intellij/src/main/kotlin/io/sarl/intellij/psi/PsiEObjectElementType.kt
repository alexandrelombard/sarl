package io.sarl.intellij.psi

import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.lang.sarl.SarlClass
import io.sarl.lang.sarl.SarlConstructor
import io.sarl.lang.sarl.SarlField
import io.sarl.lang.sarl.impl.SarlClassImplCustom
import io.sarl.lang.sarl.impl.SarlConstructorImpl
import io.sarl.lang.sarl.impl.SarlFieldImplCustom
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtend.core.macro.declaration.XtendClassDeclarationImpl

open class PsiEObjectElementType(val element: EObject, debugName: String = "EObject") :
        IElementType(debugName, SarlLanguage.INSTANCE)

class PsiSarlClassElementType(element: SarlClass) :
        PsiEObjectElementType(element, "SarlClass")
class PsiSarlConstructorElementType(element: SarlConstructor) :
        PsiEObjectElementType(element, "SarlConstructor")
class PsiSarlFieldElementType(element: SarlField) :
        PsiEObjectElementType(element, "SarlField")