package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class RSI extends IndicatorBase {

    public RSI() {
        super(Indicator.RSI, 14);
    }

    public RSI(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "RSI";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return rsi(list, getInt(0) );
    }


    private static FloatArray rsi(PriceList list, int period)
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
		/*
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
        */

        //System.out.println(get(period).date + "\t" + get(period).rsi);

        // Start in middle range with averages as the first difference
        result.mVal[0] = 50;
        float avgGain = Math.abs(arr.mVal[1] - arr.mVal[0]);
        float avgLoss = avgGain;

        for(int i = 1; i < size; i++) {
            float gain = 0;
            float loss = 0;
            int p = ValueArray.maxPeriod(i, period);

            float diff = arr.mVal[i] - arr.mVal[i-1];
            if(diff > 0)
                gain = diff;
            else
                loss = -diff;

            avgGain = ((avgGain * (p-1)) + gain) / p;
            avgLoss = ((avgLoss * (p-1)) + loss) / p;

            if(avgLoss == 0)
                result.mVal[i] = 100;
            else if(avgGain == 0)
                result.mVal[i] = 0;
            else
            {
                float RS = avgGain / avgLoss;
                result.mVal[i] = 100 - (100/(1+RS));
            }

            //System.out.println(get(i).date + "\t" + get(i).rsi);
        }

        return result;
    }
}
