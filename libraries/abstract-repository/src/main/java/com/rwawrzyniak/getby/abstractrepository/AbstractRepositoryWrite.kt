package com.rwawrzyniak.getby.abstractrepository

import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import io.reactivex.Completable

interface AbstractRepositoryWrite<AM: AbstractModel> {
	fun insert(model: AM): Completable
	fun delete(model: AM): Completable
	fun update(model: AM): Completable
}
