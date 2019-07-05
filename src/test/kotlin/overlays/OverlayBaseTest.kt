package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.functions.types.Overlay
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