package org.cerion.stocklist.arrays

abstract class BandArray : ValueArray() {
    abstract fun mid(pos: Int): Float
    abstract fun lower(pos: Int): Float
    abstract fun upper(pos: Int): Float
    abstract fun bandwidth(pos: Int): Float
    abstract fun percent(pos: Int): Float
}
