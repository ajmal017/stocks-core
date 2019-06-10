package org.cerion.stocklist.model;

import java.util.*;

public class DividendHistory {

    private List<Dividend> list;
    private float totalDividends;
    private Date nextDividendEstimate;
    private Date lastDividendDate;
    private double lastDividendAmount;

    public DividendHistory(List<Dividend> list, Date startDate) {
        this.list = list;

        totalDividends = 0;
        Dividend secondToLast = null;

        if(list == null || list.size() == 0)
            return;

        //Sort in descending order so most recent dividend is first
        Collections.sort(list, new Comparator<Dividend>() {
            @Override
            public int compare(Dividend lhs, Dividend rhs) {
                return rhs.mDate.compareTo(lhs.mDate);
            }
        });

        // Get total dividend amount
        for(Dividend d : list) {
            if(d.mDate.after(startDate)) {
                totalDividends += d.mDividend;
            } else
                break;
        }

        Dividend lastDividend = list.get(0);
        this.lastDividendAmount = lastDividend.mDividend;
        this.lastDividendDate = lastDividend.mDate;


        // Get 2nd to last dividend
        for(Dividend d : list) {
            if(d.mDate.before(lastDividend.mDate)) {
                if(secondToLast == null || d.mDate.after(secondToLast.mDate))
                    secondToLast = d;
            }
        }

        // Get difference
        if(secondToLast != null) {
            long diff = lastDividend.mDate.getTime() - secondToLast.mDate.getTime();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(lastDividend.mDate.getTime() + diff);

            nextDividendEstimate = cal.getTime();
        }
    }


    /**
     * Amount of last dividend in dollars issued per share
     * @return last dividend value
     */
    public double getLastDividend() {
        return lastDividendAmount;
    }

    public double getTotalDividends() {
        return totalDividends;
    }

    /**
     * Gets the date of last dividend
     * @return Date, or null if no dividends
     */
    public Date getLastDividendDate() {
        return lastDividendDate;
    }

    /**
     * Gets the estimated date of the next dividend based on last 2 dividends
     * @return estimated Date
     */
    public Date getNextDividendEstimate() {
        return nextDividendEstimate;
    }

}
