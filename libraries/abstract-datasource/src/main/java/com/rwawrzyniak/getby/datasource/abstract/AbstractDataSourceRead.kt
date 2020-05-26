package com.rwawrzyniak.getby.datasource.abstract

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import io.reactivex.Single

interface AbstractDataSourceRead<AE: AbstractEntity> {
	fun getById(id: String): Single<AE>
	fun getAll(): Single<List<AE>>
}
