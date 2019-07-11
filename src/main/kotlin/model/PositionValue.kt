package org.cerion.stocklist.model

import org.cerion.stocklist.PriceList
import java.text.SimpleDateFormat
import java.util.*

// TODO rename this to just Position after existing class can be renamed to something better (PurchaseLot maybe)
class PositionValue(private val purchase: Position, private val priceList: PriceList?) {

    private val dividendsReinvested = purchase.dividendsReinvested
    private val date: Date = purchase.date
    private val origPrice = purchase.origPrice
    var quote: Quote? = null
    private var origPriceAdjusted: Double = 0.toDouble()

    var dividendHistory: DividendHistory? = null
        private set

    init {
        if (priceList != null)
            setPriceHistory(priceList)
    }

    val currPrice: Double
        get() =
            when {
                quote != null -> quote!!.lastTrade.toDouble()
                priceList != null -> priceList.last.close.toDouble()
                else -> 0.0
            }

    /**
     * Gets the difference between current price and purchase price
     * @return price difference
     */
    val change: Double
        get() {
            if (dividendsReinvested) {
                val percent = percentChanged
                return origPrice * percent
            }

            return currPrice - origPrice
        }

    /**
     * Gets percent difference between current and original/purchase price
     * @return percent difference
     */
    val percentChanged: Double
        get() = if (dividendsReinvested) {
            (currPrice - origPriceAdjusted) / origPriceAdjusted
        } else (currPrice - origPrice) / origPrice

    /**
     * Gets percent difference between current/original price including dividends
     * @return percent difference
     */
    val percentChangedWithDividends: Double
        get() {
            val origValue = origValue
            val currValueDiv = currValue + dividendProfit
            return (currValueDiv - origValue) / origValue
        }

    /**
     * Gets total dividends earned in dollars
     * @return amount earned
     */
    val dividendProfit: Double
        get() {
            if (dividendsReinvested || dividendHistory == null)
                return 0.0

            return dividendHistory!!.totalDividends * purchase.count
        }

    /**
     * Get price change between original and current price in dollars
     * @return profit in dollars
     */
    val profit: Double
        get() =
            if (dividendsReinvested)
                origValue * percentChanged
            else
                currValue - origValue


    /**
     * Current value/cost in dollars of all shares
     * @return cost of current lot
     */
    val currValue: Double
        get() {
            if (dividendsReinvested) {
                val value = (currPrice * currCount * 100.0).toInt()
                return value / 100.0
            }

            return currPrice * purchase.count
        }

    val valueHistory: List<Double>
        get() {
            if (priceList == null)
                return listOf()

            var start = 0
            for (p in priceList) {
                if (this.dateEquals(p.date, date)) {
                    start = p.pos
                    break
                }
            }

            val result = ArrayList<Double>()
            for (i in start until priceList.size - 1) {
                result.add(origValue)
            }

            result.add(currValue)
            return result
        }

    /**
     * Gets the current count, same as count unless dividends reinvested
     * @return Quantity of shares
     */
    val currCount: Double
        get() {
            if (dividendsReinvested) {
                val profit = profit
                val currValue = origValue + profit
                var shares = currValue / currPrice
                val iShares = (shares * 1000).toInt()
                shares = iShares / 1000.0

                return shares
            }

            return purchase.count
        }

    private val origValue: Double
        get() = purchase.origValue

    /**
     * Add dividends earned to the value of this position
     * @param list Historical list of dividends
     */
    fun addDividends(list: List<Dividend>) {
        this.dividendHistory = DividendHistory(list, date)
    }

    private fun setPriceHistory(list: PriceList) {
        if (list.interval === Interval.DAILY) {
            if (dividendsReinvested) {
                for (i in list.indices) {
                    val date = list.mDate[i]
                    // If entered today then currrent date may not be present so just use last entry
                    // TODO add unit test to check this value
                    if (dateEquals(date, this.date) || origPriceAdjusted == 0.0 && i == list.size - 1) {
                        origPriceAdjusted = list.close(i).toDouble()
                    }
                }
            }
        } else {
            throw IllegalArgumentException("daily prices required")
        }
    }

    private fun dateEquals(d1: Date, d2: Date): Boolean {
        return dayFormat.format(d1) == dayFormat.format(d2)
    }

    companion object {
        private val dayFormat = SimpleDateFormat("yyyyMMdd")
    }
}
