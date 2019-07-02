package org.cerion.stocklist.arrays

interface IBandArray {
    fun size(): kotlin.Int
    fun mid(pos: Int): Float
    fun lower(pos: Int): Float
    fun upper(pos: Int): Float
    fun bandwidth(pos: Int): Float
    fun percent(pos: Int): Float
}