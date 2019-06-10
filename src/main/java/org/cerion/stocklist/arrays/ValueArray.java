package org.cerion.stocklist.arrays;

public abstract class ValueArray {

	public abstract int size();

	//Use a lower period value when calculating array elements before that position so all values get set to something
	public static int maxPeriod(int pos, int period)
	{
		return (pos < period-1 ? pos+1 : period);
	}

}
