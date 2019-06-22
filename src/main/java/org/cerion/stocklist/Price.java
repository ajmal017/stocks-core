package org.cerion.stocklist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.cerion.stocklist.arrays.BandArray;

public class Price implements Comparable<Price>
{
	//Core fields
	public java.util.Date date;
	public float open;
	public float high;
	public float low;
	public float volume;
	public float close;
	
	//Fields used in list
	public PriceList parent;
	public int pos;

	private static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public String getDate() { return mDateFormat.format(date); } //When it needs to be formatted properly
	public static String getDecimal(float val)
	{
		return String.format("%.2f",val);
	}

	@Deprecated
	public Price(java.util.Date date, float open, float high, float low, float close, long volume)
	{
		this.date = date;
		this.open = open;
		this.high = high;
		this.low  = low;
		this.volume = volume;
		this.close = close;
		
		//Error checking
		if(open < low || close < low || open > high || close > high)
			throw new RuntimeException("Price range inconsistency " + String.format("%s,%f,%f,%f,%f", getDate(), open, high, low, close));
	}

	public Price(java.util.Date date, float open, float high, float low, float close, float volume) {
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.close = close;

		//Error checking
		if(open < low || close < low || open > high || close > high)
			throw new RuntimeException("Price range inconsistency " + String.format("%s,%f,%f,%f,%f", getDate(), open, high, low, close));
	}
	
	public int getDOW() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	@Deprecated
	public float avgGain() {
		return avgGain(1);
	}

	@Deprecated
	public float avgGain(int days) {
		float gain = 0;
		int end = pos + days;
		
		for(int i = pos+1; i <= end; i++)
		{
			Price t = parent.get(i);
			gain += t.getPercentDiff(this);
		}
		
		gain /= days;	
		
		if(gain > 1000)
			gain = 10; //Bug in PLL
		return gain;
	}

	public float slope(int period) { return parent.slope(period, pos); } //Slope of closing price
	public float tp() { return parent.tp(pos); } //Typical price
	public float change(Price prev)
	{
		return getPercentDiff(prev);
	}
	public float getPercentDiff(Price old)
	{
		if(old.date.before(date) == false)
			throw new RuntimeException("current price is older than input price");
		
		float diff = close - old.close;
		float percent = (100 * (diff / old.close));
		return percent;
	}

	@Override
	public int compareTo(Price p) {
		//Sort by date ascending, oldest first
		return this.date.compareTo(p.date);
	}
}
