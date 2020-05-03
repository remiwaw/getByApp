package com.rwawrzyniak.getby.core.android.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ObservableList<T> (
    private val list: MutableList<T> = mutableListOf(),
    private val onAdd: PublishSubject<List<T>> = PublishSubject.create()
) {
    fun add(value: T) {
        list.add(value)
        onAdd.onNext(list)
    }

    fun observe(): Observable<List<T>> = onAdd.startWith(list).hide()
}
