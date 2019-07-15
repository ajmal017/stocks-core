package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class AverageDirectionalIndexTest : TestBase() {

    @Test
    fun averageDirectionalIndex_defaults() {
        val adx = AverageDirectionalIndex().eval(priceList) // period is 14 by default

        assertEqual(0.0, adx.first, "first")
        assertEqual(50.0, adx[1], "position 1")
        assertEqual(66.67, adx[2], "position 2")
        assertEqual(15.04, adx.last, "last")
    }

    @Test
    fun averageDirectionalIndex_test_7() {
        val adx = AverageDirectionalIndex(7).eval(priceList)

        assertEqual(0.0, adx.first, "first")
        assertEqual(50.0, adx[1], "position 1")
        assertEqual(66.67, adx[2], "position 2")
        assertEqual(18.20, adx.last, "last")
    }

}