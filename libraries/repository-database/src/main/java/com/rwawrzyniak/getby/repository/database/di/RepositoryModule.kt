package com.rwawrzyniak.getby.repository.database.di

import com.rwawrzyniak.getby.abstractconverter.AbstractConverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.converters.HabitConverter
import com.rwawrzyniak.getby.database.HabitDao
import com.rwawrzyniak.getby.datasource.abstract.AbstractDataSource
import com.rwawrzyniak.getby.repository.database.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideHabitsRepository(@Named("HabitDao") dataSource: AbstractDataSource<AbstractEntity>,
								@Named("HabitConverter") converter: AbstractConverter<AbstractModel, AbstractEntity>
	) = HabitsRepository(dataSource as HabitDao, converter as HabitConverter)
}
