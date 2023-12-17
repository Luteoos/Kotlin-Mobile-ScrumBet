package dev.luteoos.scrumbet.android

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import dev.luteoos.scrumbet.android.di.uiModule
import dev.luteoos.scrumbet.android.di.viewModelModule
import dev.luteoos.scrumbet.core.initKoin
import dev.luteoos.scrumbet.di.coreModule
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.PlatformBuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.BuildConfig
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber

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
        val userAnalyticsId = get<SharedPreferences>().getUserAnalyticsId()
        if (dev.luteoos.scrumbet.android.BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        Timber.plant(FirebaseTree(userAnalyticsId))
        if (BuildConfig.DEBUG)
            initDebugStuff()
        initOneSignal(PlatformBuildConfig.getOneSignalAppId(), externalAnalyticsId = userAnalyticsId)
//        if (DynamicColors.isDynamicColorAvailable())
//            DynamicColors.applyToActivitiesIfAvailable(this) // MaterialYou
    }

    private fun initOneSignal(appId: String, externalAnalyticsId: String) {
        // Verbose Logging set to help debug issues, remove before releasing your app.
        if (dev.luteoos.scrumbet.android.BuildConfig.DEBUG)
            OneSignal.Debug.logLevel = LogLevel.VERBOSE
        else
            OneSignal.Debug.logLevel = LogLevel.NONE
        OneSignal.initWithContext(this, appId)
        OneSignal.login(externalAnalyticsId)
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
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

    private class FirebaseTree(userID: String?) : Timber.Tree() {

        init {
            Firebase.analytics.setUserId(userID)
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
