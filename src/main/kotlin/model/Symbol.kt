package org.cerion.stocks.core.model

class Symbol(val symbol: String, var name: String? = null, var exchange: String? = null) {

    val isValid: Boolean
        get() = !(name!!.contentEquals("N/A") && exchange!!.contentEquals("N/A"))
}
