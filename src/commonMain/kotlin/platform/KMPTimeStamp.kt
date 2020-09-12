package org.cerion.stocks.core.platform

expect class KMPTimeStamp : Comparable<KMPTimeStamp> {
    val time: Long
}