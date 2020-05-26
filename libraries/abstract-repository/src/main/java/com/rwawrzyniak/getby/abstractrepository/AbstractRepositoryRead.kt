package com.rwawrzyniak.getby.abstractrepository

import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import io.reactivex.Single

interface AbstractRepositoryRead<AM: AbstractModel> {
	fun getById(id: String): Single<AM>
	fun getAll(): Single<MutableList<AM>>
}
