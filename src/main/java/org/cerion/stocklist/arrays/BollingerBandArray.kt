package org.cerion.stocklist.arrays

internal class BollingerBandArray(private val source: FloatArray, period: Int, private val multiplier: Float) : BandArray() {
    private val average = source.sma(period)
    private val range = source.std(period, average)

    override val size = source.size
    override fun mid(pos: Int): Float = average.mVal[pos]
    override fun lower(pos: Int): Float = average.mVal[pos] - multiplier * range.mVal[pos]
    override fun upper(pos: Int): Float = average.mVal[pos] + multiplier * range.mVal[pos]

    override fun bandwidth(pos: Int): Float {
        //(Upper Band - Lower Band)/Middle Band
        return (upper(pos) - lower(pos)) / mid(pos) * 100
    }

    override fun percent(pos: Int): Float {
        //%B = (Price - Lower Band)/(Upper Band - Lower Band)
        return (source.get(pos) - lower(pos)) / (upper(pos) - lower(pos))
    }

}