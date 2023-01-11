package dev.luteoos.scrumbet.android.di

import dev.luteoos.scrumbet.android.ui.main.MainViewModel
import dev.luteoos.scrumbet.android.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Add your DI here
 */

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { SplashViewModel() }
}

val uiModule = module {
    single { androidContext().packageManager }
}
