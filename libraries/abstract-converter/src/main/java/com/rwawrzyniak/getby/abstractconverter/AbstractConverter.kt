package com.rwawrzyniak.getby.abstractconverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel

interface AbstractConverter<AM: AbstractModel, AE: AbstractEntity>{
	fun toModel(entity: AbstractEntity) : AM
	fun toEntity(model: AbstractModel): AE
}
