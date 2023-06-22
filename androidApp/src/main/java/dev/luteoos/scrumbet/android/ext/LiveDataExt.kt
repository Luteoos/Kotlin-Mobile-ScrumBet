package dev.luteoos.scrumbet.android.ext

import androidx.lifecycle.MutableLiveData

fun <T : Any> MutableLiveData<T>.notify() {
    this.value = value
}

fun <T : Any> MutableLiveData<T>.post(value: T) {
    if (hasActiveObservers())
        this.postValue(value)
}
