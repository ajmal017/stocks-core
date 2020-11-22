package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.functions.types.Indicator
import kotlin.test.Test
import kotlin.test.assertEquals

class AllTest {

    @Test
    fun correctEnumReturned() {
        for (i in Indicator.values()) {
            val instance = i.instance
            assertEquals(i, instance.id, "enum does not match instance")
        }
    }
}
