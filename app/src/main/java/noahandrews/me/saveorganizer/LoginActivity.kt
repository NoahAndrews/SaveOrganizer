package noahandrews.me.saveorganizer

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.widget.Button
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment
import org.chromium.customtabsclient.CustomTabsActivityHelper
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var customTabClient: CustomTabsClient

    private lateinit var customTabsHelperFragment: CustomTabsHelperFragment

    private val preloadUrl = Uri.parse("https://www.reddit.com/login.compact") //TODO: evaulate if this works

    private val customTabsFallback = CustomTabsActivityHelper.CustomTabsFallback {
        activity, uri -> activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        customTabsHelperFragment = CustomTabsHelperFragment.attachTo(this)
        customTabsHelperFragment.setConnectionCallback(object:CustomTabsActivityHelper.ConnectionCallback {
            override fun onCustomTabsConnected() {
                customTabsHelperFragment.mayLaunchUrl(preloadUrl, null, null)
            }
            override fun onCustomTabsDisconnected() {}
        })

        loginButton = findViewById(R.id.button_login)

        loginButton.setOnClickListener {
            val customTabIntent = CustomTabsIntent.Builder()
                    //TODO: Consider animations
                    //TODO: Add referrer
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .build()
            CustomTabsHelperFragment.open(this, customTabIntent, generateOauthUri(), customTabsFallback)
        }
    }

    private fun generateOauthUri() : Uri {
        val state = UUID.randomUUID().toString()
        val clientId = "dNjcxkM9pTQ0sQ"
        val redirectUri = "http://saveorganizer.github.io/redirect"
        val duration = "permanent"
        val scopes = "identity history"

        return Uri.parse("https://www.reddit.com/api/v1/authorize.compact?" +
                "client_id=$clientId&response_type=code&state=$state&redirect_uri=$redirectUri" +
                "&duration=$duration&scope=$scopes")

    }
}
