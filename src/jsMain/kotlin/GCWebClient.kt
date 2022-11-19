import kotlinx.browser.document
import kotlinx.browser.window
import location.LocationManager
import org.w3c.dom.HTMLParagraphElement

fun onLoad() {
    println("Starting gcweb client...")
    locationTest()
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
