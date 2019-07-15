package org.cerion.stocklist

import java.util.*

data class PriceRow(
        override val date: Date,
        override val open: Float,
        override val high: Float,
        override val low: Float,
        override val close: Float,
        override val volume: Float) : IPrice {

    // TODO possibly only one use so remove later
    val formattedDate: String
        get() = Price.mDateFormat.format(date) //When it needs to be formatted properly
}