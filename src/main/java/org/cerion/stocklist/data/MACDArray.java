package org.cerion.stocklist.data;

public class MACDArray extends FloatArray {

	private FloatArray mSignal;
	
	public MACDArray(int size) { super(size); }

	//TODO, see if this can be done in constructor
	public void setSignal(int period)
	{
		mSignal = ema(period);
	}
	
	public float signal(int pos) //Signal line
	{
		return mSignal.get(pos);
	}
	
	public float hist(int pos) //Histogram
	{
		return mVal[pos] - mSignal.get(pos);
	}
}
