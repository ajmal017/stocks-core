package org.cerion.stocklist.model;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.web.YahooFinance;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class PositionValueTest {

    @Test
    public void priceChange() {
        final float shares = 10.123f;

        Position pos = new Position("SPY", shares, 2276.98, getDate(30));
        PositionValue position = new PositionValue(pos, null);
        Quote q = new Quote("SPY");
        q.lastTrade = 2352.95f;
        q.change = 1.1f;
        q.changePercent = 2.2f;
        position.setQuote(q);

        // Check dividend values before adding them
        assertEquals(0, position.getDividendProfit(),0.005);
        position.addDividends(getDividends(1,2,3));

        // Check
        assertEquals(75.97, position.getChange(), 0.005);
        assertEquals(0.0334, position.getPercentChanged(), 0.00005);
        assertEquals(shares, position.getCurrCount(),0);
        assertEquals(2352.95, position.getCurrPrice(),0.005);
        assertEquals(2352.95 * shares, position.getCurrValue(),0.005);

        assertEquals(769.05, position.getProfit(),0.005);
        assertEquals(10.123 * 6, position.getDividendProfit(),0.005);
        assertEquals(0.0360, position.getPercentChangedWithDividends(),0.00005);
    }

    @Test
    public void priceChange_diviendsReinvested_FRIFX() {
        // Actual numbers from website
        final float lastPrice = 12.13f;
        final float currValue = 4154.87f;
        final float quantity = 342.529f;
        final double purchasePrice = 4090.9401f; // Issues representing this in double, should really be 4094.94000000000
        final double profit = 63.95;

        Position pos = new Position("FRIFX", 341.481, 11.98, new GregorianCalendar(2017, 2, 9).getTime(), true);
        Quote q = new Quote("FRIFX");
        q.lastTrade = 12.13f;

        YahooFinance api = YahooFinance.getInstance();
        List<Price> prices = new ArrayList<>();
        prices.add(new Price(new GregorianCalendar(2017, 2, 8).getTime(), 1f, 1f, 1f, 1f, 10000000.0f));
        prices.add(new Price(new GregorianCalendar(2017, 2, 9).getTime(), 11.99f, 11.99f, 11.90f, 11.943306f, 10000000.0f));
        prices.add(new Price(new GregorianCalendar(2017, 2, 10).getTime(), 2f, 2f, 2f, 2f, 10000000.0f));
        PriceList list = new PriceList("FRIFX",prices);

        PositionValue position = new PositionValue(pos, list);
        position.setQuote(q);

        // Gains
        assertEquals(0.0156316852 , position.getPercentChanged(),0.000005);
        assertEquals(profit, position.getProfit(), 0.005);
        assertEquals(quantity, position.getCurrCount(),0.0005);
        assertEquals(currValue, position.getCurrValue(),0.005);

        // Dividend
        assertEquals(0.0, position.getDividendProfit(),0.005);
        assertEquals(position.getPercentChanged(), position.getPercentChangedWithDividends(),0.005);
    }

    @Test
    public void priceChange_diviendsReinvested_somethingelse() {
        // TODO get another one from the start with additional values like Total gain/loss columns
    }

    @Test
    public void dividendHistory() {
        Position pos = new Position("SPY", 10, 123.45, getDate(30));
        PositionValue position = new PositionValue(pos, null);
        Quote q = new Quote("SPY");
        q.lastTrade = 123.45f;
        position.setQuote(q);
        position.addDividends(getDividends(9.1f,2.2f,3.56f));

        assertEquals(0, position.getProfit(),0.005);
        assertEquals(10 * (9.1+2.2+3.56), position.getDividendProfit(),0.005);

        double currentValue = position.getDividendProfit() + position.getCurrValue();
        double origValue = pos.getOrigValue();
        double change = (currentValue - origValue) / origValue;

        assertEquals(change, position.getPercentChangedWithDividends(),0.005);
    }

    @Test
    public void stockSplit() {
        // TODO test for stock split, most likely will not work right
    }

    private List<Dividend> getDividends(float ...values) {
        Calendar calendar = Calendar.getInstance();
        List<Dividend> result = new ArrayList<>();

        for(float v : values) {
            Dividend d = new Dividend(calendar.getTime(), v);
            result.add(d);

            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        return result;
    }

    private Date getDate(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -daysAgo);
        return calendar.getTime();
    }

}