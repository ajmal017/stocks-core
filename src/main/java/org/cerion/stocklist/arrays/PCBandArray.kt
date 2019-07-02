package org.cerion.stocklist.arrays

// Price Channels
internal class PCBandArray(private val upper: FloatArray, private val lower: FloatArray) : BandArray() {

    override val size = upper.size

    override fun mid(pos: Int): Float = (upper(pos) + lower(pos)) / 2
    override fun lower(pos: Int): Float = lower.get(pos)
    override fun upper(pos: Int): Float = upper.get(pos)

    override fun bandwidth(pos: Int): Float {
        //(Upper Band - Lower Band)/Middle Band
        return (upper(pos) - lower(pos)) / mid(pos) * 100
    }

    override fun percent(pos: Int): Float {
        return 0.5f // Not useful for Price channels since its always 50%
    }
}