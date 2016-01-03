package org.cerion.stocklist;

import java.util.List;

import org.cerion.stocklist.data.FloatArray;
import org.cerion.stocklist.data.ValueArray;

class Function 
{
	private static final int PARAM_PERIOD = 0;
	private static final int PARAM_ARRAY = 1;
	private static final int PARAM_FLOAT = 2; 
	
	
	//Overlays
	private static final int NAME_SMA = 1;
	private static final int NAME_EMA = 2;
	private static final int NAME_STD = 3;
	private static final int NAME_BB = 4; //bollinger bands
	private static final int SMA_VOLUME = 5;
	
	//Indicators
	private static final int IND_RSI = 100;
	private static final int IND_FORCE_INDEX = 101;
	private static final int IND_OBV = 102; //On Balance Volume
	private static final int IND_PSAR = 103;
	private static final int IND_MFI  = 104; //Money flow index
	private static final int IND_ADL  = 105; //Accumulation Distribution Line
	private static final int IND_CO   = 106; //Chaikin Oscillator
	private static final int IND_CMF  = 107; //Chaikin Money Flow
	private static final int IND_UO   = 108; //Ultimate Oscillator
	private static final int IND_TSI  = 109; //True Strength Index
	private static final int IND_MASS_INDEX  = 110; //Mass Index
	private static final int IND_CCI  = 111; //Commodity Channel Index
	private static final int IND_PMO  = 112; //Price Momentum Oscillator
	private static final int IND_TRIX = 113;
	private static final int IND_MACD = 114;
	private static final int IND_PPO  = 115; //Percentage Price Oscillator
	private static final int IND_STOCHRSI = 116; //Stochastic RSI
	private static final int IND_ULCER = 117; //Ulcer index
	private static final int IND_ATR   = 118; //Average True Range
	private static final int IND_NVI   = 119; //Negative Volume Index
	private static final int IND_KST   = 120; //Know Sure Thing
	private static final int IND_SPECIALK = 121;
	private static final int IND_PVO   = 122; //Percentage Volume Oscillator
	private static final int IND_EMV   = 123; //Ease of movement
	private static final int IND_KC    = 124; //Keltner channel
	private static final int IND_WPR   = 125; //Williams %R
	private static final int IND_VORTEX = 126; //Vortex Indicator
	private static final int IND_AROON = 127; //Aroon Up/Down
	private static final int IND_STOCH = 128; //Stochastic Oscillator
	private static final int IND_DI    = 129; //Directional Index
	private static final int IND_ADX   = 130; //Average Directional Index
	private static final int IND_CEXIT = 131; //Chandelier Exit
	private static final int IND_CHAN  = 132; //Price Channels
	private static final int IND_CLOUD = 133; //Ichimoku Clouds
	
	
	private int mFunctionId;
	//public int[] mParams;
	//private int mType;
	private List<Parameter> mParams;
	private PriceList mList;

	public static String paramToString(int type)
	{
		switch(type)
		{
			case PARAM_PERIOD: return "Integer";
			case PARAM_FLOAT: return "Decimal";
			case PARAM_ARRAY: return "Array";
		}
		
		return "";
	}
	public static class Parameter
	{
		public int mType;
		public Number mNumber;
		public FloatArray mArray;
		//public Float mFloat;
		//public String mArraySource;
		
		public Parameter(Number num)
		{
			if(num.getClass() == Float.class)
				mType = PARAM_FLOAT;
			else
				mType = PARAM_PERIOD;
			mNumber = num;
		}
		
		public Parameter(ValueArray arr)
		{
			//mArraySource = arraySource;
			mType = PARAM_ARRAY;
			mArray = (FloatArray)arr;
		}
		
		/*
		public Parameter(Float f)
		{
			mType = PARAM_FLOAT;
			mNumber = f;
			//mFloat = f;
		}
		*/
		
		@Override
		public String toString()
		{
			return paramToString(mType);
		}
		
	}

	private Function(PriceList list, int fid, List<Parameter> parameters)
	{
		mFunctionId = fid;
		mList = list;
		int[] params = null;
		
		
		//Overlays, if an overlay is without an array parameter assume its being called on the closing price
		switch(mFunctionId)
		{
			case NAME_SMA: 
			case NAME_EMA:
			case NAME_STD:
			case NAME_BB: 
				Parameter p = parameters.get(0);
				if(p.mType != PARAM_ARRAY)
					parameters.add(0, new Parameter(list.mClose));
				
				break;
		}
		
		
		switch(mFunctionId)
		{
			//Indicators
			case IND_RSI: 
			case IND_FORCE_INDEX: 
			case IND_MFI:
			case IND_CMF:
			case IND_MASS_INDEX:
			case IND_CCI:
			case IND_TRIX:
			case IND_STOCHRSI:
			case IND_ULCER:
			case IND_ATR:
			case IND_EMV:
			case IND_WPR:
			case IND_VORTEX:
			case IND_AROON:
			case IND_DI:
			case IND_ADX:
			case IND_CHAN:
				params = new int[]{PARAM_PERIOD};
				break;
					
			case IND_CO:
			case IND_TSI:
			case IND_PMO:
				params = new int[]{PARAM_PERIOD, PARAM_PERIOD};
				break;
				
			case IND_OBV: //No parameters
			case IND_ADL:
			case IND_NVI:
			case IND_SPECIALK:
				params = new int[]{};
				break;
				
			//An overlay but only on PriceList
			case IND_CEXIT: 
				params = new int[]{PARAM_PERIOD, PARAM_FLOAT};
				break;
				
			case IND_PSAR: params = new int[]{PARAM_FLOAT, PARAM_FLOAT }; break;
			
			//3 Periods
			case IND_MACD:
			case IND_PPO:
			case IND_PVO:
			case IND_CLOUD:
			case IND_UO: params = new int[]{PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD }; break;
			
			//4+ Periods
			case IND_KST:
				params = new int[]{PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD, PARAM_PERIOD }; 
				break;
				
			case IND_KC:
				params = new int[]{PARAM_PERIOD, PARAM_FLOAT, PARAM_PERIOD }; 
				break;
				
			//Allows 1-3 period parameters but as long as first one is valid the others can be ignored if needed
			case IND_STOCH:
				params = new int[]{PARAM_PERIOD }; 
				break;
				
			//Overlays
			case NAME_SMA: 
			case NAME_EMA:
			case NAME_STD:
				params = new int[]{PARAM_ARRAY, PARAM_PERIOD};
				break;
				
			case NAME_BB: 
				params = new int[]{PARAM_ARRAY, PARAM_PERIOD, PARAM_FLOAT}; //float is standard deviation
				break;
				
			case SMA_VOLUME:
				params = new int[]{PARAM_PERIOD};
				break;
				
		}
		
		mParams = parameters;
		
		//Check if the minimum required parameters are entered, if optional parameters are invalid they can just be ignored
		if(params == null)
			throw new IllegalArgumentException("Invalid function name");
		else if(params.length > mParams.size())
			throw new IllegalArgumentException("Missing Parameter");
		else
		{
			for(int i = 0; i < params.length; i++)
			{
				if(params[i] != mParams.get(i).mType)
				{
					String actual = paramToString(mParams.get(i).mType);
					String expected = paramToString(params[i]);
					throw new IllegalArgumentException("Invalid parameter at position " + i + " type=" + actual + " expected type=" + expected);
				}
			}
		}
		
		

	}
	
	public Function(PriceList list, String name, List<Parameter> parameters)
	{
		this(list,nameToId(name),parameters);
	}
	
	private static int nameToId(String name)
	{
		int id = -1;
		if(name.equalsIgnoreCase("RSI")) id = IND_RSI;
		else if(name.equalsIgnoreCase("SMA")) id = NAME_SMA;
		else if(name.equalsIgnoreCase("EMA")) id = NAME_EMA;
		else if(name.equalsIgnoreCase("STD")) id = NAME_STD;
		else if(name.equalsIgnoreCase("BB")) id = NAME_BB;
		else if(name.equalsIgnoreCase("FORCE_INDEX")) id = IND_FORCE_INDEX;
		else if(name.equalsIgnoreCase("OBV")) id = IND_OBV;
		else if(name.equalsIgnoreCase("PSAR")) id = IND_PSAR;
		else if(name.equalsIgnoreCase("MFI")) id = IND_MFI;
		else if(name.equalsIgnoreCase("ADL")) id = IND_ADL;
		else if(name.equalsIgnoreCase("CO")) id = IND_CO;
		else if(name.equalsIgnoreCase("CMF")) id = IND_CMF;
		else if(name.equalsIgnoreCase("UO")) id = IND_UO;
		else if(name.equalsIgnoreCase("TSI")) id = IND_TSI;
		else if(name.equalsIgnoreCase("MASS_INDEX")) id = IND_MASS_INDEX;
		else if(name.equalsIgnoreCase("CCI")) id = IND_CCI;
		else if(name.equalsIgnoreCase("PMO")) id = IND_PMO;
		else if(name.equalsIgnoreCase("TRIX")) id = IND_TRIX;
		else if(name.equalsIgnoreCase("MACD")) id = IND_MACD;
		else if(name.equalsIgnoreCase("PPO")) id = IND_PPO;
		else if(name.equalsIgnoreCase("STOCHRSI")) id = IND_STOCHRSI;
		else if(name.equalsIgnoreCase("ULCER")) id = IND_ULCER;
		else if(name.equalsIgnoreCase("ATR")) id = IND_ATR;
		else if(name.equalsIgnoreCase("NVI")) id = IND_NVI;
		else if(name.equalsIgnoreCase("KST")) id = IND_KST;
		else if(name.equalsIgnoreCase("SPECIALK")) id = IND_SPECIALK;
		else if(name.equalsIgnoreCase("PVO")) id = IND_PVO;
		else if(name.equalsIgnoreCase("EMV")) id = IND_EMV;
		else if(name.equalsIgnoreCase("SMA_VOLUME")) id = SMA_VOLUME;
		else if(name.equalsIgnoreCase("KC")) id = IND_KC;
		else if(name.equalsIgnoreCase("WPR")) id = IND_WPR;
		else if(name.equalsIgnoreCase("VORTEX")) id = IND_VORTEX;
		else if(name.equalsIgnoreCase("AROON")) id = IND_AROON;
		else if(name.equalsIgnoreCase("STOCH")) id = IND_STOCH;
		else if(name.equalsIgnoreCase("DI")) id = IND_DI;
		else if(name.equalsIgnoreCase("ADX")) id = IND_ADX;
		else if(name.equalsIgnoreCase("CEXIT")) id = IND_CEXIT;
		else if(name.equalsIgnoreCase("CHAN")) id = IND_CHAN;
		else if(name.equalsIgnoreCase("CLOUD")) id = IND_CLOUD;
		
		return id;
	}
	
	private FloatArray getArrayParam(int pos)
	{
		Parameter param = mParams.get(pos);
		if(param.mType != PARAM_ARRAY)
			throw new IllegalStateException("Parameter " + pos + " is not type " + param.toString());
		 
		return param.mArray;
	}
	
	/*
	private String getArraySourceParam(int pos)
	{
		Parameter param = mParams.get(pos);
		if(param.mType != PARAM_ARRAY)
			return null; 
		
		return param.mArraySource;
	}
	*/
	
	private int getPeriodParam(int pos)
	{
		Parameter param = mParams.get(pos);
		if(param.mType != PARAM_PERIOD)
			throw new IllegalStateException("Parameter " + pos + " is not type " + param.toString());
		
		return param.mNumber.intValue();
	}
	
	private float getFloatParam(int pos)
	{
		Parameter param = mParams.get(pos);
		if(param.mType != PARAM_FLOAT)
			throw new IllegalStateException("Parameter " + pos + " is not type " + param.toString());
		
		return param.mNumber.floatValue();
	}
	
	public ValueArray eval()
	{
		ValueArray result = null;
		
		switch(mFunctionId)
		{
			//Indicators, functions on PriceList
			//No parameters
			case IND_OBV: return Indicators.onBalanceVolume( mList );
			case IND_NVI: return Indicators.negativeVolumeIndex( mList );
			case IND_ADL: return Indicators.accumulationDistributionLine( mList );
			case IND_SPECIALK: return Indicators.specialK( mList );
			
			//+1 parameter
			case IND_RSI: return Indicators.rsi( mList, getPeriodParam(0) );
			case IND_FORCE_INDEX: return Indicators.forceIndex( mList, getPeriodParam(0) );
			case IND_PSAR: return Indicators.parabolicSAR( mList, getFloatParam(0), getFloatParam(1) );
			case IND_MFI: return Indicators.moneyFlowIndex( mList, getPeriodParam(0) );
			case IND_CMF: return Indicators.chaikinMoneyFlow( mList, getPeriodParam(0) );
			case IND_CO: return Indicators.chaikinOscillator( mList, getPeriodParam(0), getPeriodParam(1)  );
			case IND_UO: return Indicators.ultimateOscillator( mList, getPeriodParam(0), getPeriodParam(1),getPeriodParam(2)  );
			case IND_TSI: return Indicators.trueStrengthIndex( mList, getPeriodParam(0), getPeriodParam(1) );
			case IND_MASS_INDEX: return Indicators.massIndex( mList, getPeriodParam(0) );
			case IND_CCI: return Indicators.commodityChannelIndex( mList, getPeriodParam(0) );
			case IND_TRIX: return Indicators.trix( mList, getPeriodParam(0) );
			case IND_STOCHRSI: return Indicators.stochasticRSI( mList, getPeriodParam(0) );
			case IND_ULCER: return Indicators.ulcerIndex( mList, getPeriodParam(0) );
			case IND_ATR: return Indicators.averageTrueRange( mList, getPeriodParam(0) );
			case IND_EMV: return Indicators.easeOfMovement( mList, getPeriodParam(0) );
			case IND_WPR: return Indicators.williamsPercentR( mList, getPeriodParam(0) );
			case IND_VORTEX: return Indicators.vortex( mList, getPeriodParam(0) );
			case IND_AROON: return Indicators.aroon( mList, getPeriodParam(0) );
			case IND_DI: return Indicators.directionalIndex( mList, getPeriodParam(0) );
			case IND_ADX: return Indicators.averageDirectionalIndex( mList, getPeriodParam(0) );
			case IND_CHAN: return Indicators.priceChannels( mList, getPeriodParam(0) );
			
			case IND_CLOUD: return Indicators.ichimokuCloud( mList, getPeriodParam(0), getPeriodParam(1), getPeriodParam(2) );
			
			case IND_MACD: return Indicators.macd( mList, getPeriodParam(0), getPeriodParam(1), getPeriodParam(2) );
			case IND_PPO: return Indicators.percentagePriceOscillator( mList, getPeriodParam(0), getPeriodParam(1), getPeriodParam(2) );
			case IND_PVO: return Indicators.percentageVolumeOscillator( mList, getPeriodParam(0), getPeriodParam(1), getPeriodParam(2) );
			case IND_PMO: return Indicators.priceMomentumOscillator( mList, getPeriodParam(0), getPeriodParam(1) );
			

			case IND_KST: return Indicators.knowSureThing( mList, getPeriodParam(0), getPeriodParam(1), getPeriodParam(2), getPeriodParam(3),
																  getPeriodParam(4), getPeriodParam(5), getPeriodParam(6), getPeriodParam(7));
			
			case IND_KC: return Indicators.keltnerChannel(mList, getPeriodParam(0), getFloatParam(1), getPeriodParam(2) );
				
			case IND_CEXIT: return Indicators.chandelierExit(mList, getPeriodParam(0), getFloatParam(1) );
			
			
			case IND_STOCH: 
				if(mParams.size() == 3)
					return Indicators.stochastic(mList, getPeriodParam(0), getPeriodParam(1), getPeriodParam(2) );
				else if(mParams.size() == 2)
					return Indicators.stochastic(mList, getPeriodParam(0), getPeriodParam(1) );
				else
					return Indicators.stochastic(mList, getPeriodParam(0) );
			

			//Overlays, functions on arrays
			case NAME_SMA: return getArrayParam(0).sma( getPeriodParam(1) );
			case NAME_EMA: return getArrayParam(0).ema( getPeriodParam(1) );
			case NAME_STD: return getArrayParam(0).std( getPeriodParam(1) );
			
			
			case NAME_BB:
				//String arr = getArraySourceParam(0);
				//if(arr.charAt(0) == '[')
				//	arr = arr.substring(1, arr.length()-1);
				int period = getPeriodParam(1);
				//FloatArray std;
				
				//FloatArray sma = (FloatArray)mList.getValues("SMA " + arr + " " + period);
				//FloatArray std = (FloatArray)mList.getValues("STD " + arr + " " + period);
				
				return mList.mClose.bb(period, getFloatParam(2));
				//return new BandArray(sma,std,getFloatParam(2));
				
				//return Overlays.std( getArrayParam(0) , getPeriodParam(1) );
				
			case SMA_VOLUME:
				return mList.getVolume().sma( getPeriodParam(0));
		}
		
		return result;
	}
	
	
	//Use a lower period value when calculating array elements before that position so all values get set to something
	protected static int maxPeriod(int pos, int period)
	{
		return (pos < period-1 ? pos+1 : period);
	}

	
}
