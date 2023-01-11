package dev.luteoos.scrumbet.android

import android.app.Application
import android.os.StrictMode
import dev.luteoos.scrumbet.android.di.uiModule
import dev.luteoos.scrumbet.android.di.viewModelModule
import dev.luteoos.scrumbet.core.initKoin
import dev.luteoos.scrumbet.di.coreModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            if (BuildConfig.DEBUG)
                androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(
                coreModule,
                uiModule,
                viewModelModule
            )
        }
//        if (DynamicColors.isDynamicColorAvailable())
//            DynamicColors.applyToActivitiesIfAvailable(this) // MaterialYou
//        initKoin {
//            if (BuildConfig.DEBUG)
//                androidLogger(Level.ERROR)
//            androidContext(this@MainApplication)
//            modules(
//                listOf(
//                    viewModelsModule,
//                    appModule,
//                    useCaseModule,
//                    uiModule,
//                    dataModule,
//                    repositoryModule
//                )
//            )
//        }
        if (BuildConfig.DEBUG)
            initDebugStuff()
    }

    private fun initDebugStuff() {
//        Timber.plant(Timber.DebugTree())
//        Timber.i("initDebug Policy")
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }
}
