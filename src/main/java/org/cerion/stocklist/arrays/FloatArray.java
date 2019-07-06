package org.cerion.stocklist.arrays;

import org.cerion.stocklist.overlays.BollingerBands;
import org.cerion.stocklist.overlays.ExpMovingAverage;
import org.cerion.stocklist.overlays.LinearRegressionLine;
import org.cerion.stocklist.overlays.SimpleMovingAverage;

public class FloatArray extends ValueArray {

	public float[] mVal;
	
	@Override
	public int getSize() { return mVal.length; }

	public FloatArray(int size) {
		mVal = new float[size];
	}

	// TODO make mVal private and use array notation directly on object
	public void set(int i, float value) {
		mVal[i] = value;
	}

	public float get(int pos)
	{
		return mVal[pos];
	}
	
	public float getFirst()
	{
		return mVal[0];
	}

	public float getLast()
	{
		return mVal[mVal.length - 1];
	}

	/**
	 * Find the highest value in the range [start,end]
	 * @param start first position to start looking
	 * @param end last position to look
	 * @return the maximum value in the range
	 */
	public float max(int start, int end) {
        return mVal[maxPos(start, end)];
	}

	/**
	 * Find the lowest value in the range [start,end]
	 * @param start first position to start looking
	 * @param end last position to look
	 * @return the minimum value in the range
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
	
	public FloatArray sma(int period) {
		return new SimpleMovingAverage(period).eval(this);
	}
	
	public FloatArray ema(int period) {
		return new ExpMovingAverage(period).eval(this);
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
	 * @param period The period to use for the average
	 * @return standard deviation of the average in period
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

	public BandArray bb(int period, float multiplier) {
		return new BollingerBands(period, multiplier).eval(this);
	}

	public float slope(int period, int pos) {
		period = Companion.maxPeriod(pos,period);
		float[] ab = getLinearRegressionEquation(pos - period + 1, pos);

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

	public float regressionLinePoint(int period, int pos) {
		int count = Companion.maxPeriod(pos, period);
		float slope = slope(period, pos);
		float sumY = sum(pos - count + 1, pos);

		return (sumY - (slope * count)) / count;
	}

	public FloatArray linearRegressionLine() {
		return new LinearRegressionLine().eval(this);
	}

	/**
	 * Finds linear regression equation "y = a + bx" for arr with start and end point positions
	 * @return pair [a,b]
	 */
	public float[] getLinearRegressionEquation(int start, int end) {
		// http://www.statisticshowto.com/how-to-find-a-linear-regression-equation/
		// TODO check this on fake data like a straight line to verify any 1 off issues
		int count = end - start + 1;

		if(count == 1)
			return new float[] { mVal[start], 0 };

		float sumY = 0;
		float sumX = 0;
		float sumXsquared = 0;
		float sumXY = 0;

		int x = 1;
		for(int i = start; i <= end; i++) {
			float y = mVal[i];

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
}
