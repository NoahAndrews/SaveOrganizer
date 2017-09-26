package noahandrews.me.saveorganizer.oauth

import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class AuthRequestGeneratorTest {
    lateinit var authRequestGenerator: AuthRequestGenerator

    private val authEndpoint = "https://authendpoint.com/auth"
    private val clientId = "myclientid"
    private val responseType = "rubbish"
    private val state = "thisisafakestate"
    private val redirectUri = "https://noah.com/callback"
    private val duration = "ageoftheuniverse"
    private val scopes = arrayOf("scope1", "scope2")
    private val scopeString = "scope1%20scope2"

    @Before
    fun setup() {
        authRequestGenerator = AuthRequestGenerator()
    }

    @Test
    fun allParameters() {
        val generatedRequest = authRequestGenerator.generate(
                authEndpoint = authEndpoint,
                clientId = clientId,
                responseType = responseType,
                state = state,
                redirectUri = redirectUri,
                duration = duration, scopes = scopes
        )

        val correctUri = "$authEndpoint?" +
                "client_id=$clientId&" +
                "response_type=$responseType&" +
                "state=$state&" +
                "redirect_uri=$redirectUri&" +
                "duration=$duration&" +
                "scope=$scopeString"
        generatedRequest.uri shouldEqual correctUri
    }

    @Test
    fun noResponseType() {
        val generatedRequest = authRequestGenerator.generate(
                authEndpoint = authEndpoint,
                clientId = clientId,
                state = state,
                redirectUri = redirectUri,
                duration = duration, scopes = scopes
        )

        val correctUri = "$authEndpoint?" +
                "client_id=$clientId&" +
                "response_type=code&" +
                "state=$state&" +
                "redirect_uri=$redirectUri&" +
                "duration=$duration&" +
                "scope=$scopeString"
        generatedRequest.uri shouldEqual correctUri
    }

    @Test
    fun noDuration() {
        val generatedRequest = authRequestGenerator.generate(
                authEndpoint = authEndpoint,
                clientId = clientId,
                responseType = responseType,
                state = state,
                redirectUri = redirectUri, scopes = scopes
        )

        val correctUri = "$authEndpoint?" +
                "client_id=$clientId&" +
                "response_type=$responseType&" +
                "state=$state&" +
                "redirect_uri=$redirectUri&" +
                "duration=permanent&" +
                "scope=$scopeString"
        generatedRequest.uri shouldEqual correctUri
    }
}

