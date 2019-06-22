package org.cerion.stocklist;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.model.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
			Collections.sort(list);
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

			mDate[i] = p.date;
			mOpen.mVal[i] = p.open;
			mHigh.mVal[i] = p.high;
			mLow.mVal[i] = p.low;
			mClose.mVal[i] = p.close;
			mVolume.mVal[i] = p.volume;

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
			logPrices.add(new Price(p.date, (float)Math.log(p.open), (float)Math.log(p.high), (float)Math.log(p.low), (float)Math.log(p.close), (float)Math.log(p.volume)));
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

			float open = start.open;
			float close = start.close;
			float high = start.high;
			float low = start.low;
			float volume = start.volume;

			while(i < size() - 1) {
				i++;
				Price p = get(i);

				long t1 = get(i-1).date.getTime();
				long t2 = p.date.getTime();
				long diff = (t2 - t1);
				diff/= (1000 * 60 * 60 * 24);

				// New week
				if (diff > 2)
					break;

				volume += p.volume;
				if(p.high > high)
					high = p.high;
				if(p.low < low)
					low = p.low;

				close = p.close;
			}

			Price p = new Price(start.date, open, high, low, close, volume);
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

			Price p = new Price(p1.date,
					p3.open,
					Math.max(Math.max(p1.high, p2.high),p3.high),
					Math.min(Math.min(p1.low, p2.low),p3.low),
					p1.close,
					p1.volume + p2.volume + p3.volume);

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

			float open = start.open;
			float close = get(i).close;
			float high = 0;
			float low = open;
			float volume = 0;

			for(int j = i-11; j <= i; j++) {
				Price q = get(j);
				volume += q.volume;
				if(q.high > high)
					high = q.high;
				if(q.low < low)
					low = q.low;
			}

			Price p = new Price(get(i).date, open, high, low, close, volume);
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

