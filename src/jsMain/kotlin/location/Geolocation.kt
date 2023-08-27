package location

import Location

enum class GeolocationPositionError(val code: Short) {
    PERMISSION_DENIED(1), POSITION_UNAVAILABLE(2), TIMEOUT(3);

    companion object {
        fun getByCode(code: Short) = values().find { it.code == code }
    }
}

/**
 * Wraps the JavaScript GeolocationPositon (without GeolocationCoordinates) as Kotlin does not provide that yet
 */
data class GeolocationPosition(
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double,
    val altitude: Double,  // can be invalid -> -1
    val altitudeAccuracy: Double,  // can be invalid -> -1
    val heading: Double,  // can be invalid -> -1
    val speed: Double  // can be invalid -> -1
) {
    fun toLocation() = Location(
        -1,
        latitude,
        longitude,
        accuracy,
        altitude
    )
}
