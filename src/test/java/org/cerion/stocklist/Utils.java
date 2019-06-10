package org.cerion.stocklist;

import org.cerion.stocklist.model.Dividend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Utils {

	public static PriceList generateList(int size)
	{
		List<Price> prices = new ArrayList<>();
		for(int i = 0; i < size; i++)
			prices.add(new Price(new Date(), i, i, i, i, i));
		
		return new PriceList("TEST", prices);
	}

	public static List<Dividend> getDividends(float ...values) {
		Calendar calendar = Calendar.getInstance();
		List<Dividend> result = new ArrayList<>();

		for(float v : values) {
			Dividend d = new Dividend(calendar.getTime(), v);
			result.add(d);

			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}

		return result;
	}

	public static Date getDate(int daysAgo) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -daysAgo);
		return calendar.getTime();
	}
}
