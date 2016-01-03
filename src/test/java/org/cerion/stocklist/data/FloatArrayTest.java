package org.cerion.stocklist.data;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class FloatArrayTest {

	static FloatArray arr;
	
	@BeforeClass
	public static void init() {
		arr = new FloatArray(1000);
		for(int i = 0; i < arr.size(); i++)
			arr.mVal[i] = 1;
	}
	
	@Test
	public void sma() {
		FloatArray sma = arr.sma(20);
		
		float first = sma.get(0);
		float last = sma.get(sma.size()-1);
		float total = first + last;
		
		assertEquals(2.0, total, 0.0);
	}

}
