package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.functions.types.PriceOverlay
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceOverlayBaseTest : TestBase() {

    @Test
    fun correctEnumReturned() {
        for (o in PriceOverlay.values()) {
            val overlay = o.instance
            assertEquals("enum does not match instance", o, overlay.id)
        }
    }
}