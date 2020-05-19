package com.rwawrzyniak.getby.abstractconverter

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel

interface AbstractConverterToModel<out AM: AbstractModel, in AE: AbstractEntity>{
	fun toModel(entity: AE) : AM
}
