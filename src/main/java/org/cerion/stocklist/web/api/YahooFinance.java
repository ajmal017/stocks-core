package org.cerion.stocklist.web.api;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.model.Dividend;
import org.cerion.stocklist.model.Interval;
import org.cerion.stocklist.model.Quote;
import org.cerion.stocklist.model.Symbol;
import org.cerion.stocklist.web.DataAPI;
import org.cerion.stocklist.web.Response;
import org.cerion.stocklist.web.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class YahooFinance {

	private static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat mDateFormatAlt = new SimpleDateFormat("M/dd/yyyy");

	private static YahooFinance mInstance = new YahooFinance();

	private static final boolean DEBUG = true;
	private String mCookieCrumb;
	private String mCookie;

	private YahooFinance() {

	}

	public static YahooFinance getInstance() {
		return mInstance;
	}

	/***
	 * Gets PriceList from csv formatted file that API would return
	 * @param tableData file contents as string
	 * @return PriceList
	 */
	public static List<Price> getPricesFromTable(String tableData) {
		String lines[] = tableData.split("\\r\\n");

		if (DEBUG)
			System.out.println("Table lines = " + lines.length);
	
		ArrayList<Price> prices = new ArrayList<>();
		for(int i = 1; i < lines.length; i++) {
			if (!lines[i].contains("null"))
				prices.add(parseLine(lines[i]));
			else
				System.out.println("Ignoring line " + lines[i]); // new API issue some rows have all null values
		}
		
		return prices;
	}

	public PriceList getPrices(String symbol, Interval interval, int count) {
		Calendar start = getCalendar(interval, count);

		List<Price> prices = getPrices(symbol, interval, start.getTime());
		while(prices.size() > count)
			prices.remove(0);

		PriceList result = new PriceList(symbol,prices);

		// Should never happen
		if(result.size() > 0 && result.getInterval() != interval)
			throw new RuntimeException("unexpected interval " + result.getInterval());

		System.out.println("Result lines= " + result.size());
		return result;
	}

	public List<Price> getPrices(String symbol, Interval interval, Date start) {
		if (!setCookieCrumb())
			throw new RuntimeException("Failed to get cookie");

		//https://query1.finance.yahoo.com/v7/finance/download/OHI
		// ?period1=1493915553
		// &period2=1496593953
		// &interval=1d
		// &events=history
		// &crumb=TSV3DSdPIjI

		String sURL = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol;
		sURL += "?period1=" + (start.getTime() / 1000);
		sURL += "&period2=" + (new Date().getTime() / 1000);

		if(interval == Interval.MONTHLY)
			sURL += "&interval=1mo";
		else if(interval == Interval.WEEKLY)
			sURL += "&interval=1wk";
		else
			sURL += "&interval=1d";

		sURL += "&events=history";
		sURL += "&crumb=" + mCookieCrumb;

		System.out.println(sURL);
		Response res = Tools.getURL(sURL, mCookie);
		if (DEBUG) {
			System.out.println("Response size = " + res.result.length());
			if (res.result.length() < 1000)
				System.out.println(res.result);
		}

		String sData = res.result;
		return getPricesFromTable(sData);
	}

	public List<Dividend> getDividends(String symbol) {
		if (!setCookieCrumb())
			throw new RuntimeException("Failed to get cookie");

		String sURL = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol;
		sURL += "?period1=946684800"; // Jan 1, 2000
		sURL += "&period2=" + (new Date().getTime() / 1000);
		sURL += "&interval=1wk&events=div";
		sURL += "&crumb=" + mCookieCrumb;

		List<Dividend> result = new ArrayList<>();
		Response res = Tools.getURL(sURL, mCookie);

		String sData = res.result;
		String lines[] = sData.split("\\r\\n");
		for(int i = 1; i < lines.length; i++)
		{
			String line[] = lines[i].split(",");
			
			Dividend div = new Dividend(parseDate(line[0]), Float.parseFloat(line[1]) );
			result.add(0, div);
		}

		return result;
	}
	
	// http://www.jarloo.com/yahoo_finance/
	private static final String[] FLAGS = {
			//Pricing/Date
			"p", "o", "c1", "p2", "g", "h", "l1", "t8", "k", "j", "v", "a2", "d1",

			// Ratios and financial info
			"e", "e7", "e8", "e9",
			"b4", "j4", "p5", "p6", "r", "r5", "r6", "r7", "s7",

			// Averages
			"m3", "m4",

			// Misc info
			"j1", "f6", "j2", "n", "s", "x", "s6",

			// Dividends
			"y", "d", "q"
	};

	/*
	@Override
	public Map<String,Quote> getQuotes(Set<String> symbols) {
		String flags = "";
		for(String s : FLAGS)
			flags += s;

		// String.join isn't working on android with a Set, possibly java v7/8 issue
		String symbols_join = "";
		for(String s : symbols) {
			if(symbols_join.length() > 0)
				symbols_join += "+";
			symbols_join += s;
		}

		String sResult = Tools.getURL("https://download.finance.yahoo.com/d/quotes.csv?s=" + symbols_join + "&f=" + flags + "&e=.csv");
		String lines[] = sResult.split("\r\n");

		Map<String,Quote> result = new HashMap<>();

		int i = 0;
		for(String symbol : symbols) {
			String line = lines[i];
			if(line.length() > 0)
				result.put(symbol, parseQuote(symbol, line));
			else
				result.put(symbol, new Quote(symbol));

			i++;
		}

		return result;
	}
	*/

	/*
	public Quote getQuote(String symbol) {
		Set<String> symbols = new HashSet<String>(Arrays.asList(symbol));
		Map<String,Quote> map = getQuotes(symbols);

		return map.get(symbol);
	}
	*/

	/*
	@Deprecated
	public static Quote getQuote(String symbol)
	{
		String flags = "";
		for(String s : FLAGS)
			flags += s;
		
		String sResult = Tools.getURL("http://download.finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=" + flags + "&e=.csv");
		sResult = sResult.replace("\r\n", "");

		return parseQuote(symbol, sResult);
	}
	*/

	/*
	private static Quote parseQuote(String symbol, String line) {
		line = line.replace(", ", "&comma; ");
		String lines[] = line.split(",");

		if(lines.length != FLAGS.length)
			throw new IndexOutOfBoundsException("quotes.csv result does not match parameters");

		Quote quote = new Quote(symbol);

		for(int i = 0; i < FLAGS.length; i++)
		{
			String flag = FLAGS[i];
			String value = lines[i];

			switch(flag) {
				case "s":
					String s = parseString(value);
					if(!s.contentEquals(symbol))
						throw new IllegalStateException("parameter should match symbol");
					break;
				case "x": quote.exchange = parseString(value); break;
				case "n": quote.name = parseString(value); break;

				case "p": quote.prevClose = parseFloat(value); break;
				case "o": quote.open = parseFloat(value); break;
				case "g": quote.low = parseFloat(value); break;
				case "h": quote.high = parseFloat(value); break;
				case "l1": quote.lastTrade = parseFloat(value); break;
				case "c1": quote.change = parseFloat(value); break;
				case "p2": quote.changePercent = parseFloat(value); break;
				case "v": quote.volume = parseLong(value); break;
				case "d1": quote.lastTradeDate = parseDate(value); break;

				case "e": quote.eps = parseFloat(value); break;
				case "e7": quote.epsEstCurrentYear = parseFloat(value); break;
				case "e8": quote.epsEstNextYear = parseFloat(value); break;
				case "e9": quote.epsEstNextQuarter = parseFloat(value); break;

				case "b4": quote.bookValue = parseFloat(value); break;
				case "j4": quote.ebitda = parseString(value); break;
				case "p5": quote.priceSalesRatio = parseFloat(value); break;
				case "p6": quote.priceBookRatio = parseFloat(value); break;

				case "r": quote.peRatio = parseFloat(value); break;
				case "r5": quote.pegRatio = parseFloat(value); break;
				case "r6": quote.priceEPSEstCurrentYear = parseFloat(value); break;
				case "r7": quote.priceEPSEstNextYear = parseFloat(value); break;
				case "s7": quote.shortRatio = parseFloat(value); break;
				case "s6": quote.revenue = parseString(value); break;

				case "a2": quote.averageVolume = parseLong(value); break;
				case "t8": quote.oneYearTarget = parseFloat(value); break;
				case "k": quote.high52 = parseFloat(value); break;
				case "j": quote.low52 = parseFloat(value); break;

				case "m3": quote.sma50 = parseFloat(value); break;
				case "m4": quote.sma200 = parseFloat(value); break;

				case "j1": quote.marketCap = parseString(value); break;
				case "j2": quote.sharesTotal = parseLong(value); break;
				case "f6": quote.sharesFloat = parseLong(value); break;

				case "y": quote.dividendYield = parseFloat(value); break;
				case "q": quote.dividendDate = parseDate(value); break;
				case "d": quote.dividendsPerShare = parseFloat(value); break;

			}
		}

		return quote;
	}
	*/

	private static float parseFloat(String str)
	{
		try
		{
			str = str.replace("%","");
			str = str.replace("\"","");
			return Float.parseFloat(str);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}

	private static long parseLong(String number) {
		long result = 0;

		try {
			result = Long.parseLong(number);
		} catch(Exception e) {
			//e.printStackTrace();
		}
		return result;
	}

	private static Date parseDate(String date)
	{
		Date result;
		date = date.replace("\"", "");
		try 
		{
			result = mDateFormat.parse( date );
		}
		catch(Exception e)
		{		
			// Try alternate
			try
			{
				result = mDateFormatAlt.parse(date);
			}
			catch(Exception ee)
			{
				result = null;
				//ee.printStackTrace();
			}
		}
		
		return result;
	}

	private static String parseString(String str) {
		String result = str;
		result = result.replace("\"", "");
		result = result.replace("&comma;", ",");
		return result;
	}

	public static Price parseLine(String sLine) {
		String fields[] = sLine.split(",");
		if(fields.length == 7)
		{
			//TODO fix this for S&P large numbers
			try //Fails on S&P index since too large, just ignore it
			{
				float open = Float.parseFloat(fields[1]);
				float high = Float.parseFloat(fields[2]);
				float low  = Float.parseFloat(fields[3]);
				float close = Float.parseFloat(fields[4]);
				float adjClose = Float.parseFloat(fields[5]);

				if(adjClose != close)
				{
					if(close == open)
						open = adjClose;
					else
						open = (adjClose * open) / close;
	
					if(close == high) //Fix float rounding issues to prevent close > high when they are the same
						high = adjClose;
					else
						high = (adjClose * high) / close;
					
					if(close == low)
						low = adjClose;
					else
						low = (adjClose * low) / close;
				}

				// Correcting bad data
				if (open < low) {
					System.out.println("Correcting bad [open] data on " + fields[0]);
					open = (high + low) / 2;
				}
				
				long volume = Long.parseLong(fields[6], 10);
				volume /= 1000;
				Date date = mDateFormat.parse( fields[0] );
			
				return new Price(date, open, high, low, adjClose, (float)volume);
			}
			catch(Exception e)
			{		
				e.printStackTrace();
			}
		}
		
		return null;
	}

	/*
	@Override
	public List<Symbol> getSymbols(Set<String> symbols) {
		Map<String,Quote> quotes = getQuotes(symbols);
		List<Symbol> result = new ArrayList<>();

		for(Map.Entry<String, Quote> entry : quotes.entrySet()) {
			Quote q = entry.getValue();
			result.add(new Symbol(q.symbol, q.name, q.exchange));
		}

		return result;
	}

	@Override
	public Symbol getSymbol(String symbol) {
		Set<String> set = new HashSet<>();
		set.add(symbol);

		List<Symbol> list = getSymbols(set);
		if (list != null) {
			return list.get(0);
		}

		return null;
	}
	*/

	private boolean setCookieCrumb() {
		if (mCookieCrumb != null)
			return true;

		Response res = Tools.getURL("https://finance.yahoo.com/quote/%5EGSPC/options", null); // most any page will work here
		String page = res.result;
		int index = page.indexOf("\"CrumbStore\"");
		if (index > 0)
		{
			if (DEBUG) {
				String debug = page.substring(index, index + 50);
				System.out.println(debug);
			}

			index = page.indexOf("\"crumb\"", index);

			if (index > 0)
			{
				int start = page.indexOf("\"", index + 8) + 1;
				int end = page.indexOf("\"", start);
				if (start < end)
				{
					mCookieCrumb = page.substring(start, end);
					mCookieCrumb = mCookieCrumb.replace("\\u002F", "/");
					mCookie = res.headers.get("set-cookie").get(0);
					mCookie = mCookie.split(";")[0];
					return true;
				}
			}
		}

		return false;
	}

	private Calendar getCalendar(Interval interval, int count) {
		Calendar cal = Calendar.getInstance();
		if(interval == Interval.DAILY) //This one is just an estimate since there are various days the market is closed
		{
			final int trading_days_year = 250;
			int remaining = count;
			while(remaining >= trading_days_year)
			{
				cal.add(Calendar.YEAR, -1);
				remaining -= trading_days_year;
			}

			int weekdays = (int) (remaining * (365.0 / trading_days_year));
			cal.add(Calendar.DAY_OF_YEAR, 1-weekdays);
		}
		else if(interval == Interval.WEEKLY)
		{
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.add(Calendar.WEEK_OF_MONTH, -count - 1);
		}
		else if(interval == Interval.MONTHLY)
		{
			cal.add(Calendar.MONTH, -count);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}

		return cal;
	}
}
