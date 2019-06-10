package org.cerion.stocklist.model;

import java.util.Date;

public class Position {

    private int id = 0;
    private int accountId = 0;

    // Required fields
    private final double count;
    private final String symbol;
    private final double origPrice;
    private final Date date;
    private final boolean dividendsReinvested;

    public Position(String symbol, double count, double price, Date date) {
        this(symbol, count, price, date, false);
    }

    public Position(String symbol, double count, double price, Date date, boolean dividendsReinvested) {
        this.symbol = symbol;
        this.count = count;
        this.origPrice = price;
        this.date = date;
        this.dividendsReinvested = dividendsReinvested;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCount() {
        return count;
    }

    public double getOrigPrice() {
        return origPrice;
    }

    public Date getDate() {
        return date;
    }

    public boolean IsDividendsReinvested() {
        return dividendsReinvested;
    }

    /**
     * Original value/cost in dollars of all shares
     * @return cost of original lot
     */
    public double getOrigValue() {
        int value = (int)(origPrice * count * 100); // Value is truncated, not rounded
        return value / 100.0;
    }

    @Override
    public String toString() {
        return symbol + " " + count + "@" + origPrice;
    }
}
