package org.cerion.stocks.core.model

interface Position {
    val symbol: String
    val quantity: Double
    val pricePerShare: Double
    val totalValue: Double
    val cash: Boolean
}
