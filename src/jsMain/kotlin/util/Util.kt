package util

import kotlin.js.Date
import kotlin.math.pow
import kotlin.math.roundToInt

actual object Util {
    actual fun currentTimeMillis() = Date().getTime().toLong()


    var isChromiumCached: Boolean? = null
    fun isChromium(): Boolean {
        if(isChromiumCached == null) {
            isChromiumCached = js("!!window.chrome")
            if(isChromiumCached != false) {
                console.log("Chromium detected, using self calculated event movement")
            }
        }
        return isChromiumCached ?: true
    }

    fun ensureSecureContext() {
        if(! js("window.isSecureContext") as Boolean) {
            console.error("This is not a secure context!")
        }
    }
}


fun Double.toDecimals(n: Int): String {
    val stringified = this.toDecimalsAsDouble(n).toString()
    val digitsAfterSeparator = stringified.split('.').getOrNull(1)?.length ?: 0
    return stringified + (if(digitsAfterSeparator == 0) "." else "") + "0".repeat(n - digitsAfterSeparator)
}

fun Double.toDecimalsAsDouble(n: Int) = (this * 10.0.pow(n)).roundToInt().toDouble() / 10.0.pow(n)