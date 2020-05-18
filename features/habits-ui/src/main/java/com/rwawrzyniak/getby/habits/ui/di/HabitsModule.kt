package com.rwawrzyniak.getby.habits.ui.di

import com.rwawrzyniak.getby.habits.ui.createupdate.HabitsCreateUpdateViewModelImpl
import com.rwawrzyniak.getby.habits.ui.overview.HabitsViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val habitsModule = module {
  viewModel { HabitsCreateUpdateViewModelImpl(get(), get(), get()) }
  viewModel { HabitsViewModelImpl(get(), get(), get(), get()) }
  viewModel { HabitsCreateUpdateViewModelImpl(get(), get(), get()) }
}
