package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.functions.types.Indicator;

public class AroonUpDown extends IndicatorBase {

    public AroonUpDown() {
        super(Indicator.AROON, 25);
    }

    public AroonUpDown(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Aroon Up/Down";
    }

    @Override
    public PairArray eval(PriceList list) {
        return aroon(list, getInt(0) );
    }

    private static PairArray aroon(PriceList list, int period) {
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
}
