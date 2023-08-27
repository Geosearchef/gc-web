import kotlinx.browser.document
import org.w3c.dom.*

object View {

    var viewState: ViewState = ViewState.LOGIN

    lateinit var loginCurrentFreq: HTMLDivElement
    lateinit var loginNewFreqSetButton: HTMLButtonElement
    lateinit var loginNewFreqInput: HTMLInputElement
    lateinit var loginGameSelector: HTMLSelectElement
    lateinit var loginContainer: HTMLDivElement

    lateinit var gameContainer: HTMLDivElement


    fun updateFreqDisplay() {
        loginCurrentFreq.textContent = "$currentFreq MHz"
    }

    fun updateAvailableGames(games: List<Pair<Int, String>>) {
        loginGameSelector.innerHTML = "" // clear children

        games.forEach { game ->
            loginGameSelector.appendChild(
                (document.createElement("option") as HTMLOptionElement).apply {
                    value = game.first.toString()
                    textContent = "${game.first}: ${game.second}"
                }
            )
        }
    }

    fun setViewState(newViewState: ViewState) {
        viewState = newViewState

        loginContainer.style.display = "none"
        gameContainer.style.display = "none"

        when(viewState) {
            ViewState.LOGIN -> loginContainer.style.display = "block"
            ViewState.GAME -> gameContainer.style.display = "block"
        }
    }


    fun init() {
        loginCurrentFreq = document.getElementById("login-current-freq") as HTMLDivElement
        loginNewFreqSetButton = document.getElementById("login-new-freq-set-button") as HTMLButtonElement
        loginNewFreqInput = document.getElementById("login-new-freq") as HTMLInputElement
        loginGameSelector = document.getElementById("login-game-selector") as HTMLSelectElement
        loginContainer = document.getElementById("login-container") as HTMLDivElement

        loginNewFreqSetButton.addEventListener("click", { changeFreq() })

        gameContainer = document.getElementById("game-container") as HTMLDivElement


        setViewState(ViewState.LOGIN)
    }

    enum class ViewState {
        LOGIN, GAME
    }

}