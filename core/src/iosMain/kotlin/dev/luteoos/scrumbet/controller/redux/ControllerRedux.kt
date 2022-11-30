package dev.luteoos.scrumbet.controller.redux

// import dev.luteoos.scrumbet.controller.WindTurbineController
// import dev.luteoos.windturbine.controller.auth.AuthController
// import dev.luteoos.windturbine.controller.turbine.TurbineLiveStateController
// import dev.luteoos.windturbine.core.wrap

fun AuthController.watchIsAuthenticatedFlow() = this.getIsAuthenticatedFlow().wrap()

fun WindTurbineController.watchTurbineFlow() = this.getTurbineFlow().wrap()

fun TurbineLiveStateController.watchChartFlow() = this.getChartFlow().wrap()
