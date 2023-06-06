package dev.luteoos.scrumbet.android

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import dev.luteoos.scrumbet.android.di.uiModule
import dev.luteoos.scrumbet.android.di.viewModelModule
import dev.luteoos.scrumbet.core.UUID
import dev.luteoos.scrumbet.core.initKoin
import dev.luteoos.scrumbet.di.coreModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (dev.luteoos.scrumbet.android.BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        Timber.plant(FirebaseTree())
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

    private class FirebaseTree : Timber.Tree() {

        init {
            Firebase.analytics.setUserId(UUID.getNewUUID())
        }
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            val t = throwable ?: Exception(message)

            // Firebase Crash Reporting
            if (priority == Log.ERROR)
                FirebaseCrashlytics.getInstance().recordException(t)
            else
                FirebaseCrashlytics.getInstance().log(message)
        }
    }
}
