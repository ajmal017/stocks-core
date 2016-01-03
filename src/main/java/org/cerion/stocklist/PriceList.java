package org.cerion.stocklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cerion.stocklist.data.BandArray;
import org.cerion.stocklist.data.FloatArray;
import org.cerion.stocklist.data.MACDArray;
import org.cerion.stocklist.data.PairArray;
import org.cerion.stocklist.data.ValueArray;
import org.cerion.stocklist.data.VolumeArray;

//TODO, remove extends ArrayList and insert necessary functions
//TODO https://source.android.com/source/code-style.html#follow-field-naming-conventions
public class PriceList extends ArrayList<Price>
{
	protected java.util.Date[] mDate;
	protected FloatArray mOpen;
	protected FloatArray mHigh;
	protected FloatArray mLow;
	protected FloatArray mClose;
	protected VolumeArray mVolume;
	
	public float high(int pos) { return mHigh.get(pos); }
	public float low(int pos) { return mLow.get(pos); }
	public float close(int pos) { return mClose.get(pos); }
	public long volume(int pos) { return mVolume.get(pos); }
	
	public Date[] getDates() { return mDate; }
	public FloatArray getOpen() { return mOpen; }
	public FloatArray getClose() { return mClose; }
	public FloatArray getHigh() { return mHigh; }
	public FloatArray getLow() { return mLow; }
	public VolumeArray getVolume() { return mVolume; }
	
	//Values derived from current days prices
	public float tp(int pos) { return (mClose.get(pos) + mHigh.get(pos) + mLow.get(pos)) / 3; } //Typical price
	public float mfv(int pos) //Money flow volume
	{
		float mult = ((close(pos)  -  low(pos)) - (high(pos) - close(pos))) /(high(pos) - low(pos));
		if(close(pos) == low(pos))
			mult = -1;
		if(low(pos) == high(pos)) //divide by zero
			mult = 0;
		
		return mult * volume(pos);
	} 
	
	//Rate of change
	public float roc(int pos, int period)
	{
		int x = 0; //If period goes beyond start then set to first element
		if(pos >= period)
			x = pos-period;
		
		return ((close(pos) - close(x)) * 100) / close(x);
	}
	
	public static final int TYPE_DAILY = 0;
	public static final int TYPE_WEEKLY = 1;
	public static final int TYPE_MONTHLY = 2;
	public String mSymbol;
	public int mType = TYPE_DAILY;

	private static final long serialVersionUID = 1L;
	
	//private Map<String,float[]> mMapIndicators = new HashMap<String,float[]>();
	private Map<String,ValueArray> mMapIndicators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	public PriceList(String symbol, List<Price> list)
	{
		Collections.sort(list);
		
		mSymbol = symbol;
		int size = list.size();
		
		mDate = new java.util.Date[size];
		mOpen = new FloatArray(size);
		mHigh = new FloatArray(size);
		mLow = new FloatArray(size);
		mClose = new FloatArray(size);
		mVolume = new VolumeArray(size);
		//mAdjCloses = new float[size];
		
		for(int i = 0; i < size; i++)
		{
			Price p = list.get(i);
			p.pos = i;
			p.parent = this;
			
			try
			{
			mDate[i] = p.date;
			mOpen.mVal[i] = p.open;
			mHigh.mVal[i] = p.high;
			mLow.mVal[i] = p.low;
			mClose.mVal[i] = p.close;
			mVolume.mVal[i] = p.volume;
		
			//mAdjCloses[i] = p.adjClose;
			}
			catch(Exception e)
			{
				System.out.println("Error");
			}
			
			
			this.add(p);
		}
	}
	
	//For estimating new price near closing, why are other values missing?
	/*
	public Price addPrice(float close)
	{
		Price prev = get(size()-1);

		Price p = new Price();
		p.adjClose = close;
		p.low = close;
		p.high = close;
		p.close = close;
		p.date = new Date();
		p.date.setTime(prev.date.getTime() + 1 * 24 * 60 * 60 * 1000); 
		
		add(p);
		
		return p;
	}
	*/


	/*
	 * Find the highest high price in the range [start,end]
	 */
	public float highestHigh(int start, int end)
	{
		return mHigh.max(start, end);
	}
	
	/*
	 * Find the lowest low price in the range [start,end]
	 */
	public float lowestLow(int start, int end)
	{
		return mLow.min(start, end);
	}
	
	public Price getLast(int prev)
	{
		return get(size()-1-prev);
	}
	
	public Price getLast()
	{
		return getLast(0);
	}
	
   
    //Format = EMA [SMA [RSI 14] 20] 20
    //       = EMA [Adjclose] 20
    //       = EMA 20
    public ValueArray getValues(String key)
    {
    	//key = key.toUpperCase(); //ensure consistency
    	
    	ValueArray result = mMapIndicators.get(key);
    	if(result != null)
    		return result;

    	Tokenizer tokenizer = new Tokenizer(key);
    	
    	if(tokenizer.hasInput())
    	{
    		Tokenizer.Token tok = tokenizer.GetNext();
    		if(tok.type == Tokenizer.Token.TYPE_STRING)
    		{
    			String indicator = tok.sVal; //Function name
    			ArrayList<Function.Parameter> params = new ArrayList<>();
    	    	while(tokenizer.hasInput())
    	    	{
    	    		Function.Parameter param = null;
    		    	tok = tokenizer.GetNext();
    		    	
    		    	//TODO type check this and pass in Decimal[] to Parameter constructor since thats the only value that can ever work
    		    	if(tok.type == Tokenizer.Token.TYPE_ARRAY)
    		    		param = new Function.Parameter(getValues(tok.sVal) );
    		    	else if(tok.type == Tokenizer.Token.TYPE_STRING)
    		    	{
    		    		System.out.println("ERROR");
    		    		param = null;
    		    		//FloatArray arr = getBasicArray(tok.sVal);
    		    		//param = new Function.Parameter(tok.sVal, arr);
    		    	}
    		    	else
    		    		param = new Function.Parameter( tok.nVal );
   
    		    	params.add(param);
    	    	}
    	    	
    	    	//Construct new function with name and its parameters
    	    	Function ind = new Function(this,indicator, params);
    	    	result = ind.eval();
    	    	
    	    	//result = Indicator.getValues(indicator, this, params);
    		}
    		else
    		{
    			System.out.println("ERROR expected token type " + tok.type);
    		}
    	}
    	
    	System.out.println("Lookup: " + key + " mapCount=" + mMapIndicators.size() + " symbol=" + mSymbol);
    	
    	if(result != null)
    	{
    		mMapIndicators.put(key, result);
    	}
    	else
    		System.out.println("PriceList.getValues ERROR");
    	
    	return result;
    }
    
    public float tr(int pos)
    {
        if(pos > 0)
            return Math.max(mHigh.get(pos),mClose.get(pos-1)) - Math.min(mLow.get(pos),mClose.get(pos-1));
        return mHigh.get(0) - mLow.get(0);
    } 
    
    //Simple overlays
    public FloatArray ema(int period) { return (FloatArray)getValues("EMA " + period); }
    public FloatArray sma(int period) { return (FloatArray)getValues("SMA " + period); }
    public VolumeArray smaVolume(int period) { return (VolumeArray)getValues("SMA_VOLUME " + period); }
    
    //Indicators
    public FloatArray rsi(int period) { return (FloatArray)getValues("RSI " + period); }
	public FloatArray rsiAvg(int rsiPeriod, int smaPeriod) { return (FloatArray)getValues( String.format("SMA [RSI %d] %d", rsiPeriod, smaPeriod)); }
	public FloatArray rsiStd(int rsiPeriod, int stdPeriod) { return (FloatArray)getValues( String.format("STD [RSI %d] %d", rsiPeriod, stdPeriod)); }
	public FloatArray stochRSI(int period) { return (FloatArray)getValues("STOCHRSI " + period); }
	public FloatArray forceIndex(int period) { return (FloatArray)getValues( String.format("FORCE_INDEX %d", period)); }
	public FloatArray mfi(int period) { return (FloatArray)getValues( String.format("MFI %d", period)); }
	public FloatArray cmf(int period) { return (FloatArray)getValues( String.format("CMF %d", period)); }
	public VolumeArray onBalanceVolume() { return (VolumeArray)getValues("OBV"); }
	public FloatArray pSAR(float step, float max) { return (FloatArray)getValues( String.format("PSAR %f %f", step, max)  ); }
	public FloatArray adl() { return (FloatArray)getValues("ADL"); }
	public FloatArray co(int p1, int p2) { return (FloatArray)getValues( String.format("CO %d %d", p1, p2)); }
	public FloatArray uo(int p1, int p2, int p3) { return (FloatArray)getValues( String.format("UO %d %d %d", p1, p2, p3)); }
	public FloatArray tsi(int p1, int p2) { return (FloatArray)getValues( String.format("TSI %d %d", p1, p2)); }
	public FloatArray massIndex(int p1) { return (FloatArray)getValues( String.format("MASS_INDEX %d", p1)); }
	public FloatArray cci(int p1) { return (FloatArray)getValues( String.format("CCI %d", p1)); }
	public FloatArray trix(int p1) { return (FloatArray)getValues( String.format("TRIX %d", p1)); }
	public FloatArray ulcer(int period) { return (FloatArray)getValues( String.format("ULCER %d", period)); }
	public FloatArray atr(int period) { return (FloatArray)getValues( String.format("ATR %d", period)); }
	public FloatArray emv(int period) { return (FloatArray)getValues( String.format("EMV %d", period)); }
	public FloatArray nvi() { return (FloatArray)getValues("NVI"); }
	public FloatArray wpr(int period) { return (FloatArray)getValues( String.format("WPR %d", period)); }
	public FloatArray stoch(int K) { return (FloatArray)getValues( String.format("STOCH %d", K)); }
	public FloatArray stoch(int K, int D) { return (FloatArray)getValues( String.format("STOCH %d %d", K, D)); }
	public FloatArray stoch(int K, int fastD, int slowD) { return (FloatArray)getValues( String.format("STOCH %d %d %d", K, fastD, slowD)); }
	public FloatArray adx(int period) { return (FloatArray)getValues( String.format("ADX %d", period)); }
	
	//Pair Values
	public PairArray vortex(int period) { return (PairArray)getValues( String.format("VORTEX %d", period)); }
	public PairArray aroon(int period) { return (PairArray)getValues( String.format("AROON %d", period)); }
	public PairArray di(int period) { return (PairArray)getValues( String.format("DI %d", period)); }
	public PairArray cexit(int period, float multiplier) { return (PairArray)getValues( String.format("CEXIT %d %f", period, multiplier)); }
	public PairArray cloud(int conversion, int base, int span) { return (PairArray)getValues( String.format("CLOUD %d %d %d", conversion, base, span)); }
	
	
	public FloatArray specialK() { return (FloatArray)getValues("SPECIALK"); }
	public FloatArray kst(int r1, int r2, int r3, int r4, int ma1, int ma2, int ma3, int ma4) 
			{ return (FloatArray)getValues( String.format("KST %d %d %d %d %d %d %d %d", r1, r2, r3, r4, ma1, ma2, ma3, ma4)); }
	
	public FloatArray pmo(int p1, int p2) { return (FloatArray)getValues( String.format("PMO %d %d", p1, p2)); }
	
	//MACD related
	public MACDArray macd(int p1, int p2, int signal) { return (MACDArray)getValues( String.format("MACD %d %d %d", p1, p2, signal)); }
	public MACDArray ppo(int p1, int p2, int signal)  { return (MACDArray)getValues( String.format("PPO %d %d %d", p1, p2, signal)); }
	public MACDArray pvo(int p1, int p2, int signal)  { return (MACDArray)getValues( String.format("PVO %d %d %d", p1, p2, signal)); }
	
	//Bands
	public BandArray bb(int period, float multiplier) { return (BandArray)getValues( String.format("BB %d %f", period, multiplier)); }
	public BandArray kc(int emaPeriod, float multiplier, int atrPeriod) { return (BandArray)getValues( String.format("KC %d %f %d", emaPeriod, multiplier, atrPeriod)); }
	public BandArray chan(int period) { return (BandArray)getValues( String.format("CHAN %d", period)); }
	
    public float slope(int period, int pos) { return this.mClose.slope(period, pos); }
    
    
    @Deprecated
    public FloatArray getBasicArray(String sType)
    {
    	if(sType.equalsIgnoreCase("close"))
    		return this.mClose;
    	
    	System.out.println("ERROR: getBasicArray()");
    	return null;
    	
    }
    
}
