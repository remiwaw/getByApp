package com.rwawrzyniak.getby.core.android.di

import com.google.firebase.auth.FirebaseAuth
import com.rwawrzyniak.getby.core.android.broadcast.GlobalEvent
import com.rwawrzyniak.getby.core.android.broadcast.MenuItemClickedEvent
import com.rwawrzyniak.getby.core.android.rx.DefaultSchedulerProvider
import com.rwawrzyniak.getby.core.android.rx.TestSchedulerProvider
import io.reactivex.subjects.PublishSubject
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceDatabaseModule =
	module {
		single{
			FirebaseAuth.getInstance()
		}
		single(named(GLOBAL_EVENT_SUBJECT)) {
			PublishSubject.create<GlobalEvent>()
		}
		single(named(MENU_ITEM_CLICKED_SUBJECT)) {
			PublishSubject.create<MenuItemClickedEvent>()
		}
		factory(named(SCHEDULER_PROVIDER)) {
			DefaultSchedulerProvider
		}
		factory(named(TEST_SCHEDULER_PROVIDER)) {
			TestSchedulerProvider.create()
		}
	}

const val GLOBAL_EVENT_SUBJECT = "GLOBAL_EVENT_SUBJECT"
const val MENU_ITEM_CLICKED_SUBJECT = "MENU_ITEM_CLICKED_SUBJECT"

const val SCHEDULER_PROVIDER = "schedulerProvider"
const val TEST_SCHEDULER_PROVIDER = "testSchedulerProvider"
