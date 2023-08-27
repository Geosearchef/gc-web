import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import spark.Spark.*
import util.Util

object API {

    val log = Util.logger()

    fun init() {
        get("/login") { req, res ->
            val player = addPlayer()

            LoginResponse(player.id, player.freq).toJson()
        }

        path("/player/:id") {
            post("/setFreq") { req, res ->
                val player = getPlayer(req.params(":id")) ?: return@post "Not found".also { res.status(404) }
                val newFreq = req.queryParamOrDefault("freq", player.freq.toString()).toIntOrNull() ?: return@post "Bad request".also { res.status(400) }
                player.freq = newFreq

                println("Set frequency for player ${player.id} to $newFreq")

                SetFreqResponse(player.freq).toJson()
            }

            post("/updateLocation") { req, res ->
                val player = getPlayer(req.params(":id")) ?: return@post "Not found".also { res.status(404) }
                val newLocationMessage = Json.decodeFromString<UpdateLocationRequest>(req.body())
                val newLocation = newLocationMessage.location

                newLocation.serverTimestamp = System.currentTimeMillis()
                player.lastLocation = newLocation

                println("Received location update from ${player.id}: $newLocation")

                newLocation.serverTimestamp
            }
        }
    }

}