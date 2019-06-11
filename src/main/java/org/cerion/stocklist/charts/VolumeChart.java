package org.cerion.stocklist.charts;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.IOverlay;
import org.cerion.stocklist.functions.ISimpleOverlay;
import org.cerion.stocklist.functions.IPriceOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VolumeChart extends StockChart {

    public boolean logScale = false;

    @Override
    public List<IDataSet> getDataSets() {
        List<IDataSet> result = new ArrayList<IDataSet>();

        DataSet data = new DataSet(getPriceList(logScale).getVolume(), "Volume", colorBlack());
        data.setLineType(LineType.BAR);
        result.addAll(Arrays.asList(data) );

        result.addAll(getOverlayDataSets());
        return result;
    }

    private List<IDataSet> getOverlayDataSets() {
        resetNextColor();
        List<IDataSet> result = new ArrayList<IDataSet>();
        FloatArray volume = getPriceList(logScale).getVolume();

        for(IOverlay overlay : mOverlays) {
            ISimpleOverlay ol = (ISimpleOverlay)overlay;

            ValueArray arr = ol.eval(volume);
            result.addAll(getDefaultOverlayDataSets(arr, overlay));
        }

        return result;
    }
}
