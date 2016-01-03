package org.cerion.stocklist.data;

public class BandArray extends ValueArray {

	private FloatArray mSource;
	private FloatArray mAverage; //SMA or EMA
	private FloatArray mRange;   //Standard Deviation or ATR
	private float mMultiplier;
	
	//TODO, convert bollinger and channels to use this method if possible
	private boolean bPriceChannel;
	private FloatArray mUpper;
	private FloatArray mLower;
	
	@Override
	public int size() { return mAverage.size(); }
	

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
