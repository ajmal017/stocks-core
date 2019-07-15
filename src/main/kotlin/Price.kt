package org.cerion.stocklist

import java.text.SimpleDateFormat
import java.util.*

class Price(override val date: Date, override val open: Float, override val high: Float, override val low: Float, override val close: Float, override val volume: Float) : IPrice {

    //Fields used in list
    var parent: PriceList? = null
    var pos: Int = 0
    val formattedDate: String
        get() = mDateFormat.format(date) //When it needs to be formatted properly

    val dow: Int
        get() {
            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.DAY_OF_WEEK)
        }

    constructor(p: IPrice) : this(p.date, p.open, p.high, p.low, p.close, p.volume)

    init {
        //Error checking
        if (open < low || close < low || open > high || close > high)
            throw RuntimeException("Price range inconsistency " + String.format("%s,%f,%f,%f,%f", formattedDate, open, high, low, close))
    }

    //Slope of closing price
    fun slope(period: Int): Float {
        return parent!!.slope(period, pos)
    }

    //Typical price
    fun tp(): Float = parent!!.tp(pos)
    fun change(prev: Price): Float = getPercentDiff(prev)

    fun getPercentDiff(old: Price): Float {
        if (!old.date.before(date))
            throw RuntimeException("current price is older than input price")

        val diff = close - old.close
        return 100 * (diff / old.close)
    }

    companion object {
        private val mDateFormat = SimpleDateFormat("yyyy-MM-dd")

        fun getDecimal(value: Float): String {
            return String.format("%.2f", value)
        }
    }
}
