package noahandrews.me.saveorganizer.oauth

data class AuthRequest(val state: String, val uri: String)

class AuthRequestGenerator {
    fun generate(authEndpoint: String,
                 clientId: String,
                 responseType: String = "code",
                 state: String,
                 redirectUri: String,
                 duration: String = "permanent", scopes: Array<String>): AuthRequest {

        val scopeString = scopes.joinToString(separator = "%20")

        val uriString =  "$authEndpoint?" +
                "client_id=$clientId&" +
                "response_type=$responseType&" +
                "state=$state&" +
                "redirect_uri=$redirectUri&" +
                "duration=$duration&" +
                "scope=$scopeString"
        return AuthRequest(state, uriString)
    }
}