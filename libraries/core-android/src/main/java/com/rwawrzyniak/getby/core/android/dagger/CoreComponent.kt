package com.rwawrzyniak.getby.core.android.dagger

import com.rwawrzyniak.getby.core.android.ExpensiveObject
import com.rwawrzyniak.getby.core.android.broadcast.GlobalEvent
import com.rwawrzyniak.getby.core.android.broadcast.MenuItemClickedEvent
import com.rwawrzyniak.getby.core.android.rx.SchedulerProvider
import dagger.Component
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Component(modules = [CoreModule::class, BusModule::class, 	FirebaseAuthModule::class,
	SchedulerModule::class,
	BusModule::class,
	RepositoryModule::class,
	DatabaseModule::class])
@Singleton
interface CoreComponent {
    fun getExpensiveObject(): ExpensiveObject
	@Named("schedulerProvider") fun provideSchedulerProvider(): SchedulerProvider
	@Named(BusModule.GLOBAL_EVENT_SUBJECT) fun getGlobalEventSubject(): PublishSubject<GlobalEvent>
	@Named(BusModule.MENU_ITEM_CLICKED_SUBJECT) fun getMenuItemClickedEvent(): PublishSubject<MenuItemClickedEvent>

}
