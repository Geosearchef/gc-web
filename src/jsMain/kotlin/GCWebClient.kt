import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import location.LocationManager
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.xhr.XMLHttpRequest

var currentFreq = -1
var uuid = ""

fun onLoad() {
    println("Starting gcweb client...")

    checkBrowser()

    login()

    locationTest()

    // TODO: move somewhere else
    document.getElementById("login-new-freq-set-button")?.addEventListener("click", { changeFreq() })
}

fun checkBrowser() {
    if("Android" in window.navigator.userAgent && "Chrome" !in window.navigator.userAgent) {
        window.alert("You seem to be running Android. Usage of Chrome is recommended to avoid issues with the geolocation API.")
    }
}

// TODO: move to separate classes

fun updateFreqDisplay() {
    document.getElementById("login-current-freq")?.textContent = "$currentFreq MHz"
}

fun login() {
    sendRequest("GET", "/login", {
        val res = Json.decodeFromString<LoginResponse>(it)
        console.log("logged in as " + res.id)
        uuid = res.id
        currentFreq = res.initialFreq

        updateFreqDisplay()
    })
}

fun changeFreq() {
    val desiredFreq = (document.getElementById("login-new-freq") as? HTMLInputElement)?.value?.toInt()
    desiredFreq?.let { freq ->
        sendRequest("POST", "/player/$uuid/setFreq?freq=$freq", {
            val res = Json.decodeFromString<SetFreqResponse>(it)
            console.log("updated freq to ${res.freq}")

            currentFreq = res.freq
            updateFreqDisplay()
        })
    }
}

// TODO: regularly send location? (when? sync vs async)

//inline fun <reified T> sendRequestWithResponse(method: String, route: String, crossinline onResponse: (T) -> Unit) {
//    sendRequest(method, route) {
//        val message = Json.decodeFromString<T>(it)
//        onResponse(message)
//    }
//}

fun sendRequest(method: String, route: String, onResponse: (String) -> Unit, body: String? = null) {
    val req = XMLHttpRequest()
    req.onreadystatechange = {
        if(req.readyState == 4.toShort() && req.status == 200.toShort()) {
            onResponse(req.responseText)
        }
    }
    req.open(method, route, async = true)
    if(body != null) {
        req.send(body)
    } else {
        req.send()
    }
}


// TODO: turn the location service into an actual location manager
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
    }, { error, msg ->
        println("Encountered an error while obtaining location: ${error.name}, $msg")
    })
}

fun main() {
    window.onload = { onLoad() }
}
