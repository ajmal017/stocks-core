package org.cerion.stocklist;

import org.cerion.stocklist.data.BandArray;
import org.cerion.stocklist.data.FloatArray;
import org.cerion.stocklist.data.MACDArray;
import org.cerion.stocklist.data.PairArray;
import org.cerion.stocklist.data.ValueArray;
import org.cerion.stocklist.data.VolumeArray;

final class Indicators 
{
	
	private Indicators() {}; //Static class
	
    //TODO, check everything below using Overlays.something, may be better to use PriceList internal function so the result is cached
	//TODO, make functions for common operations, highest high, lowest low, etc
	//TODO, same as above, anything that uses min/max
	//TODO, loops that dont start at 0 or 1
	
	protected static FloatArray rsi(PriceList list, int period)
	{
		FloatArray arr = list.mClose;
		int size = arr.size();
		FloatArray result = new FloatArray(size);

		/* 
        for(int i = period; i < size(); i++)
        {
            float gain = 0;
            float loss = 0;
    
	        for(int j = i-period+1; j <= i; j++)
	        {
	            float diff = get(j).adjClose - get(j-1).adjClose;
	            if(diff > 0)
	                gain += diff;
	            else
	                loss += -diff;
	        }
	        
	        float avgGain = gain / period;
	        float avgLoss = loss / period;
	        float RS = avgGain / avgLoss;

	        if(avgLoss == 0)
	        	get(i).rsi_values[pos] = 100;
	        else if(avgGain == 0)
	        	get(i).rsi_values[pos] = 0;
	        else
	        	get(i).rsi_values[pos] = 100 - (100/(1+RS));
	      
	        //System.out.println( get(i-1).rsi_values[pos] );
        }
        */
        
        //Smoothed RSI, gain/loss calculated slightly different than above
        float gain = 0;
        float loss = 0;

        for(int i = 1; i < (period+1); i++)
        {
            float diff = arr.mVal[i] - arr.mVal[i-1];
            if(diff > 0)
                gain += diff;
            else
                loss += -diff;
        }

        float avgGain = gain / period;
        float avgLoss = loss / period;
        float RS = avgGain / avgLoss;
        result.mVal[period] = 100 - (100/(1+RS));

        //System.out.println(get(period).date + "\t" + get(period).rsi);

        for(int i = (period+1); i < size; i++)
        {
            gain = 0;
            loss = 0;
            float diff = arr.mVal[i] - arr.mVal[i-1];
            if(diff > 0)
                gain = diff;
            else
                loss = -diff;

            avgGain = ((avgGain * (period-1)) + gain) / period;
            avgLoss = ((avgLoss * (period-1)) + loss) / period;

            if(avgLoss == 0)
                result.mVal[i] = 100;
            else if(avgGain == 0)
                result.mVal[i] = 0;
            else
            {
                RS = avgGain / avgLoss;
                result.mVal[i] = 100 - (100/(1+RS));
            }

            //System.out.println(get(i).date + "\t" + get(i).rsi);
        }
        
        return result;
	}
	
	protected static FloatArray stochasticRSI(PriceList list, int period) 
	{
		FloatArray result = new FloatArray(list.size());
		
		FloatArray rsi_arr = list.rsi(period);
		
        for(int i = 0; i < list.size(); i++)
        {
            float high = rsi_arr.get(i);
            float low = rsi_arr.get(i);

            int count = Function.maxPeriod(i, period);
            for(int j = i-count+1; j < i; j++)
            {
                float rsi = rsi_arr.get(j);
                if(rsi > high)
                    high = rsi;
                if(rsi < low)
                    low = rsi;
            }

            //StochRSI = (RSI - Lowest Low RSI) / (Highest High RSI - Lowest Low RSI)
            if(high == low)
            	result.mVal[i] = 1;
            else
            	result.mVal[i] = (rsi_arr.get(i) - low) / (high - low);
        }
		
		return result;
	} 	
	
	protected static FloatArray forceIndex(PriceList list, int period)
	{
		FloatArray close = list.mClose;
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
    	float mult = 2.0f / (1f + period);
    	
    	for(int i = 1; i < size; i++)
    	{
    		//Price p = get(i);
    		//Price prev = get(i-1);

    		float fi = (close.get(i) - close.get(i-1)) * list.mVolume.get(i);
    		result.mVal[i] = (fi - result.get(i-1)) * mult + result.get(i-1);
    		//System.out.println(p.date + "\t" + p.fi);
    	}
    	
    	return result;
	}
	
	protected static VolumeArray onBalanceVolume(PriceList list)
	{
		FloatArray close = list.mClose;
		VolumeArray volume = list.mVolume;
		VolumeArray result = new VolumeArray(list.size());
		
		result.mVal[0] = 0;
		for(int i = 1; i < list.size(); i++)
		{
			if(close.get(i) > close.get(i-1))
				result.mVal[i] = result.get(i-1) + volume.get(i);
			else if(close.get(i) < close.get(i-1))
				result.mVal[i] = result.get(i-1) - volume.get(i);
			else
				result.mVal[i] = result.get(i-1);
		}

		return result;
	}

	protected static FloatArray parabolicSAR(PriceList list, float step, float max) 
	{
		FloatArray result = new FloatArray(list.size());
		FloatArray close = list.mClose;
        int start = 1;

        while(close.get(start-1) == close.get(start))
            start++;

        if(close.get(start-1) > close.get(start))
            SAR_Falling(list, result, start, list.high(start-1), step, max );
        else if(close.get(start-1) < close.get(start))
            SAR_Rising(list, result, start, list.low(start-1), step, max );
        else
            System.out.println("error"); //above should fix this
		
		return result;
	}
	
    private static void SAR_Rising(PriceList list, FloatArray result, int start, float sar_start, float step, float max)
    {
        result.mVal[start] = sar_start;

        float alpha = step;
        float SAR = sar_start;
        float EP = list.high(start);
        
        for(int i = start+1; i < list.size(); i++)
        {
            EP = Math.max(EP, list.high(i));
            if(EP == list.high(i) && (alpha+step) <= max)
                alpha += step;

            if((EP - SAR) < 0)
                System.out.println("SAR_Rising() error");

            SAR = SAR + alpha*(EP - SAR);

            if(SAR > list.low(i))
            {
                SAR_Falling(list, result, i, EP, step, max);
                return;
            }

            result.mVal[i] = SAR;
        }

    }

    private static void SAR_Falling(PriceList list, FloatArray result, int start, float sar_start, float step, float max)
    {
        //System.out.println(p.date + "\t" + sar_start + "\tFalling");
        result.mVal[start] = sar_start;

        float alpha = step;
        float SAR = sar_start;
        float EP = list.low(start);

        for(int i = start+1; i < list.size(); i++)
        {
            EP = Math.min(EP, list.low(i));
            if(EP == list.low(i) && (alpha+step) <= max)
                alpha += step;

            if((SAR - EP) < 0)
                System.out.println("SAR_Falling error");

            SAR = SAR - alpha*(SAR - EP);
            if(SAR < list.high(i))
            {
                SAR_Rising(list, result, i, EP, step, max);
                return;
            }

            result.mVal[i] = SAR;
        }
    }

    protected static FloatArray moneyFlowIndex(PriceList list, int period) 
	{
		FloatArray result = new FloatArray(list.size());
		
    	//Typical Price = (High + Low + Close)/3
    	//Raw Money Flow = Typical Price x Volume
    	//Money Flow Ratio = (14-period Positive Money Flow)/(14-period Negative Money Flow)
    	//Money Flow Index = 100 - 100/(1 + Money Flow Ratio)
    	for(int i = period; i < list.size(); i++)
    	{
    		float posflow = 0;
    		float negflow = 0;
    		for(int j = i-period+1; j <= i; j++)
    		{
    			if(list.tp(j) > list.tp(j-1))
    				posflow += list.tp(j) * list.volume(j);
    			else
    				negflow += list.tp(j) * list.volume(j);
    		}
    		
    		float ratio = posflow / negflow;
    		result.mVal[i] = 100 - (100/(1+ratio));
    	}
    	
    	return result;
	}

	protected static FloatArray accumulationDistributionLine(PriceList list) 
	{
		FloatArray result = new FloatArray(list.size());

		result.mVal[0] = 0;
    	for(int i = 1; i < list.size(); i++)
    	{
    		//ADL = Previous ADL + Current Period's Money Flow Volume
    		result.mVal[i] = result.mVal[i-1] + list.mfv(i);
    	}
    	
    	return result;
	}

	protected static FloatArray chaikinOscillator(PriceList list, int p1, int p2) 
	{
		FloatArray result = new FloatArray(list.size());
		
		FloatArray adl = list.adl();
		FloatArray ema1 = adl.ema(p1);
		FloatArray ema2 = adl.ema(p2);
		
    	for(int i = 0; i < list.size(); i++)
    		result.mVal[i] = ema1.get(i) - ema2.get(i);
    	
		return result;
	}

	protected static FloatArray chaikinMoneyFlow(PriceList list, int period) 
	{
		FloatArray result = new FloatArray(list.size());
		
    	//CMF = N-period Sum of Money Flow Volume / N period Sum of Volume 
    	for(int i = 0; i < list.size(); i++)
    	{
    		int start = i - Function.maxPeriod(i, period) + 1;
    		float mfvolume = 0;
    		float volume = 0;
    		for(int j = start; j <= i; j++)
    		{
    			mfvolume += list.mfv(j);
    			volume += list.volume(j);
    		}
    		
    		result.mVal[i] = mfvolume / volume;
    	}
    	
    	return result;
	}

	protected static FloatArray ultimateOscillator(PriceList list, int p1, int p2, int p3) 
	{
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
	 	float[] bp = new float[size];
    	for(int i = 1; i < size; i++)
    		bp[i] = list.close(i) - Math.min(list.low(i),list.close(i-1));
    	
    	float[][] average = new float[size][3];
    
    	//First Period
    	for(int i = p1; i < size; i++)
    	{
    		float bpsum = 0;
    		float trsum = 0;
    		for(int j = i-p1+1; j <= i; j++)
    		{
    			bpsum += bp[j];
    			trsum += list.tr(j);
    		}
    		average[i][0] = bpsum / trsum;
    		
    		if(trsum == 0)
    			average[i][0] = 0;
    	}
    	
    	//Second Period
    	for(int i = p2; i < size; i++)
    	{
    		float bpsum = 0;
    		float trsum = 0;
    		for(int j = i-p2+1; j <= i; j++)
    		{
    			bpsum += bp[j];
    			trsum += list.tr(j);
    		}
    		average[i][1] = bpsum / trsum;
    	}
    	
    	for(int i = p3; i < size; i++)
    	{
    		float bpsum = 0;
    		float trsum = 0;
    		for(int j = i-p3+1; j <= i; j++)
    		{
    			bpsum += bp[j];
    			trsum += list.tr(j);
    		}
    		average[i][2] = bpsum / trsum;
    	}
    	
    	//Parameters should be ordered lowest to highest, but just in-case
    	int max = Math.max(Math.max(p1,p2), p3);
    	for(int i = max; i < size; i++)
    	{
    		float avg1 = average[i][0];
    		float avg2 = average[i][1];
    		float avg3 = average[i][2];
    		result.mVal[i] = 100 * ((4 * avg1)+(2 * avg2) + avg3)/(4+2+1);
    	}
    	
		return result;
	}

	protected static FloatArray trueStrengthIndex(PriceList list, int p1, int p2) 
	{
		FloatArray result = new FloatArray(list.size());
    	/*
    	-Double Smoothed PC
    	PC = Current Price less Prior Price
    	First Smoothing = 25-period EMA of PC
    	Second Smoothing = 13-period EMA of 25-period EMA of PC

    	-Double Smoothed Absolute PC
    	Absolute Price Change |PC| = Absolute Value of Current Price less Prior Price
    	First Smoothing = 25-period EMA of |PC|
    	Second Smoothing = 13-period EMA of 25-period EMA of |PC|

    	TSI = 100 x (Double Smoothed PC / Double Smoothed Absolute PC)
    	*/
    	
    	float[][] emas = new float[list.size()][4];
    	float mult1 = 2.0f / (1f + p1);
    	float mult2 = 2.0f / (1f + p2);
    	
    	for(int i = 1; i < list.size(); i++)
    	{
    		float pc = list.close(i) - list.close(i-1);
    		if(i > 1)
    		{
    			emas[i][0] = (pc - emas[i-1][0]) * mult1 + emas[i-1][0];
    			emas[i][1] = (Math.abs(pc) - emas[i-1][1]) * mult1 + emas[i-1][1];
    			
    			//second smoothing
    			emas[i][2] = (emas[i][0] - emas[i-1][2]) * mult2 + emas[i-1][2];
    			emas[i][3] = (emas[i][1] - emas[i-1][3]) * mult2 + emas[i-1][3];
    			
    			result.mVal[i] = 100 * (emas[i][2] / emas[i][3]);
    		}
    		else
    		{
    			emas[i][0] = emas[i][2] = pc;
    			emas[i][1] = emas[i][3] = Math.abs(pc);
    		}

    	}
		
    	return result;
	}

	protected static FloatArray massIndex(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
		//Single EMA = 9-period exponential moving average (EMA) of the high-low differential  
		//Double EMA = 9-period EMA of the 9-period EMA of the high-low differential 
		//EMA Ratio = Single EMA divided by Double EMA 
		//Mass Index = 25-period sum of the EMA Ratio 
		
		float mult = 2.0f / (1f + 9); //9-period EMA
		float[][] ema = new float[size][3]; //Single EMA / Double EMA / EMA Ratio
		
		//Single EMA
		ema[0][0] = list.high(0) - list.low(0);
		for(int i = 1; i < size; i++)
		{
			float diff = list.high(i) - list.low(i);
			ema[i][0] = (diff - ema[i-1][0]) * mult + ema[i-1][0];
		}
		
		//Double EMA
		ema[0][1] = ema[0][0];
		for(int i = 1; i < size; i++)
		{
			ema[i][1] = (ema[i][0] - ema[i-1][1]) * mult + ema[i-1][1];

			//Ratio
			ema[i][2] = ema[i][0] / ema[i][1];
		}
		
		//25 period sum
		float total = 0;
		for(int i = 0; i < size; i++)
		{
			total += ema[i][2];
			if(i >= 24)
			{
				result.mVal[i] = total;
				total -= ema[i-24][2];
			}
		}
		
		return result;
	}

	protected static FloatArray commodityChannelIndex(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
		FloatArray tp = new FloatArray(size);
		for(int i = 0; i < size; i++)
			tp.mVal[i] = list.tp(i);
		
		FloatArray sma_arr = tp.sma(period);

		for(int i = 1; i < size; i++)
		{
			float sma = sma_arr.get(i);
			int count = Function.maxPeriod(i, period);
			
			//Mean deviation is different than standard deviation
			float dev = 0;
			for(int j = i-count+1; j <= i; j++)
				dev += Math.abs( list.get(j).tp() - sma);
			dev = dev / count;
	
			//CCI = (Typical Price  -  20-period SMA of TP) / (.015 x Mean Deviation)
			result.mVal[i] = (list.tp(i) - sma) / (0.015f * dev);
		}
		
		return result;
	}

	protected static FloatArray priceMomentumOscillator(PriceList list, int p1, int p2) 
	{
		FloatArray result = new FloatArray(list.size());
		
    	float m1 = 2.0f/p1;
    	float m2 = 2.0f/p2;
    	float ema = 0;
    
    	for(int i = 1; i < list.size(); i++)
    	{
    		float roc = list.roc(i,1);
    		
    		ema = (roc*m1)+(ema*(1-m1));
    		
    		float e = ema * 10;
    		result.mVal[i] = ((e - result.get(i-1)) *m2 ) + result.get(i-1);
    	}
		
		return result;
	}

	protected static FloatArray trix(PriceList list, int period) 
	{
		FloatArray result = new FloatArray(list.size());
		
		FloatArray ema1 = list.mClose.ema(period);
		FloatArray ema2 = ema1.ema(period);
		FloatArray ema3 = ema2.ema(period);
		
		for(int i = 1; i < list.size(); i++)
		{
			//1-Day percent change in Triple EMA
			result.mVal[i] = ((ema3.get(i) - ema3.get(i-1)) / ema3.get(i-1)) * 100;
		}
		
		return result;
	}

	protected static MACDArray macd(PriceList list, int p1, int p2, int signal) 
	{
		MACDArray result = new MACDArray(list.size());
		
		FloatArray ema1 = list.mClose.ema(p1);
		FloatArray ema2 = list.mClose.ema(p2);
		
		for(int i = 0; i < list.size(); i++)
			result.mVal[i] = ema1.get(i) - ema2.get(i);
		
		result.setSignal(signal);
		return result;
	}

	//Volume version of MACD
	protected static MACDArray percentageVolumeOscillator(PriceList list, int p1, int p2, int signal) 
	{
		MACDArray result = new MACDArray(list.size());
		
		VolumeArray ema1 = list.mVolume.ema(p1);
		VolumeArray ema2 = list.mVolume.ema(p2);
		
		for(int i = 0; i < list.size(); i++)
			result.mVal[i] = 100 * ((float)ema1.get(i) - ema2.get(i)) / ema2.get(i);
		
		result.setSignal(signal);
		return result;
	}
	
	//Percentage version of MACD
	protected static MACDArray percentagePriceOscillator(PriceList list, int p1, int p2, int signal) 
	{
		MACDArray result = new MACDArray(list.size());
		
		FloatArray ema1 = list.mClose.ema(p1);
		FloatArray ema2 = list.mClose.ema(p2);
		
		for(int i = 0; i < list.size(); i++)
			result.mVal[i] = 100 * (ema1.get(i) - ema2.get(i)) / ema2.get(i);
		
		result.setSignal(signal);
		return result;
	}

	
	
	protected static FloatArray ulcerIndex(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
        //Percent-Drawdown = ((Close - 14-period Max Close)/14-period Max Close) x 100
        //Squared Average = (14-perod Sum of Percent-Drawdown Squared)/14
        //Ulcer Index = Square Root of Squared Average

        //Set Percent Drawdown
		float[] percentD = new float[size];
        for(int i = 0; i < size; i++)
        {
            float max = 0; //Max close
            int count = Function.maxPeriod(i, period);
            for(int j = i-count+1; j <= i; j++)
                max = Math.max(max, list.close(j));

            percentD[i] = ((list.close(i) - max)/max) * 100;
        }

        for(int i = 0; i < size; i++)
        {
            float avg = 0;
            int count = Function.maxPeriod(i, period);
            for(int j = i-count+1; j <= i; j++)
                avg += percentD[j] * percentD[j]; //Sum of squared

            avg /= period;
            result.mVal[i] = (float) Math.sqrt(avg);
        }
        
		return result;
	}

	protected static FloatArray averageTrueRange(PriceList list, int period) 
	{
		FloatArray result = new FloatArray(list.size());
		
		//Current ATR = [(Prior ATR x 13) + Current TR] / 14
		result.mVal[0] = list.tr(0);
		for(int i = 1; i < list.size(); i++)
			result.mVal[i] = ((result.get(i-1) * (period-1)) + list.tr(i)) / period;
			
		return result;
	}

	protected static FloatArray negativeVolumeIndex(PriceList list) 
	{
		FloatArray result = new FloatArray(list.size());
		
		result.mVal[0] = 1000;
		for(int i = 1; i < list.size(); i++)
		{
			if(list.volume(i) < list.volume(i-1))
				result.mVal[i] = result.mVal[i-1] + list.roc(i, 1);
			else
				result.mVal[i] = result.mVal[i-1];
		
		}
		
		return result;
	}

	/*
    Short-term Daily = KST(10,15,20,30,10,10,10,15)
    Medium-term Weekly = KST(10,13,15,20,10,13,15,20)
    Long-term Monthly = KST(9,12,18,24,6,6,6,9)
    Default signal is 9 period SMA (not EMA)
    */
	protected static FloatArray knowSureThing(PriceList list, int roc1, int roc2, int roc3, int roc4, int sma1, int sma2, int sma3, int sma4) 
	{
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
		FloatArray r1 = new FloatArray(size);
		FloatArray r2 = new FloatArray(size);
		FloatArray r3 = new FloatArray(size);
		FloatArray r4 = new FloatArray(size);
		
		/*
		RCMA1 = 10-Period SMA of 10-Period Rate-of-Change 
		RCMA2 = 10-Period SMA of 15-Period Rate-of-Change 
		RCMA3 = 10-Period SMA of 20-Period Rate-of-Change 
		RCMA4 = 15-Period SMA of 30-Period Rate-of-Change
		KST = (RCMA1 x 1) + (RCMA2 x 2) + (RCMA3 x 3) + (RCMA4 x 4)  
		*/
		for(int i = 0; i < size; i++)
		{
			r1.mVal[i] = list.roc(i, roc1);
			r2.mVal[i] = list.roc(i, roc2);
			r3.mVal[i] = list.roc(i, roc3);
			r4.mVal[i] = list.roc(i, roc4);
		}
		
		//Apply SMA to arrays
		r1 = r1.sma(sma1);
		r2 = r2.sma(sma2);
		r3 = r3.sma(sma3);
		r4 = r4.sma(sma4);
		
		for(int i = 0; i < size; i++)
			result.mVal[i] = r1.get(i) + (r2.get(i) * 2) + (r3.get(i) * 3) + (r4.get(i) * 4);
		
		return result;
	}

	protected static FloatArray specialK(PriceList list) 
	{
		FloatArray result = new FloatArray(list.size());
		
		/*
		Special K = 10 Period Simple Moving Average of ROC(10) * 1
	            + 10 Period Simple Moving Average of ROC(15) * 2
	            + 10 Period Simple Moving Average of ROC(20) * 3
	            + 15 Period Simple Moving Average of ROC(30) * 4
	            
	            + 50 Period Simple Moving Average of ROC(40) * 1
	            + 65 Period Simple Moving Average of ROC(65) * 2
	            + 75 Period Simple Moving Average of ROC(75) * 3
	            +100 Period Simple Moving Average of ROC(100)* 4
	            
	            +130 Period Simple Moving Average of ROC(195)* 1
	            +130 Period Simple Moving Average of ROC(265)* 2
	            +130 Period Simple Moving Average of ROC(390)* 3
	            +195 Period Simple Moving Average of ROC(530)* 4
	    */
		
		//This is just 3 different versions of knowSureThing so it can be calculated easy
		FloatArray kst1 = list.kst(10, 15, 20, 30,     10, 10, 10, 15);
		FloatArray kst2 = list.kst(40, 65, 75, 100,    50, 65, 75, 100);
		FloatArray kst3 = list.kst(195, 265, 390, 530, 130, 130, 130, 195);
		
		for(int i = 0; i < list.size(); i++)
			result.mVal[i] = kst1.get(i) + kst2.get(i) + kst3.get(i);
		
		return result;
	}

   
	
	//1-period EMV
	private static FloatArray easeOfMovement(PriceList list)
	{
		FloatArray result = new FloatArray(list.size());
		
    	//Distance Moved = ((H + L)/2 - (Prior H + Prior L)/2) 
    	//Box Ratio = ((V/100,000,000)/(H - L))
    	//1-Period EMV = dm / box
    	for(int i = 1; i < list.size(); i++)
    	{
    		float diff = list.high(i) - list.low(i); //Need to divide by this
    		if(diff == 0)
    			diff = 0.01f;
    		
    		float dm = ((list.high(i) + list.low(i))/2 - (list.high(i-1) + list.low(i-1))/2);
    		float box = ((list.volume(i)/100000.0f)/diff); //Volume is already divided by 1000 so removing 2 digits here
    		result.mVal[i] = dm / box;
    		if(box == 0)
    			result.mVal[i] = 0;
    	}
		
		return result;
	}
	
	//TODO, double check results again with current data
	protected static FloatArray easeOfMovement(PriceList list, int period) 
	{
		FloatArray result = new FloatArray(list.size());
		
    	//N-Period Ease of Movement = N-Period simple moving average of 1-period EMV
    	FloatArray emv = easeOfMovement(list);

    	for(int i = 1; i < list.size(); i++)
    	{
    		int count = Function.maxPeriod(i, period);
    		float total = 0;
    		for(int j = i-count+1; j <= i; j++)
    			total += emv.get(j);
    		
    		result.mVal[i] = total / count;
    	}
		
		return result;
	}

	protected static BandArray keltnerChannel(PriceList list, int emaPeriod, float multiplier, int atrPeriod) {
		//Middle Line: 20-day exponential moving average 
		//Upper Channel Line: 20-day EMA + (2 x ATR(10))
		//Lower Channel Line: 20-day EMA - (2 x ATR(10))
		return new BandArray(list.mClose, multiplier, list.ema(emaPeriod), list.atr(atrPeriod));
	}

	protected static FloatArray williamsPercentR(PriceList list, int period) {
		FloatArray result = new FloatArray(list.size());
		
		//%R = (Highest High - Close)/(Highest High - Lowest Low) * -100
		for(int i = 0; i < list.size(); i++) {
			float h = list.high(i);
			float l = list.low(i);
			
			int count = ValueArray.maxPeriod(i,period);
			for(int j = i-count+1; j < i; j++)
			{
				h = Math.max(h, list.high(j));
				l = Math.min(l, list.low(j));
			}
			
			result.mVal[i] = (h - list.close(i)) / (h - l) * -100;
		}
		
		return result;
	}

	protected static PairArray vortex(PriceList list, int period) {

		int size = list.size();
		FloatArray posVI = new FloatArray(size);
		FloatArray negVI = new FloatArray(size);
		
		float[][] vm = new float[size][2]; // +VM/-VM
		
		for(int i = 1; i < size; i++)
		{
			vm[i][0] = Math.abs(list.high(i) - list.low(i-1));
			vm[i][1] = Math.abs(list.low(i) - list.high(i-1));
		}
		
		//TODO set values lower than this
		for(int i = period * 2; i < size; i++)
		{
			float vip = 0;
			float vin = 0;
			float tr = 0;
			for(int j = i - period + 1; j <= i; j++)
			{
				vip += vm[j][0];
				vin += vm[j][1];
				tr += list.tr(j);
			}
			
			posVI.mVal[i] = vip / tr;
			negVI.mVal[i] = vin / tr;
		}
		
		return new PairArray(posVI, negVI);
	}


	protected static PairArray aroon(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray up = new FloatArray(size);
		FloatArray down = new FloatArray(size);
    	//Aroon Up = 100 x (25 - Days Since 25-day High)/25
    	//Aroon Down = 100 x (25 - Days Since 25-day Low)/25
    	//Aroon Oscillator = Aroon-Up  -  Aroon-Down

    	for(int i = period-1; i < size; i++)
    	{
    		int high = i - list.mClose.maxPos(i-period+1,i) + 1;
    		int low = i - list.mClose.minPos(i-period+1,i) +1;
    		
    		up.mVal[i] = (100 * (period - high))/period;
    		down.mVal[i] = (100 * (period - low))/period;
    	}

    	return new PairArray(up,down);
	}

	protected static FloatArray stochastic(PriceList list, int K) 
	{
		FloatArray result = new FloatArray(list.size());
	
		//K = period
        for(int i = K-1; i < list.size(); i++)
        {
            float high = list.highestHigh(i-K+1, i);
            float low = list.lowestLow(i-K+1, i);

            //K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
            result.mVal[i] = ((list.close(i) - low)/(high-low))*100;
        }
        
        return result;
	}
    
	protected static FloatArray stochastic(PriceList list, int K, int D) 
	{
		FloatArray result = stochastic(list,K);
		return result.sma(D);
	}
	
	protected static FloatArray stochastic(PriceList list, int K, int fastD, int slowD) 
	{
		FloatArray result = stochastic(list,K,fastD);
		return result.sma(slowD);
	}

	protected static PairArray directionalIndex(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray mDI = new FloatArray(size); //-DI
		FloatArray pDI = new FloatArray(size); //+DI
		
		float[][] trdm = new float[size][2]; //+DM / -DM
		
		for(int i = 1; i < size; i++) 
		{
			int curr = i;
			int prev = i-1;

			//TODO, add DM function to PriceList so this can be calculated directly
			if(list.high(curr) - list.high(prev) > list.low(prev) - list.low(curr))
				trdm[i][0] = Math.max( list.high(curr) - list.high(prev), 0);
			
			if(list.low(prev) - list.low(curr) > list.high(curr) - list.high(prev))
				trdm[i][1] = Math.max(list.low(prev) - list.low(curr),0);
		}
		
		float[][] trdm14 = new float[size][3]; //TR14 / +DM14 / -DM14

		for(int i = 1; i < size; i++)
		{
			trdm14[i][0] = trdm14[i-1][0] - (trdm14[i-1][0]/14) + list.tr(i);
			trdm14[i][1] = trdm14[i-1][1] - (trdm14[i-1][1]/14) + trdm[i][0];
			trdm14[i][2] = trdm14[i-1][2] - (trdm14[i-1][2]/14) + trdm[i][1];
			
			pDI.mVal[i] = 100 * (trdm14[i][1] / trdm14[i][0]);
			mDI.mVal[i] = 100 * (trdm14[i][2] / trdm14[i][0]);
		}
		

		return new PairArray(pDI, mDI);
	}

	protected static FloatArray averageDirectionalIndex(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray result = new FloatArray(size);
		
		PairArray di = list.di(period);
		float[] dx = new float[size]; 
		
		for(int i = 1; i < size; i++)
		{
			int count = ValueArray.maxPeriod(i, period);
			//Directional Movement Index (DX) equals the absolute value of +DI minus -DI divided by the sum of +DI and -DI. 
			float diff = di.getPos(i) - di.getNeg(i);
			float sum = di.getPos(i) + di.getNeg(i);
			dx[i] = 100 * (Math.abs(diff) / sum);
			
			result.mVal[i] = ((result.get(i-1)*(count-1)) + dx[i]) / count;
		}
		

		return result;
	}

	//TODO check again can't get results to match
	protected static PairArray chandelierExit(PriceList list, int period, float multiplier) 
	{
		int size = list.size();
		
		FloatArray high = new FloatArray(size);
		FloatArray low = new FloatArray(size);
		FloatArray atr = list.atr(period);
		
		for(int i = period; i < size; i++)
		{
			float h = list.highestHigh(i-period+1, i);
			float l = list.lowestLow(i-period+1, i);

			high.mVal[i] = h - (atr.get(i) * multiplier);
			low.mVal[i] = l + (atr.get(i) * multiplier);
		}
		
		return new PairArray(high, low);
	}

	protected static BandArray priceChannels(PriceList list, int period) 
	{
		int size = list.size();
		FloatArray upper = new FloatArray(size);
		FloatArray lower = new FloatArray(size);
		
		for(int i = period; i < size; i++)
		{
			upper.mVal[i] = list.highestHigh(i-period, i-1);
			lower.mVal[i] = list.lowestLow(i-period, i-1);
		}
		
		return new BandArray(upper, lower);
	}

	protected static PairArray ichimokuCloud(PriceList list, int conversion, int base, int span) 
	{
		int size = list.size();
		FloatArray spanA = new FloatArray(size);
		FloatArray spanB = new FloatArray(size);
		
		for(int i = span; i < size; i++)
		{
			//Conversion Line
			float high = list.highestHigh(i-conversion+1, i);
			float low  = list.lowestLow(i-conversion+1, i);
			float conversionLine = (high + low) / 2; 
			
			//Base line
			high = list.highestHigh(i-base+1, i);
			low  = list.lowestLow(i-base+1, i);
			float baseLine = (high + low) / 2; 
			
			//Leading Span A
			spanA.mVal[i] = (conversionLine + baseLine) / 2;
			
			//Leading Span B
			high = list.highestHigh(i-span+1, i);
			low  = list.lowestLow(i-span+1, i);
			spanB.mVal[i] = (high + low) / 2;
		}
		
		return new PairArray(spanA,spanB);
	}
    
}
