package noahandrews.me.saveorganizer.oauth

import android.app.Application
import android.content.Context
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import noahandrews.me.saveorganizer.util.PerfTimerFactory
import java.net.URI
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OauthManager @Inject constructor(context: Application, private val perfTimerFactory: PerfTimerFactory? = null) {
    private val TAG: String = this.javaClass.simpleName
    val STATE_PREF = "validState"
    private val OAUTH_SHARED_PREF = "OauthState"

    // OAuth URL parameters
    private val clientId = "dNjcxkM9pTQ0sQ"
    private val redirectUri = "me.noahandrews.saveorganizer://logincallback"
    private val duration = "permanent"
    private val scopes = arrayOf("identity", "history")

    private val generateStateUuid = {
        val timer = perfTimerFactory?.newPerfTimer(TAG, "generate UUID")
        val uuid = UUID.randomUUID()
        timer?.addSplit("UUID is $uuid")
        timer?.dumpToLog()
        uuid
    }

    private var sharedPrefs = context.getSharedPreferences(OAUTH_SHARED_PREF, Context.MODE_PRIVATE)
    private var validState : String? = sharedPrefs.getString(STATE_PREF, null)
    private var nextState = async(CommonPool) { generateStateUuid() }

    fun directUserToAuth() {
        launch(CommonPool) {
            
        }
    }

    @Deprecated(message = "Will be gone soon", replaceWith = ReplaceWith("directUserToAuth"))
    fun generateInitialRequest() = async(CommonPool) {
        val timer = perfTimerFactory?.newPerfTimer(TAG, "generate initial request")
        val scopeString = scopes.joinToString(separator = "%20")
        val state = nextState.await().toString()
        validState = state
        val editor = sharedPrefs.edit()
        editor.putString(STATE_PREF, state).apply()
        nextState = async(CommonPool) { generateStateUuid() }

        val request = "https://www.reddit.com/api/v1/authorize.compact?" +
                "client_id=$clientId&" +
                "response_type=code&" +
                "state=$state&" +
                "redirect_uri=$redirectUri&" +
                "duration=$duration&" +
                "scope=$scopeString"
        timer?.dumpToLog()
        request
    }

    fun processOauthRedirect(redirectUri: URI) {
        val state = extractState(redirectUri, perfTimerFactory)
        if(state != UUID.fromString(validState)) {
            throw InvalidStateParameterException()
        }
        sharedPrefs.edit().remove(STATE_PREF).apply()
        if (redirectUri.toASCIIString().contains("error")) {
            // FIXME: Handle the error. Possible errors are listed on this page:
            // https://github.com/reddit/reddit/wiki/OAuth2
        } else {
            // FIXME: Proceed to the next step of Oauth process
        }
    }
}

fun extractState(uri: URI, perfTimerFactory: PerfTimerFactory? = null) : UUID {
    val timer = perfTimerFactory?.newPerfTimer("OauthManager", "extract state")
    val timer2 = perfTimerFactory?.newPerfTimer("OauthManager", "extract state 2")

    val query = uri.query

    timer?.addSplit("extract query")

    val state = query.split(delimiters = "state=", ignoreCase = true)[1]
            .split("&")[0]

    timer?.addSplit("extract state from query")

    val uuid = UUID.fromString(state)

    timer?.addSplit("convert string to UUID")
    timer?.dumpToLog()
    timer2?.dumpToLog()

    return uuid
}
