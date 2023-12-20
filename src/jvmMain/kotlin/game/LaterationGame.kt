package game

import API
import Player

class LaterationGame(freq: Int) : Game(gameId, gameName, freq, "lateration") {

    companion object : GameAPI {
        override fun registerRoutes() {
            API.registerGameGet("/testRoute") { req, res, player, game ->
                return@registerGameGet "${game.id}, ${game.freq}, ${player.id}"
            }
        }

        override val gameId = 0
        override val gameName = "LaterationGame"
    }

    val pages = mapOf(
        0 to readPageFromDisk("00intro.html"),
        1 to readPageFromDisk("01test.html")
    )

    // DO NOT RETURN USER SUPPLIED DATA!
    override fun getPage(player: Player, pageId: Int): String {
        val page = pages[pageId] ?: return "Page $pageId does not exist!" // TODO: return page id here?

        return page
    }
}