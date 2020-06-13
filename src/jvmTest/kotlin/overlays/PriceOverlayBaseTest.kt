package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.functions.types.PriceOverlay
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