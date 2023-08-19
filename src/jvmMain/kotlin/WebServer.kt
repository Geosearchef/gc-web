import spark.Spark.port
import spark.Spark.staticFiles
import spark.kotlin.secure
import util.Util
import java.io.File

object WebServer {
    val STATIC_FILES_LOCATION =  System.getProperty("user.dir") + File.separator + (if(Util.isRunningFromJar()) "static" else "build/distributions")

    val log = Util.logger()

    fun init() {
        log.info("Serving static files from $STATIC_FILES_LOCATION")
        staticFiles.externalLocation(STATIC_FILES_LOCATION)
        port(GCWebOptions.STATIC_PORT)

        if(GCWebOptions.ENABLE_SSL) {
            log.info("Enabling SSL")
            secure("gcweb-keystore.jks", "123456", "gcweb-keystore.jks", "123456")
        }

        staticFiles.expireTime(1)

        API.init()

        spark.Spark.init()
    }

}