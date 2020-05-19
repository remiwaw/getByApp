package com.rwawrzyniak.getby.datasource.abstract

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import io.reactivex.Completable

interface AbstractDataSourceWrite<in T: AbstractEntity> {
	fun insert(entity: AbstractEntity): Completable
	fun delete(entity: AbstractEntity): Completable
	fun update(entity: AbstractEntity): Completable
}
