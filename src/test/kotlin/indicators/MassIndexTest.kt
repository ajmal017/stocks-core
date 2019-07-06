package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class MassIndexTest : TestBase() {

    @Test
    fun eval() {
        val ma = MassIndex(23).eval(priceList)
        assertEqual(23.00, ma.get(0), "massIndex 0")
        assertEqual(23.83, ma.get(1), "massIndex 1")
        assertEqual(20.93, ma.get(10), "massIndex p-2")
        assertEqual(21.85, ma.get(22), "massIndex p-1")
        assertEqual(21.75, ma.get(23), "massIndex p")
        assertEqual(23.76, ma.last, "massIndex last")
    }
}