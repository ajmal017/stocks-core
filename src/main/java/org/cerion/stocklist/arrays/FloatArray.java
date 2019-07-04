package org.cerion.stocklist.arrays;

import org.cerion.stocklist.overlays.BollingerBands;
import org.cerion.stocklist.web.CombinedDataAPI;

public class FloatArray extends ValueArray {

	public float[] mVal;
	
	@Override
	public int getSize() { return mVal.length; }

	public FloatArray(int size) {
		mVal = new float[size];
	}
	
	public float get(int pos)
	{
		return mVal[pos];
	}
	
	public float first()
	{
		return mVal[0];
	}

	public float last()
	{
		return mVal[mVal.length - 1];
	}

	/**
	 * Find the highest value in the range [start,end]
	 * @param start
	 * @param end
	 * @return
	 */
	public float max(int start, int end) {
        return mVal[maxPos(start, end)];
	}

	/**
	 * Find the lowest value in the range [start,end]
	 * @param start
	 * @param end
	 * @return
	 */
	public float min(int start, int end) {
        return mVal[minPos(start,end)];
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

	/**
	 * Sum of inclusive range
	 */
	public float sum(int start, int end) {
		float sum = 0;
		for(int i = start; i <= end; i++) {
			sum += get(i);
		}

		return sum;
	}
	
	public FloatArray sma(int period)
	{
		int size = this.size();
		FloatArray result = new FloatArray(size);

		//Some slight float rounding errors above, look into it later
		//This seems more accurate
		for(int i = 0; i < size; i++)
		{
			//Take average of first i array elements when count is less than period size
			int count = Companion.maxPeriod(i,period);
			
			float total = 0;
			for(int j = i-count+1; j <= i; j++)
				total += mVal[j];
			
			result.mVal[i] = total / count;
			
		}
		
		return result;
	}
	
	public FloatArray ema(int period) {
		int size = size();
		FloatArray result = new FloatArray(size);

		if(size > 0) {
			float mult = 2.0f / (1f + period); //ExpMovingAverage multiplier

			result.mVal[0] = mVal[0]; //initialize with first value
			for (int i = 1; i < size; i++)
				result.mVal[i] = (mVal[i] - result.mVal[i - 1]) * mult + result.mVal[i - 1];
		}

		return result;
	}

	/**
	 * Converts current array values to log(val)
	 * @return FloatArray to log scale
	 */
	public FloatArray log() {
		FloatArray result = new FloatArray(size());
		for(int i = 0; i < size(); i++)
			result.mVal[i] = (float)Math.log(get(i));

		return result;
	}

	/**
	 * Standard deviation of period
	 * @param period
	 * @return
	 */
	public FloatArray std(int period) {
		return std(period, sma(period));
	}
	
	//If the SimpleMovingAverage is available this function can be called directly to avoid re-calculating
	public FloatArray std(int period, FloatArray arr_sma) {
		int size = size();
		FloatArray result = new FloatArray(size);

		for(int i = 1; i < size; i++) {
			int count = Companion.maxPeriod(i, period);

			float sma = arr_sma.mVal[i];
			float total = 0;

			for(int j = i-count+1; j <= i; j++) {
				float diff = mVal[j] - sma;
				total += diff*diff;
			}

			result.mVal[i] = (float) Math.sqrt( total / count );
		}

		return result;
	}
	
	/**
	 * Bollinger band
	 * @param period
	 * @param multiplier
	 * @return
	 */
	public BandArray bb(int period, float multiplier) {
		return new BollingerBands(period, multiplier).eval(this);
	}

	public float slope(int period, int pos) {
		period = Companion.maxPeriod(pos,period);
		float[] ab = getLinearRegressionEquation(mVal, pos - period + 1, pos);

		return ab[1];
	}

	/**
	 * Calculates percent difference between each entry
	 * @return array of percent change from previous element
	 */
	public FloatArray getPercentChange() {
		FloatArray arr = new FloatArray(size());

		for(int i = 1; i < size(); i++)
			arr.mVal[i] = (mVal[i] - mVal[i-1])/mVal[i-1];

		return arr;
	}

	public float getPercentChange(int index) {
		return (mVal[size() - 1] - mVal[index])/mVal[index];
	}

	/*
	public float slope2(int period, int pos) {
		//Following formula from http://austingwalters.com/introduction-to-linear-regression/
		float sumX = 0;    
		float sumY = 0;
		float sumXY = 0;    //Sum of x*y
		float sumX2 = 0;    //Sum(x^2)
		
		int count = maxPeriod(pos,period);
		int i = 0;
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
	*/

	public float regressionLinePoint(int period, int pos) {
		int count = Companion.maxPeriod(pos, period);
		float slope = slope(period, pos);
		float sumY = sum(pos - count + 1, pos);

		return (sumY - (slope * count)) / count;
	}

	public FloatArray linearRegressionLine() {
		int count = size();
		int pos = count - 1;
		FloatArray result = new FloatArray(size());

		float[] ab = getLinearRegressionEquation(mVal, 0, pos);
		float slope = ab[1];
		result.mVal[0] = ab[0];

		for(int i = 1; i < size(); i++) {
			result.mVal[i] = result.mVal[i-1] + slope;
		}

		return result;
	}

	/**
	 * Finds linear regression equation "y = a + bx" for arr with start and end point positions
	 * @return pair [a,b]
	 */
	private static float[] getLinearRegressionEquation(float[] arr, int start, int end) {
		// http://www.statisticshowto.com/how-to-find-a-linear-regression-equation/
		// TODO check this on fake data like a straight line to verify any 1 off issues
		int count = end - start + 1;

		if(count == 1)
			return new float[] { arr[start], 0 };

		float sumY = 0;
		float sumX = 0;
		float sumXsquared = 0;
		float sumXY = 0;

		int x = 1;
		for(int i = start; i <= end; i++) {
			float y = arr[i];

			sumY += y;
			sumX += x;
			sumXsquared += x * x;
			sumXY += x * y;
			x++;
		}

		// Both variables are calculated by n(Sum x^2) - (Sum x)^2
		float divideBy = (count * sumXsquared) - (sumX * sumX);

		float a = (sumY * sumXsquared) - (sumX * sumXY);
		float b = (count * sumXY) - (sumX * sumY);

		return new float[] { a / divideBy, b / divideBy };
	}

	public FloatArray kama(int p1, int p2, int p3)
	{
		FloatArray result = new FloatArray(size());
		result.mVal[0] = get(0);
		
		//p1 Efficiency Ratio (ER)
		//p2 Fastest ExpMovingAverage
		//p3 Slowest ExpMovingAverage
		
		//ER = Change/Volatility
		//Change = ABS(Close - Close (X periods ago))
		//Volatility = SumX(ABS(Close - Prior Close))
		for(int i = 1; i < size(); i++)
		{
			int start = i - p1;
			if(start < 1)
				start = 1;
			
			float change = Math.abs(get(i) - get(start));
			float volatility = 0;
			
			//SumX
			for(int j = start; j <= i; j++)
				volatility += Math.abs(get(j) - get(j-1));
			
			float ER = change / volatility;
			
			//SC = [ER x (fastest SC - slowest SC) + slowest SC]^2
			//SC = [ER x (2/(2+1) - 2/(30+1)) + 2/(2+1)]^2
			float fastEMA = (float) (2.0 / (p2 + 1.0));
			float slowEMA = (float) (2.0 / (p3 + 1.0));
			float SC = ER * (fastEMA - slowEMA) + slowEMA;
			SC *= SC;

			//Current KAMA = Prior KAMA + SC x (Price - Prior KAMA)
			float prior = result.mVal[i-1];
			result.mVal[i] = prior + SC * (get(i) - prior);
		}
		
		return result;
	}

	public FloatArray line(float slope) {
		FloatArray result = new FloatArray(size());
		result.mVal[0] = mVal[0];
		for(int i = 1; i < size(); i++) {
			result.mVal[i] = result.mVal[i-1] + slope;
		}

		return result;
	}

	public float correlation(FloatArray arr) {
		float size = Math.min(size(), arr.size());

		float sumXX = 0, sumX = 0;
		float sumYY = 0, sumY = 0;
		float sumXY = 0;

		for(int i = 0; i < size; i++) {
			float x = get(size() - 1 - i);
			sumX += x;
			sumXX += x * x;

			float y = arr.get(arr.size() - 1 - i);
			sumY += y;
			sumYY += y * y;

			sumXY += x * y;
		}

		float Sxx = sumXX - (sumX * sumX) / size;
		float Syy = sumYY - (sumY * sumY) / size;
		float Sxy = sumXY - (sumX * sumY) / size;

		return Sxy / (float)Math.sqrt(Sxx * Syy);
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

	@Override
	public String toString() {
		String result = "{";

		for(int i = 0; i < size() && i < 5; i++) {
			result += get(i) + ", ";
		}

		result += "...}";
		return result;
	}

	// TODO make mVal private and use array notation directly on object
	public void set(int i, float value) {
		mVal[i] = value;
	}
}
