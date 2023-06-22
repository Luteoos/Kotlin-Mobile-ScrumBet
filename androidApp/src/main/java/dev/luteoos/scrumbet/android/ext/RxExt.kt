package dev.luteoos.scrumbet.android.ext
//
// import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
// import io.reactivex.rxjava3.core.Flowable
// import io.reactivex.rxjava3.core.Observable
// import io.reactivex.rxjava3.core.Single
// import io.reactivex.rxjava3.disposables.Disposable
// import io.reactivex.rxjava3.schedulers.Schedulers
// import timber.log.Timber
//
// fun <T> Observable<T>.resolve(
//    onNext: (T) -> Unit,
//    onError: (Throwable) -> Unit,
//    onComplete: () -> Unit
// ): Disposable {
//    return this.subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(
//            { onNext.invoke(it) },
//            {
//                Timber.e(it)
//                onError.invoke(it)
//            },
//            { onComplete.invoke() }
//        )
// }
//
// fun <T> Observable<T>.resolve(
//    onNext: (T) -> Unit,
//    onError: (Throwable) -> Unit
// ): Disposable {
//    return this.resolve(onNext, onError, {})
// }
//
// fun <T> Observable<T>.resolve(
//    onNext: (T) -> Unit
// ): Disposable {
//    return this.resolve(onNext, {})
// }
//
// fun <T> Flowable<T>.resolve(
//    onNext: (T) -> Unit,
//    onError: (Throwable) -> Unit,
//    onComplete: () -> Unit
// ): Disposable {
//    return this.subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(
//            { onNext.invoke(it) },
//            {
//                Timber.e(it)
//                onError.invoke(it)
//            },
//            { onComplete.invoke() }
//        )
// }
//
// fun <T> Flowable<T>.resolve(
//    onNext: (T) -> Unit,
//    onError: (Throwable) -> Unit
// ): Disposable {
//    return this.resolve(onNext, onError, {})
// }
//
// fun <T> Flowable<T>.resolve(
//    onNext: (T) -> Unit
// ): Disposable {
//    return this.resolve(onNext, {})
// }
//
// fun <T> Single<T>.resolve(
//    onNext: (T) -> Unit,
//    onError: (Throwable) -> Unit
// ): Disposable {
//    return this.subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(
//            { onNext.invoke(it) },
//            {
//                Timber.e(it)
//                onError.invoke(it)
//            }
//        )
// }
//
// fun <T> Single<T>.resolve(
//    onNext: (T) -> Unit
// ): Disposable {
//    return this.resolve(onNext, {})
// }
