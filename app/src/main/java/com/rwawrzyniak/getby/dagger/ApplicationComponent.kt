package com.rwawrzyniak.getby.dagger

import android.content.Context
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.habits.HabitsViewModel
import com.rwawrzyniak.getby.login.LoginViewModel
import com.rwawrzyniak.getby.register.RegisterViewModel
import dagger.BindsInstance
import dagger.Component
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9
@Singleton
@Component(modules = [FirebaseAuthModule::class, SchedulerModule::class, BusModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context
        ): ApplicationComponent
    }

    val habitsViewModel: HabitsViewModel
    val loginViewModel: LoginViewModel
    val registerViewModel: RegisterViewModel
    @Named("schedulerProvider") fun provideSchedulerProvider(): SchedulerProvider
    @Named(BusModule.GLOBAL_EVENT_SUBJECT) fun getGlobalEventSubject(): PublishSubject<GlobalEvent>
}
