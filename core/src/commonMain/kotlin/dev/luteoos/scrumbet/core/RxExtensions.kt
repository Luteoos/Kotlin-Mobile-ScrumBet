package dev.luteoos.scrumbet.core

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.observable.subscribeOn
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.scheduler.newThreadScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribe
import com.badoo.reaktive.single.subscribeOn
import dev.luteoos.scrumbet.shared.Log

fun <T> Observable<T>.resolve(
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit
): Disposable {
    return this.subscribeOn(newThreadScheduler)
        .observeOn(mainScheduler)
        .subscribe(
            onError = {
                Log.e(Exception(it))
                onError.invoke(it)
            },
            onComplete = { onComplete.invoke() },
            onNext = {
                onNext.invoke(it)
            }
        )
}

fun <T> Observable<T>.resolve(
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit
): Disposable {
    return this.resolve(onNext, onError, {})
}

fun <T> Observable<T>.resolve(
    onNext: (T) -> Unit
): Disposable {
    return this.resolve(onNext, {})
}

fun Completable.resolve(
    onComplete: () -> Unit,
    onError: (Throwable) -> Unit
): Disposable {
    return this.resolve(onComplete, onError)
}

fun Completable.resolve(
    onComplete: () -> Unit
): Disposable {
    return this.resolve(onComplete, {})
}

fun <T> Single<T>.resolve(
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit
): Disposable {
    return this.subscribeOn(newThreadScheduler)
        .observeOn(newThreadScheduler)
        .subscribe(
            onError = {
                Log.e(Exception(it))
                onError.invoke(it)
            },
            onSuccess = {
                onNext.invoke(it)
            }
        )
}

fun <T> Single<T>.resolve(
    onNext: (T) -> Unit
): Disposable {
    return this.resolve(onNext, {})
}
