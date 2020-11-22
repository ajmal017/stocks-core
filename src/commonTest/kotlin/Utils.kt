package org.cerion.stocks.core

import kotlinx.coroutines.Deferred

expect suspend fun readResourceFileAsync(fileName: String): Deferred<String>

expect fun runAsync(block: suspend () -> Unit)