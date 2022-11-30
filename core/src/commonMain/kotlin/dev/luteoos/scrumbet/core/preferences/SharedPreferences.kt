package dev.luteoos.scrumbet.core.preferences

import dev.luteoos.scrumbet.data.entity.ServerInfoEntity
import dev.luteoos.scrumbet.data.entity.TurbineEntity

interface SharedPreferences {
    fun getServerInfo(): ServerInfoEntity
    fun clearServerInfo()
    fun setServerInfo(value: ServerInfoEntity)
    fun getTurbineInfo(): TurbineEntity
    fun clearTurbineInfo()
    fun setTurbineInfo(value: TurbineEntity)
    fun clearAll()
}
