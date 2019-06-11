package org.cerion.stocklist.charts;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.*;
import org.cerion.stocklist.functions.types.IFunctionEnum;
import org.cerion.stocklist.functions.types.PriceOverlay;
import org.cerion.stocklist.model.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PriceChart extends StockChart {

    public boolean candleData = true;
    public boolean showPrice = true;
    public boolean logScale = false;

    @Override
    public IFunctionEnum[] getOverlays() {
        List<IFunctionEnum> overlay = Arrays.asList(super.getOverlays());
        List<PriceOverlay> priceOverlay = Arrays.asList(PriceOverlay.values());

        List<IFunctionEnum> combined = new ArrayList<IFunctionEnum>();
        combined.addAll(overlay);
        combined.addAll(priceOverlay);

        return combined.toArray(new IFunctionEnum[] {});
    }

    public void addOverlay(IOverlay overlay) {
        mOverlays.add(overlay);
    }

    @Override
    public List<IDataSet> getDataSets() {
        List<IDataSet> result = new ArrayList<IDataSet>();

        if(!showPrice) {
            // Don't add price data
        } else if(candleData && canShowCandleData()) {
            result.addAll(Arrays.asList(new CandleDataSet(getPriceList(logScale), "Price", colorBlack())) );
        } else {
            result.addAll(Arrays.asList(new DataSet(getPriceList(logScale).getClose(), "Price", colorBlack())) );
        }

        result.addAll(getOverlayDataSets());

        return result;
    }

    /**
     * Determines if this chart is able to display candle data, mutual funds on daily interval don't have high/low variation so candles shouldn't be used
     * @return true if this chart can display candle data properly
     */
    public boolean canShowCandleData() {
        PriceList list = getPriceList();
        if(list == null)
            return false;

        // Only daily has this problem with high/low values
        if(list.getInterval() != Interval.DAILY)
            return true;

        for(int i = 0; i < list.size(); i++) {
            if (list.high(i) != list.low(i))
                return true;
        }

        return false;
    }

    private List<DataSet> getOverlayDataSets() {
        resetNextColor();
        List<DataSet> result = new ArrayList<DataSet>();

        for(IOverlay overlay : mOverlays) {
            ValueArray arr = overlay.eval(getPriceList(logScale));
            result.addAll(getDefaultOverlayDataSets(arr, overlay));

            if(overlay.getId().getClass() == PriceOverlay.class) {
                PriceOverlay ol = (PriceOverlay)overlay.getId();

                switch (ol) {
                    case PSAR:
                        result.get(result.size() - 1).setLineType(LineType.DOTTED);
                        break;
                }
            }
        }

        return result;
    }


}
