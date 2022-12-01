package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.UserControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.UserData
import kotlinx.coroutines.flow.MutableStateFlow

class UserController : KController<UserData, AppException>(), UserControllerInterface {
    override val state: MutableStateFlow<KState<UserData, AppException>> = MutableStateFlow(KState.Loading())

}

fun a(){
    val a : UserControllerInterface = UserController()
    a.watchState()
    a.getStateFlow()
    a.onStart()
}