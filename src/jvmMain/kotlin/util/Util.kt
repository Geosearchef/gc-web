package util

import GCWebOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

actual object Util {
    fun isRunningFromJar() = GCWebOptions.javaClass.getResource("GCWebOptions.class").toString().startsWith("jar")
    inline fun logger(): Logger = LoggerFactory.getLogger(Class.forName(Thread.currentThread().stackTrace[1].className))

    actual fun currentTimeMillis() = System.currentTimeMillis()

    val random = Random()
    fun getUnassignedRandom(range: IntRange, assigned: List<Int>) : Int {
        repeat(100) {
            val randVal = random.nextInt(range.last - range.first) + range.first
            if(randVal !in assigned) {
                return randVal
            }
        }

        return -1
    }
}