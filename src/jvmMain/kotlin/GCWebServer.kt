import game.Game
import game.LaterationGame
import util.Util
import java.util.*

// synchronize everything on players
val players = ArrayList<Player>()
val games = ArrayList<Game>()

fun addPlayer(): Player {
    synchronized(players) {
        val uuid = UUID.randomUUID()
        val player = Player(uuid.toString(), Util.getUnassignedRandom(0..100, players.map { it.freq }))

        players.add(player)
        return@addPlayer player
    }
}

fun getPlayer(id: String): Player? {
    synchronized(players) {
        return@getPlayer players.find { it.id == id }
    }
}

fun createGame(gameId: Int, freq: Int): Game? {
    synchronized(players) {
        if(players.count { it.freq == freq } < 2) {
            return@createGame null
        }

        val newGame = LaterationGame(freq) // TODO: automate selection

        games.add(newGame)
        return@createGame newGame
    }
}

fun getGameByPlayerAndId(player: Player, gameId: Int): Game? {
    synchronized(players) {
        return@getGameByPlayerAndId games.find { player in it.players && it.id == gameId }
    }
}

fun joinGame(player: Player, game: Game) {
    game.addPlayer(player)
}

fun createOrJoinGame(player: Player, gameId: Int): Game? {
    synchronized(players) {
        if(games.any { player in it.players }) {
            println("Player ${player.id} tried to join a second game, aborting...")
            return@createOrJoinGame null
        }

        val game = games.find { it.freq == player.freq } ?: createGame(gameId, player.freq) ?: return@createOrJoinGame null
        joinGame(player, game)

        return@createOrJoinGame game
    }
}

fun main() {
    WebServer.init()
}