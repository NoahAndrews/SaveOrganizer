package noahandrews.me.saveorganizer

import android.app.Application
import noahandrews.me.saveorganizer.di.application.ApplicationComponent
import noahandrews.me.saveorganizer.di.application.ApplicationModule
import noahandrews.me.saveorganizer.di.application.DaggerApplicationComponent

class SOApplication : Application() {
    val applicationComponent : ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

}