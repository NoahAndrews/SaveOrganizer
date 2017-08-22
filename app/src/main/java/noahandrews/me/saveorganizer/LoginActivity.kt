package noahandrews.me.saveorganizer

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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

    private val CHOOSE_ACCOUNT_REQUEST = 1

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

        appLoginButton.setOnClickListener {
            var chooseAccountIntent: Intent
//            var authenticatorTypes = AccountManager.get(this).authenticatorTypes
            val redditPackages = arrayOf("com.reddit.account")
            val accountChooserPrompt = "Select your reddit username:"
            // TODO: figure out how to filter out the "Reddit for Android" account entry
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                chooseAccountIntent = AccountManager.newChooseAccountIntent(null, null, redditPackages, accountChooserPrompt, null, null,
                        null)
            } else {
                chooseAccountIntent = AccountManager.newChooseAccountIntent(null, null, redditPackages, false,accountChooserPrompt, null, null, null)
            }
            startActivityForResult(chooseAccountIntent, CHOOSE_ACCOUNT_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CHOOSE_ACCOUNT_REQUEST && resultCode == Activity.RESULT_OK) {
            val accountName = data?.extras!![AccountManager.KEY_ACCOUNT_NAME] //TODO: figure out how to get rid of !!
            val accountType = data.extras!![AccountManager.KEY_ACCOUNT_TYPE]
            val accounts = AccountManager.get(this).accounts
            // TODO: Findings. While requesting the contact permission shouldn't be necessary on O,
            // there doesn't seem to be a way to filter out the extra account entry.
            // Perhaps the Reddit app will eventually do that for me once it targets O.
            // I would have to generate a separate APK for lollipop devices if I want to have the feature only on API 23+.
            // API 26+ shouldn't require the permission at all, I think.
            val i =0
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
