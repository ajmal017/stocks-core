package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class UlcerIndexTest : TestBase() {

    @Test
    fun eval() = runPriceTest {
        val arr = UlcerIndex(14).eval(it)

        // TODO stock charts has a different value for this but their calculation might be wrong
        // This one matches if you do sqrt twice which seems wrong
        assertEqual(0.0, arr.first, "ulcer 0")
        assertEqual(1.02, arr[1], "ulcer 1")
        assertEqual(1.93, arr[13], "ulcer 13")
        assertEqual(2.25, arr[14], "ulcer 14")
        assertEqual(2.63, arr.last, "ulcer last")
    }
}