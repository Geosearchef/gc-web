import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement

object View {

    lateinit var loginCurrentFreq: HTMLDivElement
    lateinit var loginNewFreqSetButton: HTMLButtonElement
    lateinit var loginNewFreqInput: HTMLInputElement


    fun updateFreqDisplay() {
        loginCurrentFreq.textContent = "$currentFreq MHz"
    }


    fun init() {
        loginCurrentFreq = document.getElementById("login-current-freq") as HTMLDivElement
        loginNewFreqSetButton = document.getElementById("login-new-freq-set-button") as HTMLButtonElement
        loginNewFreqInput = document.getElementById("login-new-freq") as HTMLInputElement

        loginNewFreqSetButton.addEventListener("click", { changeFreq() })
    }

}