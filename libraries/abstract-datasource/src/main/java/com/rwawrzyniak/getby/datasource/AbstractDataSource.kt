package com.rwawrzyniak.getby.datasource

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import io.reactivex.Completable
import io.reactivex.Single

interface AbstractDataSource<AE : AbstractEntity> {
	fun getById(id: String): Single<AE>
	fun getAll(): Single<List<AE>>
	fun insert(entity: AE): Completable
	fun delete(entity: AE): Completable
	fun update(entity: AE): Completable
}
