package game

import Player
import WebServer
import java.nio.file.Files
import java.nio.file.Paths

abstract class Game(val id: Int, val name: String, val freq: Int, val pagesFolderName: String) {
    val players = ArrayList<Player>() // always acquire entire player list lock first


    fun addPlayer(player: Player) {
        synchronized(players) {
            players.add(player)
        }
    }

    abstract fun getPage(player: Player, pageId: Int): String

    fun readPageFromDisk(fileName: String): String {
        return Files.readString(
            Paths.get(WebServer.STATIC_FILES_LOCATION)
                .resolve("pages")
                .resolve(pagesFolderName)
                .resolve(fileName)
        )
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