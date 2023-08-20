import org.w3c.xhr.XMLHttpRequest

object Network {

    //inline fun <reified T> sendRequestWithResponse(method: String, route: String, crossinline onResponse: (T) -> Unit) {
    //    sendRequest(method, route) {
    //        val message = Json.decodeFromString<T>(it)
    //        onResponse(message)
    //    }
    //}

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