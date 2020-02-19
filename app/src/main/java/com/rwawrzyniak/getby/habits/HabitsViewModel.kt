package com.rwawrzyniak.getby.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar
import javax.inject.Inject

class HabitsViewModel @Inject internal constructor(

) : ViewModel() {
    val habitsState: MutableLiveData<HabitsState> = MutableLiveData(
        HabitsState(Calendar.getInstance())
    )
}
