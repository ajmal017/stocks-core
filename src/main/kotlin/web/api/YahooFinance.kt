package org.cerion.stocks.core.web.api

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.Dividend
import org.cerion.stocks.core.model.Interval
import org.cerion.stocks.core.model.Quote
import org.cerion.stocks.core.web.Tools
import java.text.SimpleDateFormat
import java.util.*

class YahooFinance private constructor() {

    private var mCookieCrumb: String? = null
    private var mCookie: String? = null

    fun getPrices(symbol: String, interval: Interval, count: Int): PriceList {
        val start = getCalendar(interval, count)

        val prices = getPrices(symbol, interval, start.time)
        while (prices.size > count)
            prices.removeAt(0)

        val result = PriceList(symbol, prices)

        // Should never happen
        if (result.size > 0 && result.interval !== interval)
            throw RuntimeException("unexpected interval " + result.interval)

        println("Result lines= " + result.size)
        return result
    }

    fun getPrices(symbol: String, interval: Interval, start: Date): MutableList<PriceRow> {
        if (!setCookieCrumb())
            throw RuntimeException("Failed to get cookie")

        //https://query1.finance.yahoo.com/v7/finance/download/OHI
        // ?period1=1493915553
        // &period2=1496593953
        // &interval=1d
        // &events=history
        // &crumb=TSV3DSdPIjI

        var sURL = "https://query1.finance.yahoo.com/v7/finance/download/$symbol"
        sURL += "?period1=" + start.time / 1000
        sURL += "&period2=" + Date().time / 1000

        if (interval === Interval.MONTHLY)
            sURL += "&interval=1mo"
        else if (interval === Interval.WEEKLY)
            sURL += "&interval=1wk"
        else
            sURL += "&interval=1d"

        sURL += "&events=history"
        sURL += "&crumb=" + mCookieCrumb!!

        println(sURL)
        val res = Tools.getURL(sURL, mCookie)
        if (DEBUG) {
            println("Response size = " + res.result.length)
            if (res.result.length < 1000)
                println(res.result)
        }

        val sData = res.result
        return getPricesFromTable(sData)
    }

    fun getDividends(symbol: String): List<Dividend> {
        if (!setCookieCrumb())
            throw RuntimeException("Failed to get cookie")

        var sURL = "https://query1.finance.yahoo.com/v7/finance/download/$symbol"
        sURL += "?period1=946684800" // Jan 1, 2000
        sURL += "&period2=" + Date().time / 1000
        sURL += "&interval=1wk&events=div"
        sURL += "&crumb=" + mCookieCrumb!!

        val result = ArrayList<Dividend>()
        val res = Tools.getURL(sURL, mCookie)

        val sData = res.result
        val lines = sData.split("\\r\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in 1 until lines.size) {
            val line = lines[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val div = Dividend(parseDate(line[0])!!, java.lang.Float.parseFloat(line[1]))
            result.add(0, div)
        }

        return result
    }

    fun getQuotes(symbols: Set<String>): Map<String, Quote> {
        var flags = ""
        for (s in FLAGS)
            flags += s

        // String.join isn't working on android with a Set, possibly java v7/8 issue
        var symbols_join = ""
        for (s in symbols) {
            if (symbols_join.isNotEmpty())
                symbols_join += "+"
            symbols_join += s
        }

        val url = "https://download.finance.yahoo.com/d/quotes.csv?s=$symbols_join&f=$flags&e=.csv"
        val sResult = Tools.getURL(url)
        val lines = sResult!!.split("\r\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val result = HashMap<String, Quote>()

        var i = 0
        for (symbol in symbols) {
            val line = lines[i]
            if (line.isNotEmpty())
                result[symbol] = parseQuote(symbol, line)
            else
                result[symbol] = Quote(symbol)

            i++
        }

        return result
    }

    fun getQuote(symbol: String): Quote? {
        val symbols = HashSet(listOf(symbol))
        val map = getQuotes(symbols)

        return map[symbol]
    }

    private fun setCookieCrumb(): Boolean {
        if (mCookieCrumb != null)
            return true

        val res = Tools.getURL("https://finance.yahoo.com/quote/%5EGSPC/options", null) // most any page will work here
        val page = res.result
        var index = page.indexOf("\"CrumbStore\"")
        if (index > 0) {
            if (DEBUG) {
                val debug = page.substring(index, index + 50)
                println(debug)
            }

            index = page.indexOf("\"crumb\"", index)

            if (index > 0) {
                val start = page.indexOf("\"", index + 8) + 1
                val end = page.indexOf("\"", start)
                if (start < end) {
                    mCookieCrumb = page.substring(start, end)
                    mCookieCrumb = mCookieCrumb!!.replace("\\u002F", "/")
                    mCookie = res.headers["Set-Cookie"]!![0]
                    mCookie = mCookie!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    return true
                }
            }
        }

        return false
    }

    private fun getCalendar(interval: Interval, count: Int): Calendar {
        val cal = Calendar.getInstance()
        if (interval === Interval.DAILY)
        //This one is just an estimate since there are various days the market is closed
        {
            val trading_days_year = 250
            var remaining = count
            while (remaining >= trading_days_year) {
                cal.add(Calendar.YEAR, -1)
                remaining -= trading_days_year
            }

            val weekdays = (remaining * (365.0 / trading_days_year)).toInt()
            cal.add(Calendar.DAY_OF_YEAR, 1 - weekdays)
        } else if (interval === Interval.WEEKLY) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, -count - 1)
        } else if (interval === Interval.MONTHLY) {
            cal.add(Calendar.MONTH, -count)
            cal.set(Calendar.DAY_OF_MONTH, 1)
        }

        return cal
    }

    companion object {

        private val mDateFormat = SimpleDateFormat("yyyy-MM-dd")
        private val mDateFormatAlt = SimpleDateFormat("M/dd/yyyy")

        val instance = YahooFinance()

        private const val DEBUG = true

        /***
         * Gets PriceList from csv formatted file that API would return
         * @param tableData file contents as string
         * @return PriceList
         */
        fun getPricesFromTable(tableData: String): MutableList<PriceRow> {
            val lines = tableData.split("\\r\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (DEBUG)
                println("Table lines = " + lines.size)

            val prices = ArrayList<PriceRow>()
            for (i in 1 until lines.size) {
                if (!lines[i].contains("null"))
                    prices.add(parseLine(lines[i]))
                else
                    println("Ignoring line " + lines[i]) // new API issue some rows have all null values
            }

            return prices
        }

        // http://www.jarloo.com/yahoo_finance/
        private val FLAGS = arrayOf(
                //Pricing/Date
                "p", "o", "c1", "p2", "g", "h", "l1", "t8", "k", "j", "v", "a2", "d1",

                // Ratios and financial info
                "e", "e7", "e8", "e9", "b4", "j4", "p5", "p6", "r", "r5", "r6", "r7", "s7",

                // Averages
                "m3", "m4",

                // Misc info
                "j1", "f6", "j2", "n", "s", "x", "s6",

                // Dividends
                "y", "d", "q")

        private fun parseQuote(symbol: String, inputLine: String): Quote {
            var line = inputLine
            line = line.replace(", ", "&comma; ")
            val lines = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (lines.size != FLAGS.size)
                throw IndexOutOfBoundsException("quotes.csv result does not match parameters")

            val quote = Quote(symbol)

            for (i in FLAGS.indices) {
                val flag = FLAGS[i]
                val value = lines[i]

                when (flag) {
                    "s" -> {
                        val s = parseString(value)
                        if (!s.contentEquals(symbol))
                            throw IllegalStateException("parameter should match symbol")
                    }
                    "x" -> quote.exchange = parseString(value)
                    "n" -> quote.name = parseString(value)

                    "p" -> quote.prevClose = parseFloat(value)
                    "o" -> quote.open = parseFloat(value)
                    "g" -> quote.low = parseFloat(value)
                    "h" -> quote.high = parseFloat(value)
                    "l1" -> quote.lastTrade = parseFloat(value)
                    "c1" -> quote.change = parseFloat(value)
                    "p2" -> quote.changePercent = parseFloat(value)
                    "v" -> quote.volume = parseLong(value)
                    "d1" -> quote.lastTradeDate = parseDate(value)

                    "e" -> quote.eps = parseFloat(value)
                    //case "e7": quote.epsEstCurrentYear = parseFloat(value); break;
                    //case "e8": quote.epsEstNextYear = parseFloat(value); break;
                    //case "e9": quote.epsEstNextQuarter = parseFloat(value); break;

                    "b4" -> quote.bookValue = parseFloat(value)
                    "j4" -> quote.ebitda = parseString(value)
                    "p5" -> quote.priceSalesRatio = parseFloat(value)
                    "p6" -> quote.priceBookRatio = parseFloat(value)

                    "r" -> quote.peRatio = parseFloat(value)
                    "r5" -> quote.pegRatio = parseFloat(value)
                    //case "r6": quote.priceEPSEstCurrentYear = parseFloat(value); break;
                    //case "r7": quote.priceEPSEstNextYear = parseFloat(value); break;
                    "s7" -> quote.shortRatio = parseFloat(value)
                    "s6" -> quote.revenue = parseString(value)

                    "a2" -> quote.averageVolume = parseLong(value)
                    "t8" -> quote.oneYearTarget = parseFloat(value)
                    "k" -> quote.high52 = parseFloat(value)
                    "j" -> quote.low52 = parseFloat(value)

                    "m3" -> quote.sma50 = parseFloat(value)
                    "m4" -> quote.sma200 = parseFloat(value)

                    "j1" -> quote.marketCap = parseString(value)
                    "j2" -> quote.sharesTotal = parseLong(value)
                    "f6" -> quote.sharesFloat = parseLong(value)

                    "y" -> quote.dividendYield = parseFloat(value)
                    "q" -> quote.dividendDate = parseDate(value)
                    "d" -> quote.dividendsPerShare = parseFloat(value)
                }
            }

            return quote
        }

        private fun parseFloat(input: String): Float {
            var str = input

            try {
                str = str.replace("%", "")
                str = str.replace("\"", "")
                return java.lang.Float.parseFloat(str)
            } catch (e: NumberFormatException) {
                return 0f
            }

        }

        private fun parseLong(number: String): Long {
            var result: Long = 0

            try {
                result = java.lang.Long.parseLong(number)
            } catch (e: Exception) {
                //e.printStackTrace();
            }

            return result
        }

        private fun parseDate(inputDate: String): Date? {
            var date = inputDate
            var result: Date?
            date = date.replace("\"", "")
            try {
                result = mDateFormat.parse(date)
            } catch (e: Exception) {
                // Try alternate
                try {
                    result = mDateFormatAlt.parse(date)
                } catch (ee: Exception) {
                    result = null
                    //ee.printStackTrace();
                }

            }

            return result
        }

        private fun parseString(str: String): String {
            var result = str
            result = result.replace("\"", "")
            result = result.replace("&comma;", ",")
            return result
        }

        fun parseLine(sLine: String): PriceRow {
            val fields = sLine.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (fields.size == 7) {
                //TODO fix this for S&P large numbers
                try
                //Fails on S&P index since too large, just ignore it
                {
                    var open = java.lang.Float.parseFloat(fields[1])
                    var high = java.lang.Float.parseFloat(fields[2])
                    var low = java.lang.Float.parseFloat(fields[3])
                    val close = java.lang.Float.parseFloat(fields[4])
                    val adjClose = java.lang.Float.parseFloat(fields[5])

                    if (adjClose != close) {
                        if (close == open)
                            open = adjClose
                        else
                            open = adjClose * open / close

                        if (close == high)
                        //Fix float rounding issues to prevent close > high when they are the same
                            high = adjClose
                        else
                            high = adjClose * high / close

                        if (close == low)
                            low = adjClose
                        else
                            low = adjClose * low / close
                    }

                    // Correcting bad data
                    if (open < low) {
                        println("Correcting bad [open] data on " + fields[0])
                        open = (high + low) / 2
                    }

                    var volume = java.lang.Long.parseLong(fields[6], 10)
                    volume /= 1000
                    val date = mDateFormat.parse(fields[0])

                    return PriceRow(date, open, high, low, adjClose, volume.toFloat())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            throw Exception("Unexpected price line")
        }
    }
}
