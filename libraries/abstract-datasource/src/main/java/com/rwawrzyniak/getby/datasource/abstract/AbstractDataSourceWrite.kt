package com.rwawrzyniak.getby.datasource.abstract

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import io.reactivex.Completable

interface AbstractDataSourceWrite<AE: AbstractEntity> {
	fun insert(entity: AE): Completable
	fun delete(entity: AE): Completable
	fun update(entity: AE): Completable
}
