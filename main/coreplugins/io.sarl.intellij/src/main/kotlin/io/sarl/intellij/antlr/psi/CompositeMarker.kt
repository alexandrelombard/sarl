package io.sarl.intellij.antlr.psi

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class CompositeMarker(
        val marker: PsiBuilder.Marker,
        val lookAhead: Int,
        val elementType: IElementType) {

    fun precede(elementType: IElementType) =
            CompositeMarker(marker.precede(), lookAhead, elementType)

    fun done() {
        marker.done(elementType)
    }

}