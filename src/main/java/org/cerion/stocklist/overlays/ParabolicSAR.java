package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.PriceOverlay;

public class ParabolicSAR extends PriceOverlayBase {

    public ParabolicSAR() {
        super(PriceOverlay.PSAR, 0.02, 0.2);
    }

    public ParabolicSAR(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public FloatArray eval(PriceList list) {
        return parabolicSAR(list, getFloat(0), getFloat(1));
    }

    @Override
    public String getName() {
        return "Parabolic SAR";
    }

    private static FloatArray parabolicSAR(PriceList list, float step, float max)
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
}
