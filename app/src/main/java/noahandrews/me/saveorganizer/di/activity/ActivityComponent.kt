package noahandrews.me.saveorganizer.di.activity

import dagger.Subcomponent
import noahandrews.me.saveorganizer.LoginActivity

@ActivityScoped
@Subcomponent
interface ActivityComponent {
    fun inject(activity : LoginActivity)
}