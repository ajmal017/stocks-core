package org.cerion.stocklist.functions.types

import org.cerion.stocklist.functions.IPriceOverlay
import org.cerion.stocklist.overlays.*

enum class PriceOverlay : IFunctionEnum {

    CHAN, // price channels
    CEXIT,
    KC,
    PSAR,
    CLOUD, // Ichimoku Clouds
    ZIGZAG,
    VWMA;

    override val instance: IPriceOverlay
        get() {
            return when (this) {
                CHAN -> PriceChannels()
                CLOUD -> IchimokuClouds()
                CEXIT -> ChandelierExit()
                KC -> KeltnerChannels()
                PSAR -> ParabolicSAR()
                ZIGZAG -> ZigZag()
                VWMA -> VolumeWeightedMovingAverage()
            }

        }

    override fun getInstance(vararg params: Number): IPriceOverlay {
        val overlay = instance
        overlay.setParams(*params)
        return overlay
    }
}
