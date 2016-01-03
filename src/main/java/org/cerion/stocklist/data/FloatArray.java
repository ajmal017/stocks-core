package org.cerion.stocklist.data;

public class FloatArray extends ValueArray {

	public float[] mVal;
	
	@Override
	public int size() { return mVal.length; }
	
	public FloatArray(int size) {
		mVal = new float[size];
	}
	
	public float get(int pos)
	{
		return mVal[pos];
	}
	

	//TODO, find cases in Indic
	
	
	/*
	 * return max value between [start,end]
	 */
	public float max(int start, int end) //TODO, use maxPos
	{
		float max = mVal[start];
        for(int i = start+1; i <= end; i++)
            max = Math.max(mVal[i], max);
        
        return max;
	}
	
	/*
	 * return min value between [start,end]
	 */
	public float min(int start, int end) //TODO, use use minPos
	{
		float min = mVal[start];
        for(int i = start+1; i <= end; i++)
            min = Math.min(mVal[i], min);
        
        return min;
	}
	
	/**
	 * Returns position of max value between [start,end]
	 */
	public int maxPos(int start, int end)
	{
		int max = start;
		for(int i = start+1; i <= end; i++)
		{
			if(get(i) > get(max))
				max = i;
		}
		return max;
	}
	
	/**
	 * Returns position of max value between [start,end]
	 */
	public int minPos(int start, int end)
	{
		int min = start;
		for(int i = start+1; i <= end; i++)
		{
			if(get(i) < get(min))
				min = i;
		}
		return min;
	}
	
	public FloatArray sma(int period)
	{
		int size = this.size();
		FloatArray result = new FloatArray(size);
	
		/*
		float total = 0;
		for(int i = 0; i < size; i++)
		{
			total += arr.mVal[i];
			if(i >= period-1)
			{
				result.mVal[i] = total / period;
				total -= arr.mVal[i-period+1];
			}
			else
				result.mVal[i] = total / (i+1);
		}
		*/
		
		//Some slight float rounding errors above, look into it later
		//This seems more accurate
		for(int i = 0; i < size; i++)
		{
			//Take average of first i array elements when count is less than period size
			int count = maxPeriod(i,period);
			
			float total = 0;
			for(int j = i-count+1; j <= i; j++)
				total += mVal[j];
			
			result.mVal[i] = total / count;
			
		}
		
		return result;
	}
	
	public FloatArray ema(int period)
	{
		int size = size();
		FloatArray result = new FloatArray(size);
		float mult = 2.0f / (1f + period); //EMA multiplier
	
		result.mVal[0] = mVal[0]; //initialize with first value
		for(int i = 1; i < size; i++)
			result.mVal[i] = (mVal[i] - result.mVal[i-1]) * mult + result.mVal[i-1];

		return result;
	}
	
	
	public FloatArray std(int period)
	{
		return std(period, sma(period));
	}
	
	//If the SMA is available this function can be called directly to avoid re-calculating
	public FloatArray std(int period, FloatArray arr_sma)
	{
		int size = size();
		FloatArray result = new FloatArray(size);

		//TODO, start loop at 0 and assign some value to every position
		for(int i = period-1; i < size; i++)
		{
			float sma = arr_sma.mVal[i];
			float total = 0;
			
			for(int j = i-period+1; j <= i; j++)
			{
				float diff = mVal[j] - sma;
				total += diff*diff;
			}
		
			result.mVal[i] = (float) Math.sqrt( total / period );
		}

		return result;
	}
	
	/**
	 * Bollinger band
	 * @param period
	 * @param multiplier
	 * @return
	 */
	public BandArray bb(int period, float multiplier)
	{
		return new BandArray(this,period,multiplier);
	}
	
	public float slope(int period, int pos)
	{
		//Following formula from http://austingwalters.com/introduction-to-linear-regression/
		float sumX = 0;    
		float sumY = 0;
		float sumXY = 0;    //Sum of x*y
		float sumX2 = 0;    //Sum(x^2)
		
		int count = maxPeriod(pos,period);
		for(int j = pos - count + 1; j <= pos; j++)
		{
			float y = mVal[j];
			
			sumX += j;
			sumY += y;
			sumXY += (j * y);
			sumX2 += (j * j);
		}
		
		//Numerator
		float slope = (sumX * sumY) / count;
		slope = sumXY - slope;
		
		//Denominator
		float temp = (sumX * sumX) / count;
		temp = sumX2 - temp;
		
		if(temp == 0)
			slope = 0;
		else
			slope = slope / temp;
		
		return slope;
	}
	
	
	
	
	//------------ Moved from PriceList, for calculating Beta
	// Possibly able to extract some useful functions from here like variance
	
	/*
	public float variance(int start, int length)
	{
		float result = 0;
		float avg = average(start,length);
		
		for(int i = start; i > (start - length); i--)
		{
			Price p = get(i);
			Price prev = get(i-1);
			float s = p.getPercentDiff(prev) - avg;
			result += (s * s);
		}
		
		return (result / length);
	}
	
	public float average(int start, int length)
	{
		float result = 0;
		for(int i = start; i > (start - length); i--)
		{
			Price p = get(i);
			Price prev = get(i-1);
			result += p.getPercentDiff(prev);
		}
		
		return (result / length);
	}
	
	@Deprecated
	public float getBeta(PriceList index, int start, int length)
	{
		if(get(0).date.equals(index.get(0).date) == false)
		{
			System.out.println("getBeta() start dates do not match");
			return 0;
		}
		
	    float result = covar(index,start,length);
	    result /= index.variance(start,length);
	    
	    return result;
	    
	}
	
	public float covar(PriceList b, int start, int length)
	{
		float result = 0;
		float avg_a = average(start,length);
		float avg_b = b.average(start,length);
		
		for(int i = start; i > (start - length); i--)
		{
			Price p1 = get(i);
			Price p1prev = get(i-1);
			Price p2 = b.get(i);
			Price p2prev = b.get(i-1);
			
			if(!p1.date.equals(p2.date))
				System.out.println("dates do not match");
			
			float s = p1.getPercentDiff(p1prev) - avg_a;
			float t = p2.getPercentDiff(p2prev) - avg_b;
			result += (s * t);
		}
		
		return (result / length);
	}
	*/
	
	
}
