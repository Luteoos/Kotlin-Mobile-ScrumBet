package dev.luteoos.scrumbet.android.di

import dev.luteoos.scrumbet.android.ui.auth.AuthViewModel
import dev.luteoos.scrumbet.android.ui.main.MainViewModel
import dev.luteoos.scrumbet.android.ui.room.RoomViewModel
import dev.luteoos.scrumbet.android.ui.splash.SplashViewModel
import dev.luteoos.scrumbet.controller.AuthController
import dev.luteoos.scrumbet.controller.RoomController
import dev.luteoos.scrumbet.controller.UserController
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.controller.interfaces.RoomControllerInterface
import dev.luteoos.scrumbet.controller.interfaces.UserControllerInterface
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Add your DI here
 */

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RoomViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }

    single<AuthControllerInterface>(createdAtStart = true) { AuthController(get(), get()) }
    factory<UserControllerInterface> { UserController(get(), get()) }
    factory<RoomControllerInterface> { RoomController() }
}

val uiModule = module {
    single { androidContext().packageManager }
}
