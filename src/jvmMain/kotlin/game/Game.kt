package game

import Player

abstract class Game(val id: Int, val name: String, val freq: Int) {
    val players = ArrayList<Player>() // always aquire entire player list lock first


    fun addPlayer(player: Player) {
        synchronized(players) {
            players.add(player)
        }
    }

    // Every game has to be registered here
    companion object {
        val gameAPIs: List<GameAPI> = listOf(LaterationGame.Companion)
    }

    interface GameAPI {
        fun registerRoutes()
        val gameId: Int
        val gameName: String
    }
}