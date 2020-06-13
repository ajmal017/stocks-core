package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import org.junit.Test

class DirectionalIndexTest : TestBase() {

    @Test
    fun directionalIndex_defaults() {
        val arr = DirectionalIndex().eval(priceList)
        val last = arr.size - 1

        // TODO verify values online, just doing these pre-refactor
        assertEqual(30.29, arr.neg(last), "last")
        assertEqual(27.01, arr.pos(last), "last")
    }
}