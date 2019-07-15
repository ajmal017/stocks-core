package org.cerion.stocklist.web

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.PriceRow
import org.cerion.stocklist.model.Dividend
import org.cerion.stocklist.model.Interval
import org.cerion.stocklist.model.Quote
import org.cerion.stocklist.model.Symbol
import org.cerion.stocklist.repository.DividendRepository
import org.cerion.stocklist.repository.PriceListRepository
import java.util.*

class RepositoryCachedAPI(private val mAPI: DataAPI, private val mPriceRepo: PriceListRepository, private val mDividendRepo: DividendRepository) : CachedDataAPI {

    override fun clearCache() {
        mPriceRepo.deleteAll()
        mDividendRepo.deleteAll()
    }

    @Throws(Exception::class)
    override fun getPrices(symbol: String, interval: Interval, start: Date, forceUpdate: Boolean): List<PriceRow> {

        val dates = mPriceRepo.getHistoricalDates(symbol, interval)
        var update = forceUpdate
        var retrieveFrom: Date? = null

        if (dates == null) {
            update = true
        } else {
            val now = Date()
            var diff = now.time - dates.lastUpdated!!.time
            diff /= (1000 * 60 * 60).toLong()
            val hours = diff
            val days = diff / 24

            println(symbol + " " + interval.name + " last updated " + dates.lastUpdated + " (" + diff + " days ago)")

            // TODO, smarter updates based on last price obtained and weekends
            if (interval === Interval.DAILY && hours >= 12)
                update = true
            else if (interval === Interval.WEEKLY && days > 3)
                update = true
            else if (interval === Interval.MONTHLY && days > 7)
                update = true


            // Incremental update, not sure if all this is necessary but start a few data points earlier to be safe
            if (update) {
                val cal = Calendar.getInstance()
                cal.time = dates.lastDate!!

                when (interval) {
                    Interval.DAILY -> cal.add(Calendar.DAY_OF_MONTH, -1)
                    Interval.WEEKLY -> cal.add(Calendar.DAY_OF_MONTH, -7)
                    Interval.MONTHLY -> cal.add(Calendar.DAY_OF_MONTH, -31)
                    Interval.QUARTERLY,
                    Interval.YEARLY -> throw Exception("Only daily/weekly/monthly allowed")
                }

                retrieveFrom = cal.time
            }
        }

        if (retrieveFrom != null) {
            updatePricesIncremental(symbol, interval, start, retrieveFrom)
        } else if (update)
            updatePrices(symbol, interval, start)

        return mPriceRepo.get(symbol, interval)
    }

    @Throws(Exception::class)
    override fun getPrices(symbol: String, interval: Interval, start: Date): List<PriceRow> {
        return getPrices(symbol, interval, start, false)
    }

    override fun getDividends(symbol: String): List<Dividend> {
        val dates = mDividendRepo.getHistoricalDates(symbol)
        var update = false

        if (dates == null) {
            update = true
        } else {
            val now = Date()
            var diff = now.time - dates.lastUpdated!!.time
            diff /= (1000 * 60 * 60 * 24).toLong()
            //Log.d(TAG, symbol + " " + interval.name() + " last updated " + dates.LastUpdated + " (" + diff + " days ago)");

            // TODO based it on the following
            // IF most recent dividend is less than 30 days old, check at most once a week
            // IF no dividends, check once a month, probably wont ever be any
            // If new dividend expected soon, check daily
            if (diff > 7)
                update = true
        }

        if (update) {
            // TODO API should fail if it doesn't get a valid response, difference between error and success
            val dividends = mAPI.getDividends(symbol)
            mDividendRepo.add(symbol, dividends) //updatePrices(symbol, interval);
        }

        return mDividendRepo.get(symbol)
    }

    override fun getSymbols(symbols: Set<String>): List<Symbol> {
        return mAPI.getSymbols(symbols)
    }

    override fun getSymbol(symbol: String): Symbol? {
        return mAPI.getSymbol(symbol)
    }

    override fun getQuotes(symbols: Set<String>): Map<String, Quote> {
        return mAPI.getQuotes(symbols)
    }

    override fun getQuote(symbol: String): Quote? {
        return mAPI.getQuote(symbol)
    }

    @Throws(Exception::class)
    private fun updatePricesIncremental(symbol: String, interval: Interval, firstDate: Date, startFrom: Date) {
        println("Incremental update starting from $startFrom")

        val newPrices = mAPI.getPrices(symbol, interval, startFrom)

        // TODO add test so priceRepo must return date sorted prices or throw exception
        // Check if existing prices matches at start
        var merge = false
        val first = newPrices[0]
        val currPrices = mPriceRepo.get(symbol, interval)
        for (p in currPrices) {
            if (p.date == first.date) {
                if (p.close == first.close &&
                        p.volume == first.volume &&
                        p.open == first.open &&
                        p.high == first.high &&
                        p.low == first.low)
                    merge = true
            }
        }

        if (merge) {
            val mergedList = ArrayList<PriceRow>()
            for (p in currPrices) {
                if (p.date == first.date) {
                    mergedList.addAll(newPrices)
                    break
                }

                mergedList.add(p)
            }

            val list = PriceList(symbol, mergedList)
            mPriceRepo.add(list)
            val diff = mergedList.size - currPrices.size
            println("Updated prices for $symbol, added $diff")

        } else {
            // Possible split or dividend
            println("Unable to merge, prices do not match")
            updatePrices(symbol, interval, firstDate)
        }
    }

    @Throws(Exception::class)
    private fun updatePrices(symbol: String, interval: Interval, startDate: Date) {
        var list: PriceList? = null
        try {
            val cal = Calendar.getInstance()
            cal.set(1990, Calendar.JANUARY, 1)

            val prices = mAPI.getPrices(symbol, interval, startDate)
            list = PriceList(symbol, prices)
        } catch (e: Exception) {
            // nothing
        }

        if (list != null && list.size > 0) {
            mPriceRepo.add(list)
            println("Updated prices for $symbol")
        } else {
            throw Exception("Failed to get updated prices for $symbol")
        }
    }
}
