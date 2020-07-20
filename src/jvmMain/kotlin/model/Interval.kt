package org.cerion.stocks.core.model

enum class Interval {
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    YEARLY
}

// Intervals the APIs allow fetching for
enum class FetchInterval {
    DAILY,
    WEEKLY,
    MONTHLY,
}