package org.cerion.stocklist.arrays;

import static org.junit.Assert.*;

import org.cerion.stocklist.Helper;
import org.cerion.stocklist.TestBase;
import org.cerion.stocklist.overlays.SimpleMovingAverage;
import org.junit.BeforeClass;
import org.junit.Test;

public class FloatArrayTest extends TestBase {

	static FloatArray mArr;
	
	@BeforeClass
	public static void init() {
		mArr = TestBase.Companion.getPriceList().getClose();
	}

	@Test
	public void sma_callsSimpleMovingAverage() {
		FloatArray arr1 = new SimpleMovingAverage(13).eval(mArr);
		FloatArray arr2 = mArr.sma(13);

		assertEquals("all elements should be equal", arr1.last(), arr2.last(), 0.00000001);
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

		FloatArray volume = TestBase.Companion.getPriceList().getVolume();
		FloatArray high = TestBase.Companion.getPriceList().getHigh();

		corr = mArr.correlation(high);
		assertEquals("correlation high", 0.999527, corr, 0.000001);

		corr = mArr.correlation(volume);
		assertEquals("correlation volume", 0.069388, corr, 0.000001);
	}
}
