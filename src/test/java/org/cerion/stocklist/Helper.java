package org.cerion.stocklist;

import org.cerion.stocklist.web.api.YahooFinance;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class Helper {

	
	public static PriceList getSP500TestData()
	{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL res = classloader.getResource(".");
		InputStream is = classloader.getResourceAsStream("sp500_2000-2015.csv");
		
		try
		{
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader in = new BufferedReader(isr);
	        StringBuffer sb = new StringBuffer();
	        String inputLine;
	        //grab the contents at the URL
	        while ((inputLine = in.readLine()) != null)
	            sb.append(inputLine+"\r\n");
		     
	        String data = sb.toString();
	        return new PriceList("^GSPC", YahooFinance.getPricesFromTable(data));
		}
		catch(Exception e)
		{
			// Nothing
		}
        
		return null;
	}
}
