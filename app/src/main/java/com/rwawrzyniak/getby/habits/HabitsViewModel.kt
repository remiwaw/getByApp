package com.rwawrzyniak.getby.habits

import BusModule.GLOBAL_EVENT_SUBJECT
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.GlobalEvent
import io.reactivex.subjects.PublishSubject
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

class HabitsViewModel @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>
) : ViewModel() {

    val habitsState: MutableLiveData<HabitsState> = MutableLiveData(
        HabitsState(Calendar.getInstance())
    )
}
