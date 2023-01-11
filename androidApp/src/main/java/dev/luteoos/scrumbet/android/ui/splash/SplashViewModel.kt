package dev.luteoos.scrumbet.android.ui.splash

import androidx.lifecycle.MutableLiveData
import dev.luteoos.scrumbet.android.core.BaseViewModel

class SplashViewModel : BaseViewModel() {
    val onReady: MutableLiveData<Boolean> = MutableLiveData(true)
}
