package org.cerion.stocklist.arrays

internal class KCBandArray(private val source: FloatArray, private val multiplier: Float, private val ema: FloatArray, private val atr: FloatArray) : IBandArray {

    override fun size(): Int = source.size()

    val average: FloatArray = ema
    val range: FloatArray = atr

    override fun mid(pos: Int): Float {
        return average.mVal[pos]
    }

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
        return (source.get(pos) - lower(pos)) / (upper(pos) - lower(pos))
    }

}