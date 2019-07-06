package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.junit.Test

class ChandelierExitTest : TestBase() {

    @Test
    fun eval() {
        var arr = ChandelierExit(22, 3.0).eval(priceList)

        assertEqual(1359.08, arr.pos(0), "chandelierExit 0")
        assertEqual(1356.60, arr.pos(1), "chandelierExit 1")
        assertEqual(1376.86, arr.pos(20), "chandelierExit 20")
        assertEqual(1379.12, arr.pos(21), "chandelierExit 21")
        assertEqual(1374.90, arr.pos(22), "chandelierExit 22")
        assertEqual(2007.38, arr.pos(3950), "chandelierExit 3950")
        assertEqual(2030.88, arr.pos(size - 1), "chandelierExit last")

        arr = ChandelierExit(15, 2.5).eval(priceList)
        assertEqual(2020.99, arr.pos(size - 1), "chandelierExit last with different parameters")
    }
}