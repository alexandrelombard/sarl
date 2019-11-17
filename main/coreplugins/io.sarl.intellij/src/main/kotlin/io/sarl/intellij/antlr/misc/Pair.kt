/*
 * Copyright (c) 2012-2017 The ANTLR Project. All rights reserved.
 * Use of this file is governed by the BSD 3-clause license that
 * can be found in the LICENSE.txt file in the project root.
 */

package io.sarl.intellij.antlr.misc

import java.io.Serializable

class Pair<A, B>(val a: A, val b: B) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        } else if (other !is Pair<*, *>) {
            return false
        }

        val otherPair = other as Pair<*, *>?
        return ObjectEqualityComparator.INSTANCE.equals(a, otherPair!!.a) && ObjectEqualityComparator.INSTANCE.equals(b, otherPair.b)
    }

    override fun hashCode(): Int {
        var hash = MurmurHash.initialize()
        hash = MurmurHash.update(hash, a)
        hash = MurmurHash.update(hash, b)
        return MurmurHash.finish(hash, 2)
    }

    override fun toString(): String {
        return String.format("(%s, %s)", a, b)
    }
}