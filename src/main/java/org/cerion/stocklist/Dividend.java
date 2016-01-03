package org.cerion.stocklist;

import java.util.Date;

public class Dividend 
{
	public java.util.Date mDate;
	public float mDividend;
	
	
	public Dividend(Date date, float div)
	{
		mDate = date;
		mDividend = div;
	}
	
	public String getDate() { return YahooFinance.mDateFormat.format(mDate); } 
	
	
	/*
	private static float round(float r)
	{
		return (float) (Math.round(r * 100.0) / 100.0);	
	}
	
	
	public static void scan()
	{
		
		
		PriceList list = Main.getList("MFA");
		
		
		float ratio = 1;
		
		for(int i = list.size()-1; i >= 1000; i--)
		{
			Price p = list.get(i);
			float diff = round(p.close - p.adjClose);
			
			if(round(p.close / p.adjClose) != 1)
			{
				p = list.get(i);
				//float yield = (diff / p.adjClose) * 400;
				System.out.println(p.date + "\t" + diff);
				
				for(int j = i-1; j >= 1; j--)	
				{
					Price t = list.get(j);
					t.adjClose *= (p.close / p.adjClose);
					t.adjClose = round(t.adjClose);
				}
			}
			
				
			//ratio = 1;//p.close / p.adjClose;
			
			System.out.println(p.date + "\t" + p.adjClose + "\t" + p.close + "\t" + diff + "\t" + ratio);
		}
		
	}
	
	public static void scanDividends()
	{
		ArrayList<String> list = Data.getSymbols();
		for(int i = 0; i < list.size(); i++)
		{
			String s = list.get(i);
			float[] arr = getDividends(s);
			
			int up = 0;
			for(int j = 1; j < arr.length; j++)
			{
				if(arr[j] > arr[j-1] )
					up++;
				else if(arr[j] < arr[j-1] )
					up--;
			}
			
			if(up > 5)
			System.out.println(s + "\t" + up);
		}
		
		
	}
	
	public static float[] getDividends(String symbol)
	{
		ArrayList<Float> arr = new ArrayList<Float>();
		
		Statement stmt = null;
		try 
		{
			stmt = SQLData.getConn().createStatement();
			String sql;
			
			sql = "SELECT * from Dividends Where Symbol='" + symbol + "' Order By Date ASC";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{

				float div = rs.getFloat("Dividend");
				arr.add(div);
			}
			
			rs.close();
			stmt.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		 
		
		float result[] = new float[arr.size()];
		for(int i = 0; i < arr.size();i++)
			result[i] = (float) arr.get(i);
			
		return result;
	}
	
	*/
	
}
