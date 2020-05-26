package com.rwawrzyniak.getby.abstractrepository

import com.rwawrzyniak.getby.abstractconverter.AbstractConverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.datasource.abstract.AbstractDataSource
import io.reactivex.Completable
import io.reactivex.Single

abstract class AbstractRepository<AE: AbstractEntity, AM: AbstractModel>(
	private val abstractDataSource : AbstractDataSource<AE>,
	private val abstractConverter: AbstractConverter<AM, AE>
) {

	fun getById(id: String): Single<AM> = abstractDataSource.getById(id).map { abstractConverter.toModel(it) }

	fun getAll(): Single<MutableList<AM>> = abstractDataSource.getAll().flattenAsObservable {it}.map { abstractConverter.toModel(it) }.toList()

	fun insert(model: AM): Completable = abstractDataSource.insert(abstractConverter.toEntity(model))

	fun delete(model: AM): Completable = abstractDataSource.delete(abstractConverter.toEntity(model))

	fun update(model: AM): Completable = abstractDataSource.update(abstractConverter.toEntity(model))
}
