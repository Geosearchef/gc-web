import org.w3c.xhr.XMLHttpRequest

object Network {
    const val POST = "POST"
    const val GET = "GET"

    //inline fun <reified T> sendRequestWithResponse(method: String, route: String, crossinline onResponse: (T) -> Unit) {
    //    sendRequest(method, route) {
    //        val message = Json.decodeFromString<T>(it)
    //        onResponse(message)
    //    }
    //}


    fun sendLocationUpdate(newLocation: Location) {
        sendPlayerRequest(POST, "/updateLocation", {}, UpdateLocationRequest(newLocation))
    }

    fun getPage(pageId: Int, onResponse: (String) -> Unit) {
        sendGameRequest(GET, "/page?pageId=$pageId", onResponse)
    }


    fun sendGameRequest(method: String, route: String, onResponse: (String) -> Unit, body: Message) {
        sendGameRequest(method, route, onResponse, body.toJson())
    }

    fun sendGameRequest(method: String, route: String, onResponse: (String) -> Unit, body: String? = null) {
        sendPlayerRequest(method, "/game/${gameId}${route}", onResponse, body)
    }

    fun sendPlayerRequest(method: String, route: String, onResponse: (String) -> Unit, body: Message) {
        sendPlayerRequest(method, route, onResponse, body.toJson())
    }

    fun sendPlayerRequest(method: String, route: String, onResponse: (String) -> Unit, body: String? = null) {
        sendRequest(method, "/player/${uuid}${route}", onResponse, body)
    }

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

}