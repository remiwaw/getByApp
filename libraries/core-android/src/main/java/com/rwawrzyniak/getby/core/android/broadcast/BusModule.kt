package com.rwawrzyniak.getby.core.android.broadcast
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Module
object BusModule {
    const val GLOBAL_EVENT_SUBJECT = "GLOBAL_EVENT_SUBJECT"
    const val MENU_ITEM_CLICKED_SUBJECT = "MENU_ITEM_CLICKED_SUBJECT"

    @Singleton
    @Provides
    @Named(GLOBAL_EVENT_SUBJECT)
    fun provideGlobalEventSubject(): PublishSubject<GlobalEvent> = PublishSubject.create()

	@Singleton
	@Provides
	@Named(MENU_ITEM_CLICKED_SUBJECT)
	fun provideMenuItemClickedSubject(): PublishSubject<MenuItemClickedEvent> = PublishSubject.create()

}
