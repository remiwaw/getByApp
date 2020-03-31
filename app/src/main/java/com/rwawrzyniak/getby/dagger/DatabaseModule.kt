package com.rwawrzyniak.getby.dagger

import android.content.Context
import androidx.room.Room
import com.rwawrzyniak.getby.core.AppDatabase
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {
    @JvmStatic @Provides
    fun provideDatabase(context: Context) = Room.databaseBuilder(
		context,
		AppDatabase::class.java, "GetBy.db"
	).build()

}
