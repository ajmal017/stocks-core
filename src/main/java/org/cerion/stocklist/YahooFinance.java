package org.cerion.stocklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class YahooFinance 
{

	public static final int INTERVAL_DAILY = 0;
	public static final int INTERVAL_WEEKLY = 1;
	public static final int INTERVAL_MONTHLY = 2;

	protected static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static PriceList getPrices(String sSymbol, int interval, int count)
	{
		Calendar cal = Calendar.getInstance();

		if(interval == INTERVAL_DAILY) //This one is just an estimate since there are various days the market is closed
		{
			final int trading_days_year = 252;
			int remaining = count;
			while(remaining >= trading_days_year) 
			{
				cal.add(Calendar.YEAR, -1);
				remaining -= trading_days_year;
			}
			
			int weekdays = (int) (count * (365.0 / trading_days_year ));
			cal.add(Calendar.DAY_OF_YEAR, 1-weekdays);
		}
		else if(interval == INTERVAL_WEEKLY)
		{
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.add(Calendar.WEEK_OF_MONTH, (1-count)); 
		}
		else if(interval == INTERVAL_MONTHLY)
		{
			cal.add(Calendar.MONTH, (1-count));
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		return getPrices(sSymbol,interval,cal);
	}
	
	public static PriceList getPrices(String sSymbol, int interval, Calendar calendar)
	{
		String sURL = "http://ichart.yahoo.com/table.csv?s=" + sSymbol;
		Calendar cal = calendar;
		if(interval == INTERVAL_MONTHLY)
		{
			sURL += "&g=m"; //monthly (d/w/m and v=dividends)
			if(cal == null) 
				cal = new GregorianCalendar(1970,0,1);
		}
		else if(interval == INTERVAL_WEEKLY)
		{
			sURL += "&g=w"; //weekly
			if(cal == null)
				cal = new GregorianCalendar(2000,0,1);
		}
		else if(cal == null)
			cal = new GregorianCalendar(2000,0,1); //October 9, 2005
		
		//a = month minus 1
		//b = day of month
		//c = year
		sURL += String.format("&a=%d&b=%d&c=%d", cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
		
		System.out.println(sURL);
		
		String sData = getURL(sURL);
		
		String lines[] = sData.split("\\r\\n");
		

		ArrayList<Price> prices = new ArrayList<>();
		for(int i = 1; i < lines.length; i++)
			prices.add(parseLine(lines[i]));
		
		PriceList result = new PriceList(sSymbol,prices);
		
		System.out.println(result.size() + " " + sURL);
		
		return result;
	}
	

	private static Price parseLine(String sLine)
	{
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
				float adjClose = Float.parseFloat(fields[6]);
				
				if(adjClose != close)
				{
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
				
				long volume = Long.parseLong(fields[5], 10);
				volume /= 1000;
				Date date = mDateFormat.parse( fields[0] );
			
				return new Price(date, open, high, low, adjClose, volume);
			}
			catch(Exception e)
			{		
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	public static ArrayList<Dividend> getDividends(String symbol)
	{
		ArrayList<Dividend> result = new ArrayList<>();
		String sURL = "http://ichart.yahoo.com/table.csv?s=" + symbol + "&g=v";
		sURL += "&a=1&b=1&c=2000";
		
		String sData = getURL(sURL);
		String lines[] = sData.split("\\r\\n");
		for(int i = 1; i < lines.length; i++)
		{
			String line[] = lines[i].split(",");
			
			Dividend div = new Dividend(parseDate(line[0]), Float.parseFloat(line[1]) );
			result.add(0, div);
		}
		
		
		System.out.println(symbol + "\t" + result.size());
		return result;
	}
	
	public static Date parseDate(String date)
	{
		Date result = new Date();
		try 
		{
			result = mDateFormat.parse( date );
		}
		catch(Exception e)
		{		
			System.out.println(e);
		}
		
		return result;
	}
	
	//TODO, make protected then maybe private
    public static String getURL(String theUrl)
    {
    	String sResult = null;
    	
    	//TODO try with resources
        try 
        {
            URL gotoUrl = new URL(theUrl);
            InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
            BufferedReader in = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String inputLine;
            //grab the contents at the URL
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine+"\r\n");
            
            sResult = sb.toString();
        }
        catch (MalformedURLException mue) 
        {
            mue.printStackTrace();
        }
		catch (IOException e) 
		{
			sResult = "";
		}
		
        return sResult;
    }
}
