package game

class LaterationGame(freq: Int) : Game(gameId, gameName, freq) {

    companion object : GameAPI {
        override fun registerRoutes() {
            API.registerGameGet("/testRoute") { req, res, player, game ->
                return@registerGameGet "${game.id}, ${game.freq}, ${player.id}"
            }
        }

        override val gameId = 0
        override val gameName = "LaterationGame"
    }


}