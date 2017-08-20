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

    //TODO: Log out in a Before method

    //TODO: Add a test that only pretends to log into Reddit, using Espresso's intent mocking thing

    /**
     * The purpose of a test that actually logs in to Reddit is to verify that nothing has changed with Reddit's oauth setup or anything.
     * See the readme for how to set up and log into a reddit account that will allow this test to pass.
     */
    @Test fun loginToReddit() {
        onView(withId(R.id.button_login)).perform(click())

        val authorizeButton = device.findObject(selector.description("Allow"))
        authorizeButton.click()

        // This test is unfinished, so make sure it fails for now.
        throw RuntimeException()

        // TODO: See this article for how to do this correctly: https://medium.com/google-developers/adapterviews-and-espresso-f4172aa853cf
//        onData(allOf(is(instanceOf(Map::class.java)), hasEntry(equalTo(MainActivity.ROW), is(redditSaveTitle)))

    }

    // TODO: Make another test that checks for graceful handling of declination
}
