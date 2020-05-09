package com.rwawrzyniak.getby.abstractrepository

import com.rwawrzyniak.getby.abstractconverter.AbstractConverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.datasource.abstract.AbstractDataSource
import io.reactivex.Completable
import io.reactivex.Single

// TODO Make abstract repository independent of Entity
abstract class AbstractRepository<
	T: AbstractModel,
	ADS : AbstractDataSource<out AbstractEntity>,
	AC: AbstractConverter<out AbstractModel, out AbstractEntity>>(
	private val abstractDataSource: ADS,
	private val abstractConverter: AC
){
	fun getById(id: String): Single<AbstractModel> =
		abstractDataSource.getById(id).map { abstractConverter.toModel(it) }

	fun getAll(): Single<MutableList<AbstractModel>> =
		abstractDataSource.getAll().flattenAsObservable {it}.map { abstractConverter.toModel(it) }.toList()

	fun insert(model: T): Completable = abstractDataSource.insert(abstractConverter.toEntity(model))

	fun delete(model: T): Completable = abstractDataSource.delete(abstractConverter.toEntity(model))

	fun update(model: T): Completable = abstractDataSource.update(abstractConverter.toEntity(model))
}
