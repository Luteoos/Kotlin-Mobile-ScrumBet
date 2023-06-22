package dev.luteoos.scrumbet.android.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

abstract class BaseViewModel : ViewModel(), KoinComponent {

//    protected val compositeDisposable = CompositeDisposable()

    val hideKeyboard = MutableLiveData<Unit>()

//    protected fun start(disposable: Disposable) {
//        compositeDisposable.add(disposable)
//    }
//
//    override fun onCleared() {
//        compositeDisposable.clear()
//    }
}
