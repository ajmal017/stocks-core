package org.cerion.stocks.core.web

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.Dividend
import org.cerion.stocks.core.model.Symbol
import org.cerion.stocks.core.repository.DividendRepository
import java.util.*

// TODO add symbols those can be cached too

class RepositoryCachedAPI(private val webApi: DataAPI, private val dividendRepo: DividendRepository) : DataAPI {

    override fun getPriceList(symbol: String, interval: FetchInterval, start: Date?): PriceList {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPrices(symbol: String, interval: FetchInterval, start: Date): List<PriceRow> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDividends(symbol: String): List<Dividend> {
        TODO("Not yet implemented")
    }

    /*
    override fun getPriceList(symbol: String, interval: Interval, start: Date): PriceList {
        return getPriceList(symbol, interval, start, false)
    }

    fun getPriceList(symbol: String, interval: Interval, start: Date, forceUpdate: Boolean): PriceList {

        var result = mPriceRepo.getList(symbol, interval)
        var update = forceUpdate
        var retrieveFrom: Date? = null

        if (result == null) {
            update = true
        }
        else if(result.lastUpdated != null) {
            val now = Date()
            var diff = now.time - result.lastUpdated!!.time
            diff /= (1000 * 60 * 60).toLong()
            val hours = diff
            val days = diff / 24

            println(symbol + " " + interval.name + " last updated " + result.lastUpdated + " (" + diff + " days ago)")

            // TODO, smarter updates based on last price obtained and weekends
            if (interval === Interval.DAILY && hours >= 12)
                update = true
            else if (interval === Interval.WEEKLY && days > 3)
                update = true
            else if (interval === Interval.MONTHLY && days > 7)
                update = true

            // Incremental update, not sure if all this is necessary but start a few data points earlier to be safe
            // TODO this may be working but do full update and re-verify this later
            /*
            if (update) {
                val cal = Calendar.getInstance()
                cal.time = result.last.date

                when (interval) {
                    Interval.DAILY -> cal.add(Calendar.DAY_OF_MONTH, -1)
                    Interval.WEEKLY -> cal.add(Calendar.DAY_OF_MONTH, -7)
                    Interval.MONTHLY -> cal.add(Calendar.DAY_OF_MONTH, -31)
                    Interval.QUARTERLY,
                    Interval.YEARLY -> throw Exception("Only daily/weekly/monthly allowed")
                }

                retrieveFrom = cal.time
            }
             */
        }

        if (retrieveFrom != null) {
            updatePricesIncremental(symbol, interval, start, retrieveFrom)
        }
        else if (update)
            updatePrices(symbol, interval, start)
        else
            return result!!

        // Result should have been added above
        return mPriceRepo.getList(symbol, interval)!!
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

     */

    override fun getSymbol(symbol: String): Symbol? {
        return webApi.getSymbol(symbol)
    }

    /*
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
     */
}
