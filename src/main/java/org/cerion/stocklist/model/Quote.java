package org.cerion.stocklist.model;

import java.util.Date;

public class Quote 
{
	public String symbol;
	public String name;
	public String exchange;
	public float prevClose;

	// Current Day
	public float open;
	public float low;
	public float high;
	public float lastTrade; //Most current price, same as close after hours
	public float change; // Change since previous date (lastTrade - prevClose)?
	public float changePercent;
	public long volume;
	public Date lastTradeDate;

	public long averageVolume;
	public float oneYearTarget; // Average analysts estimate of price in 1 year
	public float high52;
	public float low52;

	public float eps;
	//public float epsEstCurrentYear;
	//public float epsEstNextYear;
	//public float epsEstNextQuarter;

	public float bookValue;
	public String ebitda; // Earnings before interest, tax, depreciation and amortization
	public float priceSalesRatio;
	public float priceBookRatio;
	public float peRatio;
	public float pegRatio;
	//public float priceEPSEstCurrentYear;
	//public float priceEPSEstNextYear;
	public float shortRatio;

	public float sma50;
	public float sma200;

	public float dividendYield;
	public Date dividendDate;
	public float dividendsPerShare;

	public String marketCap;
	public long sharesTotal;
	public long sharesFloat;
	public String revenue;

	// Other
	public float beta;
	public String sector;

	// Possibly useful omitted ones that can be calculated from the others
	// Change from 52 week low/high
	// %Change from 52 week low/high
	// Change and percent from sma50/200

	public Quote(String symbol) {
		this.symbol = symbol;
	}

	public boolean validate() {
		if(name.contentEquals("N/A") || prevClose == 0)
			return false;

		return true;
	}

	public float getCurrentPrice() {
		return lastTrade;
	}
}
