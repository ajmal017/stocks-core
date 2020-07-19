package org.cerion.stocks.core.functions

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.Utils
import org.cerion.stocks.core.functions.types.IFunctionEnum
import org.cerion.stocks.core.functions.types.Indicator
import org.cerion.stocks.core.functions.types.Overlay
import org.cerion.stocks.core.functions.types.PriceOverlay
import org.cerion.stocks.core.indicators.MACD
import org.cerion.stocks.core.overlays.BollingerBands
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.*

class FunctionBaseTest : TestBase() {

    @Test
    fun hashCodeUniqueness() {
        val values = ArrayList<IFunctionEnum>()
        values.addAll(listOf(*Overlay.values()))
        values.addAll(listOf(*PriceOverlay.values()))
        values.addAll(listOf(*Indicator.values()))

        val size = values.size
        val map = HashMap<IFunction, String>()

        for (f in values) {
            val function = f.instance
            //TODO, create multiple versions variations of default values
            map[function] = ""
        }

        assertEquals("no values returned", true, size > 0)
        assertEquals("map does not match size", size.toLong(), map.size.toLong())
    }

    @Test
    fun hashCodeUnique_WithSameOrdinal() {
        val overlay = Overlay.values()[0]
        val po = PriceOverlay.values()[0]
        val map = HashMap<IFunction, String>()

        assertEquals("expected functions do not match", overlay.ordinal.toLong(), po.ordinal.toLong())
        val params = arrayOf<Number>(0)

        val call1 = overlay.getInstance(*params)
        val call2 = po.getInstance(*params)
        val call3 = overlay.getInstance(*params)
        val call4 = po.getInstance(*params)

        assertEquals("hash code should match", call1.hashCode().toLong(), call2.hashCode().toLong())

        map[call1] = ""
        map[call2] = ""
        assertEquals("unique functions mapped to same value", map.size.toLong(), 2)

        map[call3] = ""
        map[call4] = ""
        assertEquals("same functions should not be mapped", map.size.toLong(), 2)
    }

    @Test
    fun equals_checksParameters() {
        val call1 = Overlay.EMA.getInstance(10)
        val call2 = Overlay.EMA.getInstance(10)
        val call3 = Overlay.EMA.getInstance(20)

        assertEquals("should be equal", call1, call2)
        assertNotEquals("should not be equal", call1, call3)
    }

    @Test
    fun parametersVerified_DecimalType() {
        //All types should work on decimal input, no exceptions thrown
        var call: IFunction = BollingerBands(20, 2.0)
        call.eval(Utils.generateList(50))

        call = BollingerBands(20, 2.0)
        call.eval(Utils.generateList(50))

        //Int
        call = BollingerBands(20, 2.0)
        call.eval(Utils.generateList(50))
    }

    @Test(expected = IllegalArgumentException::class)
    fun setParams_tooMany() {
        val call = BollingerBands(20, 2.0)
        call.setParams(20, 10, 10)
    }

    @Test(expected = IllegalArgumentException::class)
    fun setParams_tooFew() {
        val call = BollingerBands(20, 2.0)
        call.setParams(20)
    }

    @Test(expected = IllegalArgumentException::class)
    fun setParams_typeMismatch() {
        val call = BollingerBands(20, 2.0)
        call.setParams(20, 10)
    }

    @Test
    fun setParams_convertsDoubleToFloat() {
        val call = BollingerBands()
        assertEquals(Float::class, call.params[1]::class)

        call.setParams(20, 2.0)
        assertEquals(Float::class, call.params[1]::class)
    }

    @Test
    fun verifyReturnTypes_simpleOverlays() {

        for (o in Overlay.values()) {
            val overlay = o.instance
            var arr = overlay.eval(priceList.close)
            assertEquals("'$o' resultType() does not match eval() result", arr.javaClass, overlay.resultType)

            // Verify when called on both evals
            arr = overlay.eval(priceList)
            assertEquals("'$o' resultType() does not match eval() result (2)", arr.javaClass, overlay.resultType)
        }
    }

    @Test
    fun verifyReturnTypes_priceOverlays() {
        for (o in PriceOverlay.values()) {
            val overlay = o.instance
            val arr = overlay.eval(priceList)

            assertEquals("'$o' resultType() does not match eval() result", arr.javaClass, overlay.resultType)
        }
    }

    @Test
    fun verifyReturnTypes_indicators() {
        for (i in Indicator.values()) {
            val indicator = i.instance
            val arr = indicator.eval(priceList)

            assertEquals("'$i' resultType() does not match eval() result", arr.javaClass, indicator.resultType)
        }
    }

    @Test
    fun functionBase_serialize() {
        assertEquals("SMA(22)", SimpleMovingAverage(22).serialize())
        assertEquals("BB(30,2.1)", BollingerBands(30, 2.1).serialize())
        assertEquals("MACD(2,3,4)", MACD(2,3,4).serialize())
    }
}