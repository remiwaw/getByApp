package com.rwawrzyniak.getby.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.dagger.SchedulerModule.SCHEDULER_PROVIDER
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

class HabitsViewModel @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>,
    @Named(SCHEDULER_PROVIDER) private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val compositeDisposable =  CompositeDisposable()

    val habitsState: MutableLiveData<HabitsState> = MutableLiveData(
        HabitsState(Calendar.getInstance())
    )

    init {
        globalEventSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe { habitsState.postValue(HabitsState(Calendar.getInstance())) }
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
