package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class KAMATest : TestBase() {

    @Test
    fun kama_defaults() = runPriceTest {
        val arr = KAMA().eval(it.close)

        // Not verified online yet
        assertEqual(2057.11, arr[arr.size - 1], "last")
    }

}