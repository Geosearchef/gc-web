import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import location.LocationManager
import org.w3c.dom.HTMLParagraphElement

var currentFreq = -1
var uuid = ""
var gameJoined = false
var gameId = -1

fun onLoad() {
    println("Starting gcweb client...")

    View.init()

    checkBrowser()

    getAvailableGames()
    login()

    println("Attempting to join game...")
    attemptGameJoinLoop()

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
        println("logged in as " + res.id)
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

fun getAvailableGames() {
    Network.sendRequest("GET", "/availableGames", {
        val res = Json.decodeFromString<AvailableGamesResponse>(it)
        console.log("updated available games: ${res.games}")

        View.updateAvailableGames(res.games)
    })
}

fun attemptGameJoinLoop() {
    if(gameJoined) {
        println("Game in progress, stopping join task")
        return
    }

    if(uuid == "" || View.loginGameSelector.childElementCount == 0) {
        println("Not yet logged in / games available, aborting game join")
        window.setTimeout(::attemptGameJoinLoop, GCWebOptions.GAME_JOIN_RETRY_INTERVAL)
        return
    }

    println("Game join attempt") // TODO: remove

    val desiredGameId = View.loginGameSelector.value.toInt()

    Network.sendPlayerRequest("POST", "/joinGame?gameId=$desiredGameId", {
        val res = Json.decodeFromString<JoinGameResponse>(it)

        if(res.success) {
            gameJoined = true
            gameId = res.gameId
            println("Joined Game $gameId!")

            View.setViewState(View.ViewState.GAME)
        }
    })

    if(!gameJoined) {
        window.setTimeout(::attemptGameJoinLoop, GCWebOptions.GAME_JOIN_RETRY_INTERVAL)
    } else {
        println("Game in progress, stopping join task")
    }
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

//        //TODO: just for testing
//        if(uuid != "") {
//            Network.sendLocationUpdate(pos.toLocation())
//        }
    }, { error, msg ->
        println("Encountered an error while obtaining location: ${error.name}, $msg")
    })
}

fun main() {
    window.onload = { onLoad() }
}
