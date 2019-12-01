package org.cerion.stocks.core.model

import java.util.Date

// Possibly useful omitted ones that can be calculated from the others
// Change from 52 week low/high
// %Change from 52 week low/high
// Change and percent from sma50/200
class Quote(val symbol: String) {
    val currentPrice get() = lastTrade

    var name: String? = null
    var exchange: String? = null
    var prevClose: Float = 0.toFloat()

    // Current Day
    var open: Float = 0.toFloat()
    var low: Float = 0.toFloat()
    var high: Float = 0.toFloat()
    var lastTrade: Float = 0.toFloat() //Most current price, same as close after hours
    var change: Float = 0.toFloat() // Change since previous date (lastTrade - prevClose)?
    var changePercent: Float = 0.toFloat()
    var volume: Long = 0
    var lastTradeDate: Date? = null

    var averageVolume: Long = 0
    var oneYearTarget: Float = 0.toFloat() // Average analysts estimate of price in 1 year
    var high52: Float = 0.toFloat()
    var low52: Float = 0.toFloat()

    var eps: Float = 0.toFloat()
    //public float epsEstCurrentYear;
    //public float epsEstNextYear;
    //public float epsEstNextQuarter;

    var bookValue: Float = 0.toFloat()
    var ebitda: String? = null // Earnings before interest, tax, depreciation and amortization
    var priceSalesRatio: Float = 0.toFloat()
    var priceBookRatio: Float = 0.toFloat()
    var peRatio: Float = 0.toFloat()
    var pegRatio: Float = 0.toFloat()
    //public float priceEPSEstCurrentYear;
    //public float priceEPSEstNextYear;
    var shortRatio: Float = 0.toFloat()

    var sma50: Float = 0.toFloat()
    var sma200: Float = 0.toFloat()

    var dividendYield: Float = 0.toFloat()
    var dividendDate: Date? = null
    var dividendsPerShare: Float = 0.toFloat()

    var marketCap: String? = null
    var sharesTotal: Long = 0
    var sharesFloat: Long = 0
    var revenue: String? = null

    // Other
    var beta: Float = 0.toFloat()
    var sector: String? = null

    fun validate(): Boolean {
        return !(name!!.contentEquals("N/A") || prevClose == 0f)

    }
}
