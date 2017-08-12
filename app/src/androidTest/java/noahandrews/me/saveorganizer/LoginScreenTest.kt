package noahandrews.me.saveorganizer

import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import org.junit.Before
import org.hamcrest.core.AllOf.*
import org.hamcrest.core.Is.*
import org.hamcrest.core.IsInstanceOf.*
import org.hamcrest.core.IsEqual.*
import org.hamcrest.collection.IsMapContaining.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginScreenTest {
    @Rule @JvmField public val activityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var device: UiDevice
    private lateinit var selector: UiSelector

    private lateinit var redditUsername: String
    private lateinit var redditPassword: String
    private lateinit var redditSaveTitle: String

    @Before fun setupUiAutomator() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        selector = UiSelector()
    }

    @Before public fun setupRedditCreds() {

    }

    //TODO: Log out in a Before method

    @Test public fun loginToReddit() {
        onView(withId(R.id.button_login)).perform(click())

        val usernameField = device.findObject(selector.resourceId("username"))
        usernameField.click()
        usernameField.text = redditUsername

        val passwordField = device.findObject(selector.resourceId("password"))
        passwordField.click()
        passwordField.text = redditPassword

        val loginButton = device.findObject(selector.resourceId("login"))
        loginButton.click()

        val authorizeButton = device.findObject(selector.resourceId("authorize"))
        authorizeButton.click()

        //See this article for how to do this correctly: https://medium.com/google-developers/adapterviews-and-espresso-f4172aa853cf
//        onData(allOf(is(instanceOf(Map::class.java)), hasEntry(equalTo(MainActivity.ROW), is(redditSaveTitle)))

    }
}
