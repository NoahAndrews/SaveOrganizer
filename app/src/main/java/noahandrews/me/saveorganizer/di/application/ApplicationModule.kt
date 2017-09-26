package noahandrews.me.saveorganizer.di.application

import android.app.Application
import dagger.Module
import dagger.Provides
import noahandrews.me.saveorganizer.util.AndroidPerfTimerFactory
import noahandrews.me.saveorganizer.util.PerfTimerFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @Singleton
    fun application() : Application {
        return application
    }

    @Provides
    @Singleton
    fun perfTimerFactory() : PerfTimerFactory {
        return AndroidPerfTimerFactory()
    }
}