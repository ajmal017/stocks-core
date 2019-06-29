package org.cerion.stocklist.charts;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.MACDArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.IIndicator;
import org.cerion.stocklist.functions.IOverlay;
import org.cerion.stocklist.functions.ISimpleOverlay;
import org.cerion.stocklist.functions.types.Indicator;

import java.util.ArrayList;
import java.util.List;

public class IndicatorChart extends StockChart {

    private IIndicator mIndicator;
    private ArrayList<IIndicator> extra = new ArrayList<IIndicator>();

    public IndicatorChart(IIndicator indicator) {
        mIndicator = indicator;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        IndicatorChart chart = (IndicatorChart)super.clone();

        // Preserve overlays, they are reset with setIndicator()
        List<IOverlay> overlays = new ArrayList<>();
        overlays.addAll(chart.mOverlays);

        // Copy indicator
        Number[] params = getIndicator().getParams().toArray(new Number[0]).clone();
        IIndicator indicator = getIndicator().getId().getInstance();
        indicator.setParams(params);
        chart.setIndicator(indicator);

        // Add back overlays
        chart.mOverlays = overlays;

        return chart;
    }

    public void setIndicator(IIndicator indicator) {
        clearOverlays();
        mIndicator = indicator;
    }

    public void add(IIndicator indicator) {
        if (indicator.getId() != mIndicator.getId())
            throw new IllegalArgumentException("must be type " + mIndicator.getId());

        extra.add(indicator);
    }

    public IIndicator getIndicator() {
        return mIndicator;
    }

    public Indicator getId() {
        if (mIndicator == null)
            return null;

        return mIndicator.getId();
    }

    @Override
    public List<IDataSet> getDataSets() {
        List<IDataSet> result = new ArrayList<IDataSet>();

        //DataSet data = new DataSet(mList.getVolume(), mIndicator.toString(), colorBlack());
        //result.addAll(Arrays.asList(data) );
        ValueArray arr = mIndicator.eval(getPriceList());
        result.addAll(getIndicatorDataSets(arr, mIndicator));

        // TODO set color on these
        for(IIndicator indicator : extra) {
            ValueArray va = indicator.eval(getPriceList());
            result.addAll(getIndicatorDataSets(va, indicator));
        }

        result.addAll(getOverlayDataSets(arr));
        return result;
    }

    private List<DataSet> getOverlayDataSets(ValueArray arr) {
        resetNextColor();
        List<DataSet> result = new ArrayList<DataSet>();

        for(IOverlay overlay : mOverlays) {
            ISimpleOverlay ol = (ISimpleOverlay)overlay;

            ValueArray temp = ol.eval((FloatArray)arr);
            result.addAll(getDefaultOverlayDataSets(temp, ol));
        }

        return result;
    }

    private List<DataSet> getIndicatorDataSets(ValueArray arr, IIndicator indicator) {
        // TODO add all cases types and colors
        if(arr.getClass() == BandArray.class)
            return getBandDataSet((BandArray)arr,indicator.toString(), indicator.toString(), colorBlack());
        else if(arr.getClass() == MACDArray.class)
            return getMACDDataSet((MACDArray)arr, indicator.toString(), indicator.toString(), indicator.toString(), colorBlack(), colorRed(), colorBlue());
        else if(arr.getClass() == PairArray.class)
            return getPairDataSet((PairArray)arr, indicator.toString(), indicator.toString(), colorGreen(), colorRed());

        return getSingleDataSet((FloatArray)arr, indicator.toString(), colorBlack());
    }
}
