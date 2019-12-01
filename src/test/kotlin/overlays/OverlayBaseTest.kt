package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.functions.types.Overlay
import org.junit.Assert.assertEquals
import org.junit.Test

class OverlayBaseTest : TestBase() {

    @Test
    fun correctEnumReturned() {
        for (o in Overlay.values()) {
            val overlay = o.instance
            assertEquals("enum does not match instance", o, overlay.id)
        }
    }
}