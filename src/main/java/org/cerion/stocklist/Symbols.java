package org.cerion.stocklist;

import java.util.ArrayList;
import java.util.List;

public class Symbols {

	//http://www.nasdaqtrader.com/trader.aspx?id=symboldirdefs
	//ftp://ftp.nasdaqtrader.com/symboldirectory/nasdaqlisted.txt

	
	//TODO make everything in this class just return List<String>
	public static final String[] sectors = { "SPY", "XLY", "XLP", "XLE", "XLF", "XLV", "XLI", "XLB", "XLK", "XLU" };
	
	public static List<String> getSP500List()
	{
		String data = YahooFinance.getURL("https://en.wikipedia.org/wiki/List_of_S%26P_500_companies");
		
		//String arr[] = data.split("CIK=");
		String arr[] = data.split("</a></td>\r\n<td><a href");
		
		ArrayList<String> result = new ArrayList<>();
		for(int i = 1; i < arr.length; i++)
		{
			String t[] = arr[i].split(">");
			String s = t[t.length-1];
			s = s.trim();
			
			//Some are listed twice, for each class of stock
			//Don't add the one with the least historical data
			if(s.length() > 0 && !s.contentEquals("GOOG") && !s.contentEquals("DISCK") && !s.contentEquals("NWSA") && !s.contentEquals("FOXA")) 
			{
				result.add(s);
				//System.out.println(s);
			}
		}
		
		//System.out.println(result.size());
		return result;
	}
}
