import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import location.LocationManager
import org.w3c.dom.HTMLParagraphElement

var currentFreq = -1
var uuid = ""

fun onLoad() {
    println("Starting gcweb client...")

    View.init()

    checkBrowser()

    login()

    locationTest()
}

fun checkBrowser() {
    if("Android" in window.navigator.userAgent && "Chrome" !in window.navigator.userAgent) {
        window.alert("You seem to be running Android. Usage of Chrome is recommended to avoid issues with the geolocation API.")
    }
}



fun login() {
    Network.sendRequest("GET", "/login", {
        val res = Json.decodeFromString<LoginResponse>(it)
        console.log("logged in as " + res.id)
        uuid = res.id
        currentFreq = res.initialFreq

        View.updateFreqDisplay()
    })
}

fun changeFreq() {
    val desiredFreq = View.loginNewFreqInput.value.toInt()
    Network.sendPlayerRequest("POST", "/setFreq?freq=$desiredFreq", {
        val res = Json.decodeFromString<SetFreqResponse>(it)
        console.log("updated freq to ${res.freq}")

        currentFreq = res.freq
        View.updateFreqDisplay()
    })
}


fun locationTest() {
//    LocationManager.getOneShotLocation({ pos ->
//        console.log(pos)
//    }, { error, msg ->
//        println("Encountered an error while obtaining location: ${error.name}, $msg")
//    })
//
//    window.setTimeout(::locationTest, 5000)


    LocationManager.watchLocation({ pos ->
        console.log(pos)
        (document.getElementById("debug-location") as HTMLParagraphElement).textContent = pos.toString()

        //TODO: just for testing
        if(uuid != "") {
            Network.sendLocationUpdate(pos.toLocation())
        }
    }, { error, msg ->
        println("Encountered an error while obtaining location: ${error.name}, $msg")
    })
}

fun main() {
    window.onload = { onLoad() }
}
