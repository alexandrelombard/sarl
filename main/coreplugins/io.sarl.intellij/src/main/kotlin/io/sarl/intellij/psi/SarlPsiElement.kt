package io.sarl.intellij.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

open class SarlPsiElement(node: ASTNode) : ASTWrapperPsiElement(node)
open class EObjectPsiElement(node: ASTNode, val elementType: PsiEObjectElementType) : SarlPsiElement(node)