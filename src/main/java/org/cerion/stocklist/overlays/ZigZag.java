package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.PriceOverlay;

public class ZigZag extends PriceOverlayBase {

    public ZigZag() {
        super(PriceOverlay.ZIGZAG, 5.0);
    }

    public ZigZag(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public FloatArray eval(PriceList list) {
        return zigzag(list, getFloat(0));
    }

    @Override
    public String getName() {
        return "ZigZag";
    }

    private static float zz_p(float curr, float prev) {
        return 100 * (curr - prev) / prev;
    }
    private FloatArray zigzag(PriceList list, float percent) {
        int size = list.size();
        FloatArray result = new FloatArray(list.size());

        // start=-1, down=0, up=1
        int direction = -1;
        int currPos = 0;

        for(int i = 0; i < list.size(); i++) {
            float high = list.high(i);
            float low = list.low(i);
            float currHigh = list.high(currPos);
            float currLow = list.low(currPos);

            if(direction == -1) {
                if(zz_p(high, currLow) > percent) {
                    direction = 1;
                    currPos = i;
                    result.mVal[0] = currLow;
                }
                else if(zz_p(low, currHigh) < -percent) {
                    direction = 0;
                    currPos = i;
                    result.mVal[0] = currHigh;
                }
            }
            else if(direction == 0) {

                if(low < currLow)
                    currPos = i;
                else if(zz_p(high, currLow) > percent) {
                    result.mVal[currPos] = currLow;
                    direction = 1;
                    currPos = i;
                }
            }
            else {
                if(high > currHigh)
                    currPos = i;
                else if(zz_p(low, currHigh) < -percent) {
                    result.mVal[currPos] = currHigh;
                    direction = 0;
                    currPos = i;
                }
            }
        }

        // Add current position as the last point
        if(direction == 0)
            result.mVal[currPos] = list.low(currPos);
        else if(direction == 1)
            result.mVal[currPos] = list.high(currPos);

        // Add last point as the reverse direction
        int last = size - 1;
        if(direction == 0)
            result.mVal[last] = list.high(last);
        else if(direction == 1)
            result.mVal[last] = list.low(last);

        // Fix by adding straight lines to connect points
        int start = 0;
        int end;
        for(int i = 1; i < size; i++) {
            if(result.get(i) != 0) {
                end = i;
                float a = result.get(start);
                float b = result.get(end);
                float inc = (b - a) / (end - start);

                for(int j = start + 1; j < end; j++) {
                    result.mVal[j] = result.mVal[j-1] + inc;
                }

                start = end;
            }
        }

        return result;
    }
}
