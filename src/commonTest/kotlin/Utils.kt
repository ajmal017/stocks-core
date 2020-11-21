package org.cerion.stocks.core

expect suspend fun readResourceFile(fileName: String): String

expect fun runTest(block: suspend () -> Unit)