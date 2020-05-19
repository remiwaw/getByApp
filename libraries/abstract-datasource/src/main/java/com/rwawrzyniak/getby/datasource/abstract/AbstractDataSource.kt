package com.rwawrzyniak.getby.datasource.abstract

import com.rwawrzyniak.getby.abstractentities.AbstractEntity

interface AbstractDataSource<T: AbstractEntity> : AbstractDataSourceRead<T>, AbstractDataSourceWrite<T>
