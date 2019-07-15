package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class PringsSpecialKTest : TestBase() {

    @Test
    fun pringsSpecialK_test() {
        val arr = PringsSpecialK().eval(priceList)

        // TODO verify values online, just doing these pre-refactor
        assertEqual(0.0, arr.first, "first")
        assertEqual(-57.52, arr[1], "position 1")
        assertEqual(-74.84, arr[2], "position 2")
        assertEqual(112.63, arr.last, "last")
    }
}