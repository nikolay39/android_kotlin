package com.example.android.sunshine.di


import android.content.Context
import com.example.android.sunshine.detail.DetailFragment
import com.example.android.sunshine.di.modules.*
import com.example.android.sunshine.di.modules.viewmodel.CommonUiModule
import com.example.android.sunshine.di.modules.viewmodel.MyUiBuilderModule
import com.example.android.sunshine.overview.OverviewFragment
import com.example.android.sunshine.repository.ForecastRepository
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton
//https://github.com/mlykotom/connecting-the-dots-sample/blob/master/app/src/main/java/com/mlykotom/connectingthedots/presentation/BaseFragment.kt
@Singleton
@Component(
    modules = [
        //AppModule::class,
        RepoModule::class,
        //AppCommonModule::class,
        CommonUiModule::class,
        MyUiBuilderModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
    //fun context(): Context
    //fun application(): Application
    //fun sharedPrefs(): SharedPreferences

    //fun detailviewComponent(): DetailViewComponent.Factory
    val forecastRepository: ForecastRepository
    fun inject(overviewFragment: OverviewFragment)
    fun inject(detailFragment: DetailFragment)
}
object SubcomponentsModule