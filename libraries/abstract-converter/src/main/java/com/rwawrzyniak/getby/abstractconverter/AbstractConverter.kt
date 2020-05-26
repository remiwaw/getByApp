package com.rwawrzyniak.getby.abstractconverter

import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel

interface AbstractConverter<AM: AbstractModel, AE: AbstractEntity>: AbstractConverterToEntity<AM, AE>, AbstractConverterToModel<AM, AE>
