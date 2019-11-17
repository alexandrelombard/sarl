// copied from ANTLR 4 Java runtime
/*
 * [The "BSD license"]
 *  Copyright (c) 2012 Terence Parr
 *  Copyright (c) 2012 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.sarl.intellij.antlr.psi

import com.intellij.lang.Language
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import io.sarl.intellij.antlr.lexer.RuleIElementType
import io.sarl.intellij.antlr.lexer.TokenIElementType

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

object Trees {
    interface Predicate<T> {
        fun test(t: T): Boolean
    }

    /** Return a list of all ancestors of this node.  The first node of
     * list is the root and the last is the parent of this node.
     */
    fun getAncestors(t: PsiElement): List<PsiElement> {
        var t = t
        if (t.parent == null) return emptyList()
        val ancestors = ArrayList<PsiElement>()
        t = t.parent
        while (t != null) {
            ancestors.add(0, t) // insert at start
            t = t.parent
        }
        return ancestors
    }

    /** Return true if t is u's parent or a node on path to root from u.
     * Use == not equals().
     */
    fun isAncestorOf(t: PsiElement?, u: PsiElement?): Boolean {
        if (t == null || u == null || t.parent == null) return false
        var p: PsiElement? = u.parent
        while (p != null) {
            if (t === p) return true
            p = p.parent
        }
        return false
    }

    fun getRoot(t: PsiElement): AntlrPsiNode {
        val contextOfType = PsiTreeUtil.getParentOfType(t, PsiFile::class.java)
        return Trees.getChildren(contextOfType)[0] as AntlrPsiNode
    }

    /** From collection of nodes, make a map from the text of the node to the
     * node.
     */
    fun toMap(nodes: Collection<PsiElement>): Map<String, PsiElement> {
        val m = HashMap<String, PsiElement>()

        for (node in nodes) {
            m[node.text] = node
        }
        return m
    }

    fun findAllTokenNodes(t: PsiElement, ttype: Int): Collection<PsiElement> {
        return findAllNodes(t, ttype, true)
    }

    fun findAllRuleNodes(t: PsiElement, ruleIndex: Int): Collection<PsiElement> {
        return findAllNodes(t, ruleIndex, false)
    }

    fun findAllNodes(t: PsiElement, index: Int, findTokens: Boolean): List<PsiElement> {
        val nodes = ArrayList<PsiElement>()
        _findAllNodes(t, index, findTokens, nodes)
        return nodes
    }

    fun _findAllNodes(t: PsiElement, index: Int, findTokens: Boolean,
                      nodes: MutableList<in PsiElement>) {
        // check this node (the root) first
        if (findTokens && t is LeafPsiElement) {
            val elType = t.node.elementType
            if (elType is TokenIElementType) {
                if (elType.antlrTokenType == index) {
                    nodes.add(t)
                }
            }
        } else if (!findTokens && t is AntlrPsiNode) {
            val elType = t.node.elementType
            if (elType is RuleIElementType) {
                if (elType.ruleIndex == index) nodes.add(t)
            }
        }
        // check children
        for (c in t.children) {
            _findAllNodes(c, index, findTokens, nodes)
        }
    }

    /** Get all descendents; includes t itself.  */
    fun getDescendants(t: PsiElement): List<PsiElement> {
        val nodes = ArrayList<PsiElement>()
        nodes.add(t)

        for (c in t.children) {
            nodes.addAll(getDescendants(c))
        }
        return nodes
    }

    /** Get all non-WS, non-Comment children of t  */
    fun getChildren(t: PsiElement?): Array<PsiElement> {
        if (t == null) return PsiElement.EMPTY_ARRAY

        var psiChild: PsiElement? = t.firstChild ?: return PsiElement.EMPTY_ARRAY

        val result = ArrayList<PsiElement>()
        while (psiChild != null) {
            if (psiChild !is PsiComment && psiChild !is PsiWhiteSpace) {
                result.add(psiChild)
            }
            psiChild = psiChild.nextSibling
        }
        return PsiUtilCore.toPsiElementArray(result)
    }


    /** Find smallest subtree of t enclosing range startCharIndex..stopCharIndex
     * inclusively using postorder traversal.  Recursive depth-first-search.
     */
    fun getRootOfSubtreeEnclosingRegion(t: PsiElement,
                                        startCharIndex: Int, // inclusive
                                        stopCharIndex: Int)  // inclusive
            : PsiElement? {
        for (c in t.children) {
            val sub = getRootOfSubtreeEnclosingRegion(c, startCharIndex, stopCharIndex)
            if (sub != null) return sub
        }
        val elementType = t.node.elementType
        if (elementType is RuleIElementType) {
            val r = t.node.textRange
            // is range fully contained in t?  Note: jetbrains uses exclusive right end (use < not <=)
            if (startCharIndex >= r.startOffset && stopCharIndex < r.endOffset) {
                return t
            }
        }
        return null
    }

    /** Return first node satisfying the pred among descendants. Depth-first order. Test includes t itself.  */
    fun findNodeSuchThat(t: PsiElement, pred: Predicate<PsiElement>): PsiElement? {
        if (pred.test(t)) return t

        for (c in t.children) {
            val u = findNodeSuchThat(c, pred)
            if (u != null) return u
        }
        return null
    }

    fun createLeafFromText(project: Project, language: Language, context: PsiElement,
                           text: String, type: IElementType): PsiElement? {
        val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
        val el = factory.createElementFromText(text, language, type, context) ?: return null
        return PsiTreeUtil.getDeepestFirst(el) // forces parsing of file!!
        // start rule depends on root passed in
    }

    fun createFile(project: Project, language: Language, text: String): PsiFile? {
        val ftype = language.associatedFileType ?: return null
        val ext = ftype.defaultExtension
        val fileName = "___fubar___.$ext" // random name but must have correct extension
        val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
        return factory.createFileFromText(fileName, language, text, false, false)
    }
}
