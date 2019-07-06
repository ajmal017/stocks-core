package org.cerion.stocklist.arrays

class MACDArray(size: Int, private val signalPeriod: Int) : FloatArray(size) {

    private val signal: FloatArray by lazy {
        ema(signalPeriod)
    }

    //Signal line
    fun signal(pos: Int) : Float = signal.get(pos)

    //Histogram
    fun hist(pos: Int) : Float = this[pos] - signal.get(pos)
}
