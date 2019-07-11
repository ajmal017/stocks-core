package org.cerion.stocklist.model

import java.util.Date

class Position(val symbol: String, val count: Double, val origPrice: Double, val date: Date, val dividendsReinvested: Boolean = false) {

    var id = 0
    var accountId = 0

    /**
     * Original value/cost in dollars of all shares
     * @return cost of original lot
     */
    // Value is truncated, not rounded
    val origValue: Double
        get() {
            val value = (origPrice * count * 100.0).toInt()
            return value / 100.0
        }


    override fun toString(): String {
        return "$symbol $count@$origPrice"
    }
}
