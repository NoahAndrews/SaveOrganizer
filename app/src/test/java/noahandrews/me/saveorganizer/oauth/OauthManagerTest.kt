package noahandrews.me.saveorganizer.oauth

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import com.google.common.base.Stopwatch
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import noahandrews.me.saveorganizer.TestPerfTimerFactory
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.junit.Rule
import java.net.URI

@SuppressLint("CommitPrefEdits")
class OauthManagerTest {
    @Rule @JvmField val mockitoRule = MockitoJUnit.rule()!!

    lateinit var oauthManager : OauthManager
    @Mock lateinit var applicationMock: Application
    @Mock lateinit var sharedPrefsMock: SharedPreferences
    @Mock lateinit var sharedPrefsEdMock: SharedPreferences.Editor

    @Before
    fun setup() {
        val timer = Stopwatch.createStarted()

        //Setup SharedPreferences mocks
        When calling applicationMock.getSharedPreferences(any(), any()) itReturns sharedPrefsMock
        When calling sharedPrefsMock.edit() itReturns sharedPrefsEdMock
        When calling sharedPrefsEdMock.putString(any(), any()) itReturns sharedPrefsEdMock

//        When calling authRequestGenerator.generate(any(), any(), any(),state = )

        oauthManager = OauthManager(applicationMock, TestPerfTimerFactory())
        timer.stop()
    }

    @Test
    fun generateInitialRequestTest() {
        runBlocking<Unit> {
            val uri = URI(oauthManager.generateInitialRequest().await())
            val returnedState = extractState(uri, TestPerfTimerFactory())
            Verify on sharedPrefsEdMock that sharedPrefsEdMock.putString(oauthManager.STATE_PREF, returnedState.toString()) was called
            //FIXME: This test is incomplete
            //FIXME: Test that I get a new request every time
        }
    }

    @Test
    fun directUserToAuthTest() {
        oauthManager.directUserToAuth()
    }



    //FIXME: Test the state extractor

}