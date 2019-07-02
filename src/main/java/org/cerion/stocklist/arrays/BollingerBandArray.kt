package org.cerion.stocklist.arrays

internal class BollingerBandArray(private val arr: FloatArray, period: Int, private val multiplier: Float) : ValueArray(), IBandArray {

    private val average = arr.sma(period)
    private val range = arr.std(period, average)

    override fun size(): Int = arr.size()
    override fun mid(pos: Int): Float = average.mVal[pos]

    override fun lower(pos: Int): Float {
        return average.mVal[pos] - multiplier * range.mVal[pos]
    }

    override fun upper(pos: Int): Float {
        return average.mVal[pos] + multiplier * range.mVal[pos]
    }

    override fun bandwidth(pos: Int): Float {
        //(Upper Band - Lower Band)/Middle Band
        return (upper(pos) - lower(pos)) / mid(pos) * 100
    }

    override fun percent(pos: Int): Float {
        //%B = (Price - Lower Band)/(Upper Band - Lower Band)
        return (arr.get(pos) - lower(pos)) / (upper(pos) - lower(pos))
    }

}