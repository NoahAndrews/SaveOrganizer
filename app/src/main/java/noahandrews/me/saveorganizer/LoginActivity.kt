package noahandrews.me.saveorganizer

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.util.TimingLogger
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment
import noahandrews.me.saveorganizer.oauth.InvalidStateParameterException
import noahandrews.me.saveorganizer.oauth.OauthManager
import org.chromium.customtabsclient.CustomTabsActivityHelper
import java.net.URI
import javax.inject.Inject


//FIXME: The next thing to do before testing and committing is to set up Dagger here.

class LoginActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName

    private lateinit var customTabsHelperFragment: CustomTabsHelperFragment

    @Inject lateinit var oauthManager : OauthManager

    private val preloadUrl = Uri.parse("https://www.reddit.com/login.compact") //TODO: evaluate if this works

    private val customTabsFallback = CustomTabsActivityHelper.CustomTabsFallback {
        activity, uri -> activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }


    // TODO: Attempt to reuse the credentials from other reddit apps using AccountManager, but only
    // for API 26 and higher. We don't want to have to request access to Contacts.

    //TODO: Test what happens when I let the reddit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (application as SOApplication).applicationComponent.newActivityComponent().inject(this)

        customTabsHelperFragment = CustomTabsHelperFragment.attachTo(this)
        customTabsHelperFragment.setConnectionCallback(object:CustomTabsActivityHelper.ConnectionCallback {
            override fun onCustomTabsConnected() {
                customTabsHelperFragment.mayLaunchUrl(preloadUrl, null, null)
                // TODO: preload the authorization URL as well.
            }
            override fun onCustomTabsDisconnected() {}
        })

        loginButton.setOnClickListener {
            launch(CommonPool) {
                val timer = TimingLogger(TAG, "loginButton")
                val customTabIntent = CustomTabsIntent.Builder()
                        //TODO: Consider animations
                        //TODO: Add referrer
                        .setToolbarColor(ContextCompat.getColor(this@LoginActivity, R.color.colorPrimary))
                        .build()
                CustomTabsHelperFragment.open(this@LoginActivity, customTabIntent, Uri.parse(oauthManager.generateInitialRequest().await()), customTabsFallback)
                timer.dumpToLog()
            }
        }
    }

    override fun onNewIntent(newIntent: Intent?) {
        if (newIntent?.action == Intent.ACTION_VIEW && newIntent.data != null) try {
            val javaUri = URI(newIntent.data.toString())
            val androidUri = newIntent.data

//            oauthManager.processOauthRedirect(newIntent.data)
        } catch (exception: InvalidStateParameterException) {
            Toast.makeText(this, "Received unrecognized reddit authorization.", Toast.LENGTH_LONG).show()
            //FIXME: Handle an unexpected state parameter
        }
    }
}
