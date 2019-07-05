package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class VortexTest : TestBase() {

    @Test
    fun eval() {
        val arr = Vortex(14).eval(priceList)
        assertEqual(1.0, arr.getNeg(0), "vortex (-) 0")
        assertEqual(1.0, arr.getPos(0), "vortex (+) 0")
        assertEqual(0.83, arr.getNeg(1), "vortex (-) 0")
        assertEqual(0.17, arr.getPos(1), "vortex (+) 0")
        assertEqual(1.08, arr.getNeg(27), "vortex (-) 2p-1")
        assertEqual(0.92, arr.getPos(27), "vortex (+) 2p-1")
        assertEqual(1.10, arr.getNeg(28), "vortex (-) 2p")
        assertEqual(0.96, arr.getPos(28), "vortex (+) 2p")
        assertEqual(0.98, arr.getNeg(arr.size - 1), "vortex (-) last")
        assertEqual(0.94, arr.getPos(arr.size - 1), "vortex (+) last")
        assertEqual(-0.04, arr.diff(arr.size - 1), "vortex diff last")
    }
}