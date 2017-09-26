package noahandrews.me.saveorganizer.di.application

import dagger.Component
import noahandrews.me.saveorganizer.di.activity.ActivityComponent
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun newActivityComponent() : ActivityComponent
}