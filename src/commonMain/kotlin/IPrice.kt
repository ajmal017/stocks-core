package org.cerion.stocks.core

import org.cerion.stocks.core.platform.KMPDate


interface IPrice {
    val date: KMPDate
    val open: Float
    val close: Float
    val high: Float
    val low: Float
    val volume: Float
}