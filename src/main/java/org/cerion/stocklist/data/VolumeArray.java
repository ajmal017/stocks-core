package org.cerion.stocklist.data;

public class VolumeArray extends ValueArray {

	public long[] mVal;
	
	@Override
	public int size() { return mVal.length; };
	
	public VolumeArray(int size) {
		mVal = new long[size];
	}
	
	public long get(int pos)
	{
		return mVal[pos];
	}
	
	
	public VolumeArray sma(int period)
	{
		int size = mVal.length;
		VolumeArray result = new VolumeArray(size);
	
		//Copied from FloatArray version
		for(int i = 0; i < size; i++)
		{
			int count = maxPeriod(i,period);
			long total = 0;
			for(int j = i-count+1; j <= i; j++)
				total += mVal[j];
			
			result.mVal[i] = total / count;
			
		}
		
		return result;
	}
	
	public VolumeArray ema(int period)
	{
		int size = size();
		VolumeArray result = new VolumeArray(size);
		float mult = 2.0f / (1f + period); //EMA multiplier
	
		result.mVal[0] = mVal[0]; //initialize with first value
		for(int i = 1; i < size; i++)
			result.mVal[i] = (long)((mVal[i] - result.mVal[i-1]) * mult) + result.mVal[i-1];

		return result;
	}
}
