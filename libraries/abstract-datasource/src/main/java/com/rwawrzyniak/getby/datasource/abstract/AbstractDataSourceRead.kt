package com.rwawrzyniak.getby.datasource.abstract

import io.reactivex.Single

interface AbstractDataSourceRead<AbstractEntity> {
	fun getById(id: String): Single<AbstractEntity>
	fun getAll(): Single<List<AbstractEntity>>
}
