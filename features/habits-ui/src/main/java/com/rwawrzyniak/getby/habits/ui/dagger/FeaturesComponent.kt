package com.rwawrzyniak.getby.dagger

import com.rwawrzyniak.getby.core.android.dagger.CoreComponent
import com.rwawrzyniak.getby.core.android.dagger.FeatureScope
import com.rwawrzyniak.getby.core.android.fragment.BaseFragment
import com.rwawrzyniak.getby.habits.ui.createupdate.HabitsCreateUpdateViewModelImpl
import com.rwawrzyniak.getby.habits.ui.details.HabitDetailsViewModelImpl
import com.rwawrzyniak.getby.habits.ui.overview.HabitsViewModelImpl
import dagger.Component

@Component(modules = [FeaturesComponent::class], dependencies = [CoreComponent::class])
@FeatureScope
interface FeaturesComponent {
    fun inject(mainActivity: BaseFragment)
	val habitDetailsViewModel: HabitDetailsViewModelImpl
	val habitsCreateUpdateViewModel: HabitsCreateUpdateViewModelImpl
	val habitsViewModelImpl: HabitsViewModelImpl

}
