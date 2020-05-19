package com.rwawrzyniak.getby.abstractconverter

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel

interface AbstractConverterToEntity<in AM: AbstractModel, out AE: AbstractEntity>{
	fun toEntity(model: AbstractModel): AE
}
