import util.Util
import java.util.*
import kotlin.collections.ArrayList

val players = ArrayList<Player>()

fun addPlayer(): Player {
    synchronized(players) {
        val uuid = UUID.randomUUID()
        val player = Player(uuid.toString(), Util.getUnassignedRandom(0..100, players.map { it.freq }))

        players.add(player)
        return@addPlayer player
    }
}

fun getPlayer(id: String): Player? = players.find { it.id == id }

fun main() {
    WebServer.init()
}