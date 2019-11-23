package io.sarl.intellij.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

open class SarlPsiElement(node: ASTNode) : ASTWrapperPsiElement(node)
class EObjectPsiElement(node: ASTNode) : SarlPsiElement(node)
class NamedEObjectPsiElement(node: ASTNode) : SarlPsiElement(node)