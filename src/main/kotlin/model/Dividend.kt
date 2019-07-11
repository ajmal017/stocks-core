package org.cerion.stocklist.model

import java.text.SimpleDateFormat
import java.util.*

class Dividend(val date: Date, val dividend: Float) {
    val dateString: String
        get() = mDateFormat.format(date)

    companion object {
        private val mDateFormat = SimpleDateFormat("yyyy-MM-dd")
    }
}
