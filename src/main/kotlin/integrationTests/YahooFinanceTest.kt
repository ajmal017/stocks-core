package org.cerion.stocklist.integrationTests

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.model.Interval
import org.cerion.stocklist.web.api.YahooFinance

class YahooFinanceTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            priceHistory()
        }

        private fun priceHistory() {
            val api = YahooFinance.instance

            var list: PriceList?
            val size = 100

            for(i in 0..19) {
                println(i)

                list =
                        when(i) {

                            0 -> api.getPrices("OHI", Interval.DAILY, size)
                            1 -> api.getPrices("OHI", Interval.WEEKLY, size)
                            2 -> api.getPrices("OHI", Interval.MONTHLY, size)

                            3 -> api.getPrices("^GSPC", Interval.DAILY, size)
                            4 -> api.getPrices("^GSPC", Interval.WEEKLY, size)
                            5 -> api.getPrices("^GSPC", Interval.MONTHLY, size)

                            6 -> api.getPrices("FNMIX", Interval.DAILY, size)
                            7 -> api.getPrices("FNMIX", Interval.WEEKLY, size)
                            8 -> api.getPrices("FNMIX", Interval.MONTHLY, size)

                            9 -> api.getPrices("AAPL", Interval.DAILY, size)
                            10 -> api.getPrices("AAPL", Interval.WEEKLY, size)
                            11 -> api.getPrices("AAPL", Interval.MONTHLY, size)

                            /*
                            12 -> {
                                val date = GregorianCalendar(2012, 2, 1).getTime();
                                list = PriceList ("SPY", api.getPrices("SPY", Interval.DAILY, date));
                            }
                            */

                            else -> null
                        }

                if (i < 12) {
                    if (list == null || list.size != size)
                        throw Exception("priceHistory failed at i = " + i);
                }

                Thread.sleep(500);
            }

            /*
            PriceList L =
            L = api.getPrices("OHI", Interval.WEEKLY, 100);
            L = api.getPrices("OHI", Interval.MONTHLY, 100);

            List<Dividend> divs = api.getDividends("OHI");

            System.out.println("Success = " + L.size());
            */
        }
    }


}