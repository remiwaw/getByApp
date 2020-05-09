package com.rwawrzyniak.getby.datasource.abstract

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import io.reactivex.Completable
import io.reactivex.Single

interface AbstractDataSource<T : AbstractEntity> {
	fun getById(id: String): Single<T>
	fun getAll(): Single<List<T>>
	fun insert(entity: AbstractEntity): Completable
	fun delete(entity: AbstractEntity): Completable
	fun update(entity: AbstractEntity): Completable
}
