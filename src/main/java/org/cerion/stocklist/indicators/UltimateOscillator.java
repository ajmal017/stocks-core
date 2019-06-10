package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class UltimateOscillator extends IndicatorBase {

    public UltimateOscillator() {
        super(Indicator.UO, 7, 14, 28);
    }

    public UltimateOscillator(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Ultimate Oscillator";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return ultimateOscillator(list, getInt(0), getInt(1), getInt(2) );
    }

    private static FloatArray ultimateOscillator(PriceList list, int p1, int p2, int p3) {
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

}
