package dev.luteoos.scrumbet.di

import dev.luteoos.scrumbet.Greeting
import dev.luteoos.scrumbet.domain.repository.RoomRepositoryImpl
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepository
import dev.luteoos.scrumbet.domain.util.getHttpClient
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.preferences.SharedPreferencesImpl
import dev.luteoos.scrumbet.shared.DeviceData
import org.koin.dsl.module

val coreModule = module {
    single<SharedPreferences> { SharedPreferencesImpl() }
    single { DeviceData() }
    single { getHttpClient() }

    single<RoomRepository> { RoomRepositoryImpl(get()) }
//    single<AuthRepository> { AuthRepositoryImpl() }
//    single<TurbineLiveRepository> { TurbineLiveRepositoryImpl(get()) }

    single { Greeting() }
//    single { TestTurbineRepository(get()) }
}
