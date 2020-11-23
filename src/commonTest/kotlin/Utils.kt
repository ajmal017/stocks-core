package org.cerion.stocks.core

import kotlinx.coroutines.Deferred

expect object Utils {
    suspend fun readResourceFileAsync(fileName: String): Deferred<String>

    fun runAsync(block: suspend () -> Unit)
}