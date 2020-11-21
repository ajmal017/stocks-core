package org.cerion.stocks.core

import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import kotlin.js.Promise

actual suspend fun readResourceFile(fileName: String): String {
    return getText(fileName).await()
}

private fun getText(fileName: String): Promise<String> {
    return window.fetch(fileName).then {
        it.text()
    }.then {
        it
    }
}

actual fun runTest(block: suspend () -> Unit): dynamic = GlobalScope.promise {
    block()
}