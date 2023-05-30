package dev.luteoos.scrumbet.di

import dev.luteoos.scrumbet.BuildKonfig
import dev.luteoos.scrumbet.Greeting
import dev.luteoos.scrumbet.domain.repository.RoomRepositoryImpl
import dev.luteoos.scrumbet.domain.repository.ServerRepositoryImpl
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepository
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import dev.luteoos.scrumbet.domain.util.getHttpClient
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.preferences.SharedPreferencesImpl
import dev.luteoos.scrumbet.shared.DeviceData
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreModule = module {
    single<SharedPreferences> { SharedPreferencesImpl() }
    single { DeviceData() }
    single { getHttpClient() }

    single<RoomRepository> { RoomRepositoryImpl(get(), get(named("BASE_URL"))) }
    single<ServerRepository> { ServerRepositoryImpl(get(named("BASE_URL")), get(named("SSL_PREFIX")), get()) }

    single { Greeting() }
    single(named("APP_VERSION")) { BuildKonfig.appVersion }
    single(named("BASE_URL")) { BuildKonfig.baseUrl }
    single(named("SSL_PREFIX")) { BuildKonfig.sslPrefix }
}
