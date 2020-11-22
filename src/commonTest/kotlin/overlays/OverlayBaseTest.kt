package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.functions.types.Overlay
import kotlin.test.Test
import kotlin.test.assertEquals

class OverlayBaseTest {

    @Test
    fun correctEnumReturned() {
        for (o in Overlay.values()) {
            val overlay = o.instance
            assertEquals(o, overlay.id, "enum does not match instance")
        }
    }
}