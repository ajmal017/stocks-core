package org.cerion.stocklist;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class PriceListTest {

	static PriceList mPriceList;

	@BeforeClass
	public static void init() {

		mPriceList = Helper.getSP500TestData();
	}

	@Test
	public void averageYearlyGain() {
		float gain = mPriceList.averageYearlyGain();

		assertEquals(0.0215, gain, 0.0001);
	}


}
