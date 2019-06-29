package org.cerion.stocklist.model;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO rename this to just Position after existing class can be renamed to something better (PurchaseLot maybe)
public class PositionValue {

    private final Position purchase;
    private final Date date;
    private final double origPrice;

    private Quote quote;
    private DividendHistory dividendHistory;
    private final PriceList priceList;
    private double origPriceAdjusted;
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");

    public PositionValue(Position purchase, PriceList priceHistory) {
        this.purchase = purchase;
        this.date = purchase.getDate();
        this.origPrice = purchase.getOrigPrice();

        this.priceList = priceHistory;
        if (priceHistory != null)
            setPriceHistory(priceHistory);
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public DividendHistory getDividendHistory() {
        return dividendHistory;
    }

    /**
     * Add dividends earned to the value of this position
     * @param list Historical list of dividends
     */
    public void addDividends(List<Dividend> list) {
        this.dividendHistory = new DividendHistory(list, date);
    }

    private void setPriceHistory(PriceList list) {
        if(list.getInterval() == Interval.DAILY) {
            if(IsDividendsReinvested()) {
                for(int i = 0; i < list.size(); i++) {
                    Date date = list.mDate[i];
                    // If entered today then currrent date may not be present so just use last entry
                    // TODO add unit test to check this value
                    if(dateEquals(date,this.date) || (origPriceAdjusted == 0 && i == list.size() -1)) {
                        origPriceAdjusted = list.close(i);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("daily prices required");
        }
    }

    public double getCurrPrice() {
        if (quote != null)
            return quote.lastTrade;
        if (priceList != null)
            return priceList.getLast().getClose();

        return 0;
    }

    /**
     * Gets the difference between current price and purchase price
     * @return price difference
     */
    public double getChange() {
        if(IsDividendsReinvested()) {
            double percent = getPercentChanged();
            return origPrice * percent;
        }

        return getCurrPrice() - origPrice;
    }

    /**
     * Gets percent difference between current and original/purchase price
     * @return percent difference
     */
    public double getPercentChanged() {
        if(IsDividendsReinvested()) {
            return (getCurrPrice() - origPriceAdjusted) / origPriceAdjusted;
        }

        return (getCurrPrice() - origPrice) / origPrice;
    }

    /**
     * Gets percent difference between current/original price including dividends
     * @return percent difference
     */
    public double getPercentChangedWithDividends() {
        double origValue = getOrigValue();
        double currValueDiv = getCurrValue() + getDividendProfit();
        return (currValueDiv - origValue) / origValue;
    }

    /*
    public double getOneDayPercentChange() {
        if (quote != null)
            return quote.changePercent;
        if (priceList != null) {
            int last = priceList.size() - 1;
            return 100 * (priceList.close(last) - priceList.close(last-1)) / priceList.close(last-1);
        }

        return 0;
    }

    public double getOneDayChange() {
        if (quote != null)
            return quote.change;
        if (priceList != null) {
            int last = priceList.size() - 1;
            return priceList.close(last) - priceList.close(last-1);
        }

        return 0;
    }
    */

    /**
     * Gets total dividends earned in dollars
     * @return amount earned
     */
    public double getDividendProfit() {
        if(IsDividendsReinvested())
            return 0;
        if (dividendHistory == null)
            return 0;

        return dividendHistory.getTotalDividends() * purchase.getCount();
    }

    /**
     * Get price change between original and current price in dollars
     * @return profit in dollars
     */
    public double getProfit() {
        if(IsDividendsReinvested()) {
            return getOrigValue() * (getPercentChanged());
        }
        return getCurrValue() - getOrigValue();
    }


    /**
     * Current value/cost in dollars of all shares
     * @return cost of current lot
     */
    public double getCurrValue() {
        if(IsDividendsReinvested()) {
            int value = (int)(getCurrPrice() * getCurrCount() * 100);
            return value / 100.0;
            // return getOrigValue() + getProfit(); /// TODO should be truncated not rounded
        }

        return getCurrPrice() * purchase.getCount();
    }

    public List<Double> getValueHistory() {
        if (priceList == null)
            throw new RuntimeException("price history must be set");

        int start = 0;
        for (Price p : priceList) {
            if (this.dateEquals(p.getDate(), date)) {
                start = p.pos;
                break;
            }
        }

        List<Double> result = new ArrayList<>();
        for(int i = start; i < priceList.size() - 1; i++) {
            result.add(getOrigValue());
        }

        result.add(getCurrValue());
        return result;
    }

    /**
     * Gets the current count, same as count unless dividends reinvested
     * @return Quantity of shares
     */
    public double getCurrCount() {
        if(IsDividendsReinvested()) {
            double orig = getOrigValue();
            double profit = getProfit();
            double currValue = getOrigValue() + getProfit();
            double shares = currValue / getCurrPrice();
            int iShares = (int)(shares * 1000);
            shares = iShares / 1000.0;

            return shares;
        }

        return purchase.getCount();
    }

    private boolean IsDividendsReinvested() {
        return purchase.IsDividendsReinvested();
    }

    private boolean dateEquals(Date d1, Date d2) {
        return dayFormat.format(d1).equals(dayFormat.format(d2));
    }

    private double getOrigValue() {
        return purchase.getOrigValue();
    }
}
