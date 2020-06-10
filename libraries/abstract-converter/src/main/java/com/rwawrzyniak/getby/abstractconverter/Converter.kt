package com.rwawrzyniak.getby.abstractconverter

import kotlin.reflect.KFunction1

abstract class Converter<Model, Entity>(
	private val fromModel: KFunction1<Model, Entity>,
	private val fromEntity: KFunction1<Entity, Model>
) {
	fun toEntity(model: Model): Entity {
		return fromModel(model)
	}

	fun toModel(entity: Entity): Model {
		return fromEntity(entity)
	}
}
