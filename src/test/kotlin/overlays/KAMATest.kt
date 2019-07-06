package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.junit.Test

class KAMATest : TestBase() {

    @Test
    fun kama_defaults() {
        val arr = KAMA().eval(priceList.close)

        // Not verified online yet
        assertEqual(2057.11, arr[arr.size - 1], "last")
    }

}