package org.cerion.stocklist.web;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.model.Dividend;
import org.cerion.stocklist.model.HistoricalDates;
import org.cerion.stocklist.model.Interval;
import org.cerion.stocklist.model.Quote;
import org.cerion.stocklist.model.Symbol;
import org.cerion.stocklist.repository.DividendRepository;
import org.cerion.stocklist.repository.PriceListRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RepositoryCachedAPI implements CachedDataAPI {

    private DataAPI mAPI;
    private PriceListRepository mPriceRepo;
    private DividendRepository mDividendRepo;

    public RepositoryCachedAPI(DataAPI api, PriceListRepository priceRepo, DividendRepository dividendRepo) {
        mAPI = api;
        mPriceRepo = priceRepo;
        mDividendRepo = dividendRepo;
    }

    @Override
    public void clearCache() {
        mPriceRepo.deleteAll();
        mDividendRepo.deleteAll();
    }

    @Override
    public PriceList getPrices(String symbol, Interval interval, int max) {
        throw new UnsupportedOperationException();
        /*
        HistoricalDates dates = mPriceRepo.getHistoricalDates(symbol, interval);
        boolean update = false;
        Date retrieveFrom = null;

        if(dates == null) {
            update = true;
        } else {
            Date now = new Date();
            long diff = now.getTime() - dates.LastUpdated.getTime();
            diff /= 1000 * 60 * 60;
            long hours = diff;
            long days = diff / 24;

            System.out.println(symbol + " " + interval.name() + " last updated " + dates.LastUpdated + " (" + diff + " days ago)");

            // TODO, smarter updates based on last price obtained and weekends
            if(interval == Interval.DAILY && hours >= 12)
                update = true;
            else if(interval == Interval.WEEKLY && days > 3)
                update = true;
            else if(interval == Interval.MONTHLY && days > 7)
                update = true;

            // Incremental update, not sure if all this is necessary but start a few data points earlier to be safe
            if (update) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dates.LastDate);
                switch(interval)
                {
                    case DAILY: cal.add(Calendar.DAY_OF_MONTH, -7); break;
                    case WEEKLY: cal.add(Calendar.DAY_OF_MONTH, -14); break;
                    case MONTHLY: cal.add(Calendar.DAY_OF_MONTH, -90); break;
                }

                retrieveFrom = cal.getTime();
            }
        }

        try {
            if (retrieveFrom != null) {
                updatePricesIncremental(symbol, interval, retrieveFrom);
            } else if (update)
                updatePrices(symbol, interval, max); // TODO remove limit here

        } catch(Exception e) {
            return null;
        }

        return mPriceRepo.get(symbol, interval, max);
        */
    }

    @Override
    public List<Price> getPrices(String symbol, Interval interval, Date start, boolean forceUpdate) throws Exception {

        HistoricalDates dates = mPriceRepo.getHistoricalDates(symbol, interval);
        boolean update = forceUpdate;
        Date retrieveFrom = null;

        if(dates == null) {
            update = true;
        } else {
            Date now = new Date();
            long diff = now.getTime() - dates.LastUpdated.getTime();
            diff /= 1000 * 60 * 60;
            long hours = diff;
            long days = diff / 24;

            System.out.println(symbol + " " + interval.name() + " last updated " + dates.LastUpdated + " (" + diff + " days ago)");

            // TODO, smarter updates based on last price obtained and weekends
            if(interval == Interval.DAILY && hours >= 12)
                update = true;
            else if(interval == Interval.WEEKLY && days > 3)
                update = true;
            else if(interval == Interval.MONTHLY && days > 7)
                update = true;


            // Incremental update, not sure if all this is necessary but start a few data points earlier to be safe
            if (update) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dates.LastDate);
                switch(interval)
                {
                    case DAILY: cal.add(Calendar.DAY_OF_MONTH, -1); break;
                    case WEEKLY: cal.add(Calendar.DAY_OF_MONTH, -7); break;
                    case MONTHLY: cal.add(Calendar.DAY_OF_MONTH, -31); break;
                }

                retrieveFrom = cal.getTime();
            }
        }

        try {
            if (retrieveFrom != null) {
                updatePricesIncremental(symbol, interval, start, retrieveFrom);
            } else if (update)
                updatePrices(symbol, interval, start);

        } catch(Exception e) {
            return null;
        }

        return mPriceRepo.get(symbol, interval);
    }

    @Override
    public List<Price> getPrices(String symbol, Interval interval, Date start) throws Exception {
        return getPrices(symbol, interval, start, false);
    }

    @Override
    public List<Dividend> getDividends(String symbol) {
        HistoricalDates dates = mDividendRepo.getHistoricalDates(symbol);
        boolean update = false;

        if(dates == null) {
            update = true;
        } else {
            Date now = new Date();
            long diff = now.getTime() - dates.LastUpdated.getTime();
            diff /= 1000 * 60 * 60 * 24;
            //Log.d(TAG, symbol + " " + interval.name() + " last updated " + dates.LastUpdated + " (" + diff + " days ago)");

            // TODO based it on the following
            // IF most recent dividend is less than 30 days old, check at most once a week
            // IF no dividends, check once a month, probably wont ever be any
            // If new dividend expected soon, check daily
            if(diff > 7)
                update = true;
        }

        if(update) {
            // TODO API should fail if it doesn't get a valid response, difference between error and success
            List<Dividend> dividends = mAPI.getDividends(symbol);
            mDividendRepo.add(symbol, dividends); //updatePrices(symbol, interval);
        }

        return mDividendRepo.get(symbol);
    }

    @Override
    public List<Symbol> getSymbols(Set<String> symbols) {
        return mAPI.getSymbols(symbols);
    }

    @Override
    public Symbol getSymbol(String symbol) {
        return mAPI.getSymbol(symbol);
    }

    @Override
    public Map<String, Quote> getQuotes(Set<String> symbols) {
        return mAPI.getQuotes(symbols);
    }

    @Override
    public Quote getQuote(String symbol) {
        return mAPI.getQuote(symbol);
    }

    private void updatePricesIncremental(String symbol, Interval interval, Date firstDate, Date startFrom) throws Exception {
        System.out.println("Incremental update starting from " + startFrom.toString());

        List<Price> newPrices = mAPI.getPrices(symbol, interval, startFrom);
        if (newPrices == null || newPrices.size() == 0)
            throw new Exception("Failed to get updated prices for " + symbol);

        // TODO add test so priceRepo must return date sorted prices or throw exception
        // Check if existing prices matches at start
        boolean merge = false;
        Price first = newPrices.get(0);
        List<Price> currPrices = mPriceRepo.get(symbol, interval);
        for(Price p : currPrices) {
            if (p.date.equals(first.date)) {
                if (p.close == first.close &&
                        p.volume == first.volume &&
                        p.open == first.open &&
                        p.high == first.high &&
                        p.low == first.low)
                    merge = true;
            }
        }

        if (merge) {
            List<Price> mergedList = new ArrayList<>();
            for(Price p : currPrices) {
                if (p.date.equals(first.date)) {
                    mergedList.addAll(newPrices);
                    break;
                }

                mergedList.add(p);
            }

            PriceList list = new PriceList(symbol, mergedList);
            mPriceRepo.add(list);
            int diff = mergedList.size() - currPrices.size();
            System.out.println("Updated prices for " + symbol + ", added " + diff);

        } else {
            // Possible split or dividend
            System.out.println("Unable to merge, prices do not match");
            updatePrices(symbol, interval, firstDate);
        }
    }

    private void updatePrices(String symbol, Interval interval, Date startDate) throws Exception {
        PriceList list = null;
        try
        {
            Calendar cal = Calendar.getInstance();
            cal.set(1990, Calendar.JANUARY, 1);

            List<Price> prices = mAPI.getPrices(symbol, interval, startDate);
            list = new PriceList(symbol, prices);
        }
        catch(Exception e)
        {
            // nothing
        }

        if (list != null && list.size() > 0) {
            mPriceRepo.add(list);
            System.out.println("Updated prices for " + symbol);
        }
        else {
            throw new Exception("Failed to get updated prices for " + symbol);
        }
    }
}
