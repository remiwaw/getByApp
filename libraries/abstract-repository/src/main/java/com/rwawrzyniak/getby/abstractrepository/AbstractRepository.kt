package com.rwawrzyniak.getby.abstractrepository

import com.rwawrzyniak.getby.abstractconverter.Converter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.datasource.AbstractDataSource
import io.reactivex.Completable
import io.reactivex.Single

abstract class AbstractRepository<AE: AbstractEntity, AM: AbstractModel>(
	private val abstractDataSource : AbstractDataSource<AE>,
	private val abstractConverter: Converter<AM, AE>
) {

	open fun getById(id: String): Single<AM> = abstractDataSource.getById(id).map { abstractConverter.toModel(it) }

	open fun getAll(): Single<MutableList<AM>> = abstractDataSource.getAll().flattenAsObservable {it}.map { abstractConverter.toModel(it) }.toList()

	open fun insert(model: AM): Completable = abstractDataSource.insert(abstractConverter.toEntity(model))

	open fun delete(model: AM): Completable = abstractDataSource.delete(abstractConverter.toEntity(model))

	open fun update(model: AM): Completable = abstractDataSource.update(abstractConverter.toEntity(model))

	open fun test(): Unit = TODO()
}
