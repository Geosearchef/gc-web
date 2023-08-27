import game.Game
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import spark.Request
import spark.Response
import spark.Spark.*
import util.Util

object API {

    val log = Util.logger()

    fun init() {
        get("/login") { req, res ->
            val player = addPlayer()

            LoginResponse(player.id, player.freq).toJson()
        }

        get("/availableGames") { req, res ->
            AvailableGamesResponse(Game.gameAPIs.map { Pair(it.gameId, it.gameName) }).toJson()
        }

        path("/player/:id") {
            post("/setFreq") { req, res ->
                val player = getPlayer(req.params(":id")) ?: return@post "Not found".also { res.status(404) }
                val newFreq = req.queryParamOrDefault("freq", player.freq.toString()).toIntOrNull() ?: return@post "Bad request".also { res.status(400) }
                player.freq = newFreq

                println("Set frequency for player ${player.id} to $newFreq")

                SetFreqResponse(player.freq).toJson()
            }

            post("/joinGame") { req, res ->
                val player = getPlayer(req.params(":id")) ?: return@post "Not found".also { res.status(404) }
                val desiredGameId = req.queryParams("gameId").toIntOrNull() ?: return@post "Bad request".also { res.status(400) }

                val game: Game? = createOrJoinGame(player, desiredGameId)

                game?.let { println("Player ${player.id} joined game with id ${it.id} and freq ${it.freq}") }

                JoinGameResponse(game != null, game?.id ?: -1).toJson()
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

            path("/game") {
                for(gameApi in Game.gameAPIs) {
                    path("/${gameApi.gameId}") {
                        registrationContextCurrentGameId = gameApi.gameId // sets the current global context, so that routes know their game id

                        gameApi.registerRoutes()
                    }
                }
            }
        }
    }


    var registrationContextCurrentGameId = -1
    fun registerGameGet(path: String, route: (Request, Response, Player, Game) -> Any) {
        val registeringGameId = registrationContextCurrentGameId
        get(path) { req, res ->
            val player = getPlayer(req.params(":id")) ?: return@get "Not found".also { res.status(404) }
            val game = getGameByPlayerAndId(player, registeringGameId) ?: return@get "Not found".also { res.status(404) }

            return@get route(req, res, player, game)
        }
    }
    fun registerGamePost(path: String, route: (Request, Response, Player, Game) -> Any) {
        val registeringGameId = registrationContextCurrentGameId
        post(path) { req, res ->
            val player = getPlayer(req.params(":id")) ?: return@post "Not found".also { res.status(404) }
            val game = getGameByPlayerAndId(player, registeringGameId) ?: return@post "Not found".also { res.status(404) }

            return@post route(req, res, player, game)
        }
    }

}