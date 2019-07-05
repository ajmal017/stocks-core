package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class DirectionalIndexTest : TestBase() {

    @Test
    fun directionalIndex_defaults() {
        val arr = DirectionalIndex().eval(priceList)
        val last = arr.size - 1

        // TODO verify values online, just doing these pre-refactor
        assertEqual(30.29, arr.getNeg(last), "last")
        assertEqual(27.01, arr.getPos(last), "last")
    }
}