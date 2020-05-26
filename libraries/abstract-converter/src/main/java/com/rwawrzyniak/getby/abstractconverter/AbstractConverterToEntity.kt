package com.rwawrzyniak.getby.abstractconverter

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel

interface AbstractConverterToEntity<AM: AbstractModel, AE: AbstractEntity>{
	fun toEntity(model: AM): AE
}
