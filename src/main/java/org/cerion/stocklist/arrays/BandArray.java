package org.cerion.stocklist.arrays;

public class BandArray extends ValueArray {

	private FloatArray mSource;
	private FloatArray mAverage; //SimpleMovingAverage or ExpMovingAverage
	private FloatArray mRange;   //Standard Deviation or ATR
	private float mMultiplier;

	private boolean bPriceChannel;
	private FloatArray mUpper;
	private FloatArray mLower;
	
	@Override
	public int size() { return mSource.size(); }
	

	/**
	 * Bollinger Bands
	 */
	protected BandArray(FloatArray arr, int period, float multiplier)
	{
		mSource = arr;
		mMultiplier = multiplier;
		mAverage = arr.sma(period);
		mRange = arr.std(period, mAverage);
	}
	
	/**
	 * Keltner Channel
	 */
	public BandArray(FloatArray arr, float multiplier, FloatArray ema, FloatArray atr)
	{
		mSource = arr;
		mMultiplier = multiplier;
		mAverage = ema;
		mRange = atr;
	}
	

	/**
	 * Price Channels
	 */
	public BandArray(FloatArray upper, FloatArray lower)
	{
		mSource = upper; // TODO better way to handle this? Source is unused but it is on all the others
		bPriceChannel = true;
		mUpper = upper;
		mLower = lower;
	}
	
	public float mid(int pos) 
	{ 
		if(bPriceChannel)
			return (upper(pos) + lower(pos)) / 2;
		
		return mAverage.mVal[pos]; 
	}

	public float source(int pos) {
		return mSource.get(pos);
	}
	
	public float lower(int pos)
	{
		if(bPriceChannel)
			return mLower.get(pos);
		
		return mAverage.mVal[pos] - (mMultiplier * mRange.mVal[pos]);
	}
	
	public float upper(int pos)
	{
		if(bPriceChannel)
			return mUpper.get(pos);
		
		return mAverage.mVal[pos] + (mMultiplier * mRange.mVal[pos]);
	}
	
	public float bandwidth(int pos)
	{
		//(Upper Band - Lower Band)/Middle Band
		return ((upper(pos) - lower(pos)) / mid(pos)) * 100;
	}
	
	public float percent(int pos)
	{
		//%B = (Price - Lower Band)/(Upper Band - Lower Band)
		return (mSource.get(pos) - lower(pos))/(upper(pos) - lower(pos));
	}
}
