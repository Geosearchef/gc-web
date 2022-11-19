package location

import util.Util

object LocationManager {


    // jetbrains, please wrap this API :)
    fun getOneShotLocation(locationCallback: (GeolocationPosition) -> Unit, errorCallback: (GeolocationPositionError, String) -> Unit) {
        Util.ensureSecureContext()

        val errorCallbackWrapper = { error: dynamic ->
            errorCallback(GeolocationPositionError.getByCode((js("error.code") as Number).toShort())!!, js("error.message") as String)
        }
        val successCallbackWrapper = { position: dynamic ->
            locationCallback(geolocationPositionFromJS(position))
        }

        js("navigator.geolocation.getCurrentPosition(function(pos) { successCallbackWrapper(pos) }, function(error) { errorCallbackWrapper(error) }, {enableHighAccuracy: true})")
    }

    var watchId: Int? = null

    fun watchLocation(locationCallback: (GeolocationPosition) -> Unit, errorCallback: (GeolocationPositionError, String) -> Unit) {
        Util.ensureSecureContext()

        if(watchId != null) {
            println("Already watching this position, aborting!")
            return
        }

        println("Continously watching geolocation")

        val errorCallbackWrapper = { error: dynamic ->
            errorCallback(GeolocationPositionError.getByCode((js("error.code") as Number).toShort())!!, js("error.message") as String)
        }
        val successCallbackWrapper = { position: dynamic ->
            locationCallback(geolocationPositionFromJS(position))
        }

        watchId = (js("navigator.geolocation.watchPosition(function(pos) { successCallbackWrapper(pos) }, function(error) { errorCallbackWrapper(error) }, {enableHighAccuracy: true})") as Number).toInt()
    }

    private fun geolocationPositionFromJS(position: dynamic) = GeolocationPosition(
        (js("position.timestamp") as Number).toLong(),
        (js("position.coords.latitude") as Number).toDouble(),
        (js("position.coords.longitude") as Number).toDouble(),
        (js("position.coords.accuracy") as Number).toDouble(),
        (js("position.coords.altitude != null ? position.coords.altitude : -1") as Number).toDouble(),  // can be invalid -> -1
        (js("position.coords.altitudeAccuracy != null ? position.coords.altitudeAccuracy : -1") as Number).toDouble(),  // can be invalid -> -1
        (js("position.coords.heading != null ? position.coords.heading : -1") as Number).toDouble(),  // can be invalid -> -1
        (js("position.coords.speed != null ? position.coords.speed : -1") as Number).toDouble(),  // can be invalid -> -1
    )


}