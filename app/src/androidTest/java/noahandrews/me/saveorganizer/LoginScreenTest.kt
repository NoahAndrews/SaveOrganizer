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

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginScreenTest {
    @Rule @JvmField val activityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var device: UiDevice
    private lateinit var selector: UiSelector

    @Before fun setupUiAutomator() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        selector = UiSelector()
    }


    // Deprecated setup for logging into reddit within a test
  /*
    private lateinit var redditUsername: String
    private lateinit var redditPassword: String
    private lateinit var redditSaveTitle: String
    @Before fun setupRedditCreds() {
        //TODO: replace these fake ones with ones from Gradle
        redditUsername = BuildConfig.REDDIT_TEST_USERNAME
        redditPassword = BuildConfig.REDDIT_TEST_PASSWORD
        redditSaveTitle = BuildConfig.REDDIT_TEST_POST_TITLE
    }*/

    //TODO: Log out in a Before method

    //TODO: Add a test that only pretends to log into Reddit, using Espresso's intent mocking thing

    /**
     * The purpose of a test that actually logs in to Reddit is to verify that nothing has changed with Reddit's oauth setup or anything.
     */
    @Test fun loginToReddit() {
        onView(withId(R.id.button_login)).perform(click())

        // My original approach was to log into reddit as a part of the test.
        // See the readme for what to do instead. The old method depended on the tester being logged
        // out of reddit in Chrome, the new one depends on them being logged in. Putting this here
        // so that it's in the git history, in case there's an issue with the new way.

        /*val usernameField = device.findObject(selector.resourceId("user_login"))
        usernameField.click()
        usernameField.text = redditUsername
        device.pressEnter()

        val passwordField = device.findObject(selector.resourceId("passwd_login"))
        passwordField.click()
        passwordField.text = redditPassword
        device.pressEnter()

        // Click on something to dismiss the keyboard
        val login = device.findObject(selector.descriptionContains("log in or sign up"))
        login.click()

        val loginButton = device.findObject(selector.className("android.widget.Button"))
        loginButton.click()*/

        val authorizeButton = device.findObject(selector.description("Allow"))
        authorizeButton.click()

        throw RuntimeException()

        // TODO: See this article for how to do this correctly: https://medium.com/google-developers/adapterviews-and-espresso-f4172aa853cf
//        onData(allOf(is(instanceOf(Map::class.java)), hasEntry(equalTo(MainActivity.ROW), is(redditSaveTitle)))

    }

    // TODO: Make another test that checks for graceful handling of declination
}
