package org.cerion.stocklist;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.model.Interval;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class PriceList extends ArrayList<Price>
{
	public String mSymbol;
	private boolean mLogScale = false;

	public java.util.Date[] mDate;
	public FloatArray mOpen;
	public FloatArray mHigh;
	public FloatArray mLow;
	public FloatArray mClose;
	public FloatArray mVolume;
	
	public float high(int pos) { return mHigh.get(pos); }
	public float low(int pos) { return mLow.get(pos); }
	public float close(int pos) { return mClose.get(pos); }
	public float volume(int pos) { return mVolume.get(pos); }
	
	public Date[] getDates() { return mDate; }
	public FloatArray getOpen() { return mOpen; }
	public FloatArray getClose() { return mClose; }
	public FloatArray getHigh() { return mHigh; }
	public FloatArray getLow() { return mLow; }
	public FloatArray getVolume() { return mVolume; }

	public PriceList(String symbol, List<Price> list) {

		try {
			list.sort(Comparator.comparing(Price::getDate));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		mSymbol = symbol;
		int size = list.size();

		mDate = new java.util.Date[size];
		mOpen = new FloatArray(size);
		mHigh = new FloatArray(size);
		mLow = new FloatArray(size);
		mClose = new FloatArray(size);
		mVolume = new FloatArray(size);
		//mAdjCloses = new float[size];

		for(int i = 0; i < size; i++) {
			Price p = list.get(i);
			p.pos = i;
			p.parent = this;

			mDate[i] = p.getDate();
			mOpen.mVal[i] = p.getOpen();
			mHigh.mVal[i] = p.getHigh();
			mLow.mVal[i] = p.getLow();
			mClose.mVal[i] = p.getClose();
			mVolume.mVal[i] = p.getVolume();

			this.add(p);
		}
	}

	//Values derived from current days prices
	public float tp(int pos) { return (mClose.get(pos) + mHigh.get(pos) + mLow.get(pos)) / 3; } //Typical price
	public float mfv(int pos) //Money flow volume
	{
		float mult = ((close(pos)  -  low(pos)) - (high(pos) - close(pos))) /(high(pos) - low(pos));
		if(close(pos) == low(pos))
			mult = -1;
		if(low(pos) == high(pos)) //divide by zero
			mult = 0;
		
		return mult * volume(pos);
	} 
	
	//Rate of change
	public float roc(int pos, int period)
	{
		int x = 0; //If period goes beyond start then set to first element
		if(pos >= period)
			x = pos-period;
		
		return ((close(pos) - close(x)) * 100) / close(x);
	}

	public PriceList toLogScale() {
		if(mLogScale)
			return this;

		List<Price> logPrices = new ArrayList<>();
		for(int i = 0; i < size(); i++) {
			Price p = get(i);
			logPrices.add(new Price(p.getDate(), (float)Math.log(p.getOpen()), (float)Math.log(p.getHigh()), (float)Math.log(p.getLow()), (float)Math.log(p.getClose()), (float)Math.log(p.getVolume())));
		}

		return new PriceList(mSymbol, logPrices);
	}

	public boolean is(String symbol) {
		return mSymbol.contentEquals(symbol);
	}
	
	public Interval getInterval() {
		if(size() > 1) {
			// Skip first instance
			// the first month of a fund may only have a few days worth of arrays depending on its first trading date, example SPY
			long diff = mDate[2].getTime() - mDate[1].getTime();
			diff /= 1000 * 60 * 60 * 24; //days

			if(diff > 200)
				return Interval.YEARLY;
			if(diff > 45)
				return Interval.QUARTERLY;
			else if(diff > 10)
				return Interval.MONTHLY;
			else if(diff > 5)
				return Interval.WEEKLY;
		}
		
		return Interval.DAILY;
	}

	public PriceList toWeekly() {
		if(getInterval() != Interval.DAILY)
			throw new RuntimeException("Interval must be daily");

		List<Price> prices = new ArrayList<>();

		int i = 0;
		while(i < size() - 1) {
			Price start = get(i);

			float open = start.getOpen();
			float close = start.getClose();
			float high = start.getHigh();
			float low = start.getLow();
			float volume = start.getVolume();

			while(i < size() - 1) {
				i++;
				Price p = get(i);

				long t1 = get(i-1).getDate().getTime();
				long t2 = p.getDate().getTime();
				long diff = (t2 - t1);
				diff/= (1000 * 60 * 60 * 24);

				// New week
				if (diff > 2)
					break;

				volume += p.getVolume();
				if(p.getHigh() > high)
					high = p.getHigh();
				if(p.getLow() < low)
					low = p.getLow();

				close = p.getClose();
			}

			Price p = new Price(start.getDate(), open, high, low, close, volume);
			prices.add(p);
		}

		return new PriceList(mSymbol, prices);
	}

	public PriceList toQuarterly() {
		if(getInterval() != Interval.MONTHLY)
			throw new RuntimeException("Interval must be monthly");

		List<Price> prices = new ArrayList<>();
		for(int i = size() - 1; i >= 2; i-= 3) {
			Price p1 = get(i);
			Price p2 = get(i-1);
			Price p3 = get(i-2);

			Price p = new Price(p1.getDate(),
					p3.getOpen(),
					Math.max(Math.max(p1.getHigh(), p2.getHigh()),p3.getHigh()),
					Math.min(Math.min(p1.getLow(), p2.getLow()),p3.getLow()),
					p1.getClose(),
					p1.getVolume() + p2.getVolume() + p3.getVolume());

			prices.add(p);
		}

		return new PriceList(mSymbol, prices);
	}

	public PriceList toYearly() {
		if(getInterval() != Interval.MONTHLY)
			throw new RuntimeException("Interval must be monthly");

		List<Price> prices = new ArrayList<>();
		for(int i = size() - 1; i >= 11; i-= 12) {
			Price start = get(i-11);

			float open = start.getOpen();
			float close = get(i).getClose();
			float high = 0;
			float low = open;
			float volume = 0;

			for(int j = i-11; j <= i; j++) {
				Price q = get(j);
				volume += q.getVolume();
				if(q.getHigh() > high)
					high = q.getHigh();
				if(q.getLow() < low)
					low = q.getLow();
			}

			Price p = new Price(get(i).getDate(), open, high, low, close, volume);
			prices.add(p);
		}

		return new PriceList(mSymbol, prices);
	}

	public Price getLast(int prev) {
		return get(size()-1-prev);
	}
	public Price getLast() {
		return getLast(0);
	}
	
    public float tr(int pos)
    {
        if(pos > 0)
            return Math.max(mHigh.get(pos),mClose.get(pos-1)) - Math.min(mLow.get(pos),mClose.get(pos-1));
        return mHigh.get(0) - mLow.get(0);
    } 
    
    public float slope(int period, int pos) {
		return this.mClose.slope(period, pos);
    }

    public float averageYearlyGain() {
		float count = size() - 1;
		float years = (float)count / pricesPerYear();

		// Simple Return = (Current Price-Purchase Price) / Purchase Price
		// Annual Return = (Simple Return +1) ^ (1 / Years Held)-1

		float simpleReturn = getLast().getPercentDiff(get(0));
		double a = (simpleReturn / 100) + 1;
		double b = (1 / years);

		double annualReturn = Math.pow(a, b) - 1;
		return (float)annualReturn;
	}

	public float getChange() {
		return getClose().get(size()-1) - getClose().get(size()-2);
	}

	public float getPercentChange() {
		return getClose().getPercentChange(size()-2);
	}

	private int pricesPerYear() {
		Interval interval = getInterval();
		switch(interval) {
			case DAILY: return 252;
			case WEEKLY: return 52;
			default: return 12;
		}
	}
}

