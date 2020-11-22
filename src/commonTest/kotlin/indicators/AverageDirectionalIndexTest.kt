package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class AverageDirectionalIndexTest : TestBase() {

    @Test
    fun averageDirectionalIndex_defaults() = runPriceTest {
        val adx = AverageDirectionalIndex().eval(it) // period is 14 by default

        assertEqual(0.0, adx.first, "first")
        assertEqual(50.0, adx[1], "position 1")
        assertEqual(66.67, adx[2], "position 2")
        assertEqual(15.04, adx.last, "last")
    }

    @Test
    fun averageDirectionalIndex_test_7() = runPriceTest {
        val adx = AverageDirectionalIndex(7).eval(it)

        assertEqual(0.0, adx.first, "first")
        assertEqual(50.0, adx[1], "position 1")
        assertEqual(66.67, adx[2], "position 2")
        assertEqual(18.20, adx.last, "last")
    }

}