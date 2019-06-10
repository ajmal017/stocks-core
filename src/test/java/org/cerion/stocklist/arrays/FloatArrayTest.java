package org.cerion.stocklist.arrays;

import static org.junit.Assert.*;

import org.cerion.stocklist.Helper;
import org.junit.BeforeClass;
import org.junit.Test;

public class FloatArrayTest {

	static FloatArray mArr;
	
	@BeforeClass
	public static void init() {
		mArr = Helper.getSP500TestData().getClose();
	}
	
	@Test
	public void sma_1()
	{
		FloatArray sma = mArr.sma(1);
		assertEquals("Unexpected test arrays length", 4025, sma.size());
		
		for(int i = 0; i < sma.size(); i++)
			assertEqual(mArr.get(i), sma.get(i), "position " + i);
	}
	
	@Test
	public void sma_2()
	{
		FloatArray sma = mArr.sma(2);
		
		assertEquals("Unexpected test arrays length", 4025, sma.size());
		assertEqual(1455.22, sma.get(0), "position 0");
		assertEqual(1427.32, sma.get(1), "position 1");
		assertEqual(1429.40, sma.get(sma.size() / 2), "position " + (sma.size() / 2));
		assertEqual(2053.65, sma.get(sma.size() - 1), "position last");
	}
	
	@Test
	public void sma_20()
	{
		FloatArray sma = mArr.sma(20);
		
		assertEquals("Unexpected test arrays length", 4025, sma.size());
		assertEqual(1455.22, sma.get(0), "position 0");
		assertEqual(1427.32, sma.get(1), "position 1");
		assertEqual(1473.51, sma.get(sma.size() / 2), "position " + (sma.size() / 2));
		assertEqual(2050.38, sma.get(sma.size() - 1), "position last");
	}
	
	@Test
	public void sma_200()
	{
		FloatArray sma = mArr.sma(200);
		
		assertEquals("Unexpected test arrays length", 4025, sma.size());
		assertEqual(1455.22, sma.get(0), "position 0");
		assertEqual(1427.32, sma.get(1), "position 1");
		assertEqual(1490.95, sma.get(sma.size() / 2), "position " + (sma.size() / 2));
		assertEqual(2061.15, sma.get(sma.size() - 1), "position last");
	}
	
	@Test
	public void sma_usesHighestAverage()
	{
		FloatArray sma20 = mArr.sma(20);
		FloatArray sma100 = mArr.sma(100);
		FloatArray sma200 = mArr.sma(200);
		
		for(int i = 0; i < 20; i++)
			assertEqual(sma20.get(i), sma100.get(i), "20 and 100 position " + i);
	
		for(int i = 0; i < 20; i++)
			assertEqual(sma20.get(i), sma200.get(i), "20 and 200 position " + i);
		
		for(int i = 0; i < 100; i++)
			assertEqual(sma100.get(i), sma200.get(i), "100 and 200 position " + i);
	}

	@Test
	public void slope() {
		assertEqual(0.0, mArr.slope(5, 0), "slope position 0");
		assertEqual(-55.8, mArr.slope(5, 1), "slope position 1");
		assertEqual(-2.35, mArr.slope(5, 4), "slope position 4");
		assertEqual(15.57, mArr.slope(5, 5), "slope position 5");
		assertEqual(-2.72, mArr.slope(5, mArr.size() - 1), "slope position last"); // stockcharts verified
	}

	@Test
	public void regressionLine() {
		assertEqual(1455.22, mArr.regressionLinePoint(5, 0), "regressionLine position 0");
		assertEqual(1483.12, mArr.regressionLinePoint(5, 1), "regressionLine position 1");
		assertEqual(1422.68, mArr.regressionLinePoint(5, 4), "regressionLine position 4");
		assertEqual(1405.24, mArr.regressionLinePoint(5, 5), "regressionLine position 5");
		assertEqual(2063.35, mArr.regressionLinePoint(5, mArr.size() - 1), "regressionLine position last");
	}

	@Test
	public void zeroLengthArrays() {
		FloatArray arr = new FloatArray(0);
		arr.ema(20);
	}

	@Test
	public void sum() {
		assertEquals(1455.22, mArr.sum(0,0), 0.005);
		assertEquals(2854.64, mArr.sum(0,1), 0.005);
		assertEquals(8698.38, mArr.sum(5,10), 0.005);
	}

	@Test
	public void std() {
		int period = 10;
		FloatArray std = mArr.std(period);

		assertEqual(0.0, std.get(0), "standard deviation position 0");
		assertEqual(27.90, std.get(1), "standard deviation position 1");
		assertEqual(23.34, std.get(period-1), "standard deviation position p-1");
		assertEqual(23.33, std.get(period), "standard deviation position p");
		assertEqual(20.78, std.last(), "standard deviation position last");
	}

	@Test
	public void correlation() {
		float corr = mArr.correlation(mArr);
		assertEqual(1.0, corr, "correlation self");
		assertEquals("correlation self", 1.0, corr, 0.000001);

		FloatArray volume = Helper.getSP500TestData().getVolume();
		FloatArray high = Helper.getSP500TestData().getHigh();

		corr = mArr.correlation(high);
		assertEquals("correlation high", 0.999527, corr, 0.000001);

		corr = mArr.correlation(volume);
		assertEquals("correlation volume", 0.069388, corr, 0.000001);
	}

	private static void assertEqual(double expected, float actual, String message) {
		assertEquals(message, expected, actual, 0.005);
	}

}
