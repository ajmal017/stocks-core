package org.cerion.stocks.core.repository

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.HistoricalDates
import org.cerion.stocks.core.model.Interval
import org.cerion.stocks.core.web.DataAPI
import java.util.*


interface IPriceListRepository {
    fun add(list: PriceList)
    fun get(symbol: String, interval: Interval): PriceList?
}

class CachedPriceListRepository(private val repo: IPriceListRepository, private val api: DataAPI) {

    fun get(symbol: String, interval: Interval, startDate: Date?): PriceList {

        val result = repo.get(symbol, interval)
        var update = false
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
            throw NotImplementedError("add incremental updating")
            //updatePricesIncremental(symbol, interval, start, retrieveFrom)
        }
        else if (update)
            return updatePrices(symbol, interval, startDate!!)
        else
            return result!!
    }

    private fun updatePrices(symbol: String, interval: Interval, startDate: Date): PriceList {
        var list: PriceList? = null
        try {
            val cal = Calendar.getInstance()
            cal.set(1990, Calendar.JANUARY, 1)

            list = api.getPriceList(symbol, interval, startDate)
        } catch (e: Exception) {
            // nothing
        }

        if (list != null && list.size > 0) {
            repo.add(list)
            println("Updated prices for $symbol")
        } else {
            throw Exception("Failed to get updated prices for $symbol")
        }

        return list
    }
}

@Deprecated("use new version and rename when this is removed")
interface PriceListRepository {
    fun add(list: PriceList)

    fun getHistoricalDates(symbol: String, interval: Interval): HistoricalDates?

    @Deprecated("use PriceList version")
    fun get(symbol: String, interval: Interval): List<PriceRow>

    fun getList(symbol: String, interval: Interval): PriceList?

    fun deleteAll()
    
    @Deprecated("")
    operator fun get(symbol: String, interval: Interval, max: Int): PriceList
}
