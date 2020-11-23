package org.cerion.stocks.core.functions

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.functions.types.IFunctionEnum
import org.cerion.stocks.core.functions.types.Indicator
import org.cerion.stocks.core.functions.types.Overlay
import org.cerion.stocks.core.functions.types.PriceOverlay
import org.cerion.stocks.core.indicators.MACD
import org.cerion.stocks.core.overlays.BollingerBands
import org.cerion.stocks.core.overlays.ExpMovingAverage
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.cerion.stocks.core.platform.KMPDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class BaseClass {
    abstract fun eval(): Number
}
class SomeClassOne : BaseClass() {
    override fun eval(): Int = 55
}

class SomeClassTwo : BaseClass() {
    override fun eval(): Double = 5.5
}

fun run(foo: BaseClass, bar: BaseClass) {
    // TODO check if foo.eval() returns the same type as bar.eval()
}

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

        assertEquals(true, size > 0, "no values returned")
        assertEquals(size.toLong(), map.size.toLong(), "map does not match size")
    }

    @Test
    fun hashCodeUnique_WithSameOrdinal() {
        val overlay = Overlay.values()[0]
        val po = PriceOverlay.values()[0]
        val map = HashMap<IFunction, String>()

        assertEquals(overlay.ordinal, po.ordinal, "expected functions do not match")
        val params = arrayOf<Number>(0)

        val call1 = overlay.getInstance(*params)
        val call2 = po.getInstance(*params)
        val call3 = overlay.getInstance(*params)
        val call4 = po.getInstance(*params)

        assertEquals(call1.hashCode(), call2.hashCode(), "hash code should match")

        map[call1] = ""
        map[call2] = ""
        assertEquals(map.size, 2, "unique functions mapped to same value")

        map[call3] = ""
        map[call4] = ""
        assertEquals(map.size, 2, "same functions should not be mapped")
    }

    @Test
    fun equals_checksClassName() {
        val call1 = ExpMovingAverage(10)
        val call2 = SimpleMovingAverage(10)
        assertNotEquals(call1, call2)
    }
    @Test
    fun equals_checksParameters() {
        val call1 = Overlay.EMA.getInstance(10)
        val call2 = Overlay.EMA.getInstance(10)
        val call3 = Overlay.EMA.getInstance(20)

        assertEquals(call1, call2, "should be equal")
        assertNotEquals(call1, call3, "should not be equal")
    }

    @Test
    fun parametersVerified_DecimalType() {
        //All types should work on decimal input, no exceptions thrown
        var call: IFunction = BollingerBands(20, 2.0)
        call.eval(generateList(50))

        call = BollingerBands(20, 2.0)
        call.eval(generateList(50))

        //Int
        call = BollingerBands(20, 2.0)
        call.eval(generateList(50))
    }

    @Test
    fun verifyReturnTypes_simpleOverlays() = runPriceTest {
        for (o in Overlay.values()) {
            val overlay = o.instance
            var arr = overlay.eval(it.close)
            assertEquals(arr::class, overlay.resultType, "'$o' resultType() does not match eval() result")

            // Verify when called on both evals
            arr = overlay.eval(it)
            assertEquals(arr::class, overlay.resultType, "'$o' resultType() does not match eval() result (2)")
        }
    }

    @Test
    fun verifyReturnTypes_priceOverlays() = runPriceTest {
        for (o in PriceOverlay.values()) {
            val overlay = o.instance
            val arr = overlay.eval(it)

            assertEquals(arr::class, overlay.resultType, "'$o' resultType() does not match eval() result")
        }
    }

    @Test
    fun verifyReturnTypes_indicators() = runPriceTest {
        for (i in Indicator.values()) {
            val indicator = i.instance
            val arr = indicator.eval(it)

            assertEquals(arr::class, indicator.resultType, "'$i' resultType() does not match eval() result")
        }
    }

    @Test
    fun functionBase_serialize() {
        assertEquals("SMA(22)", SimpleMovingAverage(22).serialize())
        assertEquals("BB(30,2.1)", BollingerBands(30, 2.1).serialize())
        assertEquals("MACD(2,3,4)", MACD(2,3,4).serialize())
    }

    @Deprecated("may be actual function on PriceList to do this now")
    private fun generateList(size: Int): PriceList {
        val prices = ArrayList<PriceRow>()
        for (i in 0 until size)
            prices.add(PriceRow(KMPDate.TODAY, i.toFloat(), i.toFloat(), i.toFloat(), i.toFloat(), i.toFloat()))

        return PriceList("TEST", prices)
    }
}