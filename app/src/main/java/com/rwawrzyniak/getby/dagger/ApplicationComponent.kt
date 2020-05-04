package com.rwawrzyniak.getby.dagger

import android.content.Context
import android.content.SharedPreferences
import com.rwawrzyniak.getby.core.android.broadcast.GlobalEvent
import com.rwawrzyniak.getby.common.PreferencesHelper
import com.rwawrzyniak.getby.core.android.broadcast.BusModule
import com.rwawrzyniak.getby.core.android.broadcast.MenuItemClickedEvent
import com.rwawrzyniak.getby.core.android.rx.SchedulerProvider
import com.rwawrzyniak.getby.habits.overview.HabitsViewModelImpl
import com.rwawrzyniak.getby.habits.createupdate.HabitsCreateUpdateViewModelImpl
import com.rwawrzyniak.getby.habits.details.HabitDetailsViewModelImpl
import com.rwawrzyniak.getby.login.LoginViewModel
import com.rwawrzyniak.getby.register.RegisterViewModel
import dagger.BindsInstance
import dagger.Component
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9
@Singleton
@Component(modules = [FirebaseAuthModule::class, SchedulerModule::class, BusModule::class, RepositoryModule::class, DatabaseModule::class, ApplicationModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context
        ): ApplicationComponent
    }

    val habitDetailsViewModel: HabitDetailsViewModelImpl
    val habitsCreateUpdateViewModel: HabitsCreateUpdateViewModelImpl
    val habitsViewModelImpl: HabitsViewModelImpl
    val loginViewModel: LoginViewModel
    val registerViewModel: RegisterViewModel
    @Named("schedulerProvider") fun provideSchedulerProvider(): SchedulerProvider
    @Named(BusModule.GLOBAL_EVENT_SUBJECT) fun getGlobalEventSubject(): PublishSubject<GlobalEvent>
    @Named(BusModule.MENU_ITEM_CLICKED_SUBJECT) fun getMenuItemClickedEvent(): PublishSubject<MenuItemClickedEvent>
	fun prefManager(): SharedPreferences
	fun preferencesHelper(): com.rwawrzyniak.getby.common.PreferencesHelper
}
