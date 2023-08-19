import kotlinx.serialization.Serializable

@Serializable
class Location(var serverTimestamp: Long, val lat: Double, val lon: Double, val accuracy: Double, val altitude: Double = 0.0) {

}
