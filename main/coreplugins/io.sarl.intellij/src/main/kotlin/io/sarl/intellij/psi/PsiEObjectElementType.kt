package io.sarl.intellij.psi

import com.intellij.psi.tree.IElementType
import io.sarl.intellij.SarlLanguage
import io.sarl.lang.sarl.impl.SarlClassImplCustom
import io.sarl.lang.sarl.impl.SarlConstructorImpl
import io.sarl.lang.sarl.impl.SarlFieldImplCustom
import org.eclipse.emf.ecore.EObject

class PsiEObjectElementType(val element: EObject) : IElementType("EObject", SarlLanguage.INSTANCE)

class PsiSarlClassElementType(val element: SarlClassImplCustom) : IElementType("SarlClass", SarlLanguage.INSTANCE)
class PsiSarlConstructorElementType(val element: SarlConstructorImpl) : IElementType("SarlConstructor", SarlLanguage.INSTANCE)
class PsiSarlFieldElementType(val element: SarlFieldImplCustom) : IElementType("SarlField", SarlLanguage.INSTANCE)