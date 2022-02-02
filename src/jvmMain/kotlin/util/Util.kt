package util

import GCWebOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

actual object Util {
    fun isRunningFromJar() = GCWebOptions.javaClass.getResource("GCWebOptions.class").toString().startsWith("jar")
    inline fun logger(): Logger = LoggerFactory.getLogger(Class.forName(Thread.currentThread().stackTrace[1].className))

    actual fun currentTimeMillis() = System.currentTimeMillis()
}