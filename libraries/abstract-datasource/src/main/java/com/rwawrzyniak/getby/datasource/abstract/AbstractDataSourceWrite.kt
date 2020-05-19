package com.rwawrzyniak.getby.datasource.abstract

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import io.reactivex.Completable

interface AbstractDataSourceWrite<in T: AbstractEntity> {
	fun insert(entity: T): Completable
	fun delete(entity: T): Completable
	fun update(entity: T): Completable
}
