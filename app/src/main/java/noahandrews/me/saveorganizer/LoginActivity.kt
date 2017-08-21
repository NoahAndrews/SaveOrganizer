package noahandrews.me.saveorganizer

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment
import org.chromium.customtabsclient.CustomTabsActivityHelper
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var customTabsHelperFragment: CustomTabsHelperFragment

    private val preloadUrl = Uri.parse("https://www.reddit.com/login.compact") //TODO: evaluate if this works

    private val customTabsFallback = CustomTabsActivityHelper.CustomTabsFallback {
        activity, uri -> activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }


    // TODO: Attempt to reuse the credentials from other reddit apps using AccountManager, but only
    // for API 26 and higher. We don't want to have to request access to Contacts.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(intent.action == Intent.ACTION_VIEW) {
            val redirectUri = intent.data
            val isStateCorrect = redirectUri.getQueryParameter("state") == "" //FIXME: Get this from SharedPreferences
            if (redirectUri.queryParameterNames.contains("error") || !isStateCorrect) {
                // FIXME: Handle the error. Possible errors are an incorrect state, and those listed on this page:
                // https://github.com/reddit/reddit/wiki/OAuth2
            } else {
                // FIXME: Store the oauth token
            }
        }


        customTabsHelperFragment = CustomTabsHelperFragment.attachTo(this)
        customTabsHelperFragment.setConnectionCallback(object:CustomTabsActivityHelper.ConnectionCallback {
            override fun onCustomTabsConnected() {
                customTabsHelperFragment.mayLaunchUrl(preloadUrl, null, null)
            }
            override fun onCustomTabsDisconnected() {}
        })

        loginButton.setOnClickListener {
            val customTabIntent = CustomTabsIntent.Builder()
                    //TODO: Consider animations
                    //TODO: Add referrer
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .build()
            val oauthStateValue = UUID.randomUUID().toString() //FIXME: Persist this to SharedPreferences
            CustomTabsHelperFragment.open(this, customTabIntent, generateOauthUri(oauthStateValue), customTabsFallback)
        }
    }

    private fun generateOauthUri(state: String) : Uri {
        val clientId = "dNjcxkM9pTQ0sQ"
        val redirectUri = "me.noahandrews.saveorganizer://logincallback"
        val duration = "permanent"
        val scopes = "identity history"

        return Uri.parse("https://www.reddit.com/api/v1/authorize.compact?" +
                "client_id=$clientId&response_type=code&state=$state&redirect_uri=$redirectUri" +
                "&duration=$duration&scope=$scopes")
    }
}
