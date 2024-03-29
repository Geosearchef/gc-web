import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface Message {
    fun toJson(): String
}

@Serializable
data class AvailableGamesResponse(val games: List<Pair<Int, String>>) : Message {
    override fun toJson() = Json.encodeToString(this)
}

@Serializable
data class LoginResponse(val id: String, val initialFreq: Int) : Message {
    override fun toJson() = Json.encodeToString(this)
}

@Serializable
data class SetFreqResponse(val freq: Int) : Message {
    override fun toJson() = Json.encodeToString(this)
}

@Serializable
data class JoinGameResponse(val success: Boolean, val gameId: Int) : Message {
    override fun toJson() = Json.encodeToString(this)
}

@Serializable
data class UpdateLocationRequest(val location: Location) : Message {
    override fun toJson() = Json.encodeToString(this)
}

