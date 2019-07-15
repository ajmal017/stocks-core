package org.cerion.stocklist

import java.util.*

data class PriceRow(
        override val date: Date,
        override val open: Float,
        override val high: Float,
        override val low: Float,
        override val close: Float,
        override val volume: Float) : IPrice {

    init {
        //Error checking
        if (open < low || close < low || open > high || close > high)
            throw RuntimeException("Price range inconsistency " + String.format("%s,%f,%f,%f,%f", formattedDate, open, high, low, close))
    }

    // TODO possibly only one use so remove later
    val formattedDate: String
        get() = Price.mDateFormat.format(date) //When it needs to be formatted properly
}