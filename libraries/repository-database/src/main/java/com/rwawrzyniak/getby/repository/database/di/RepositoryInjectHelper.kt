package com.rwawrzyniak.getby.repository.database.di

import com.rwawrzyniak.getby.converters.di.RepositoryComponentProvider

object RepositoryInjectHelper {
	fun provideComponent(implementingClass: Any): RepositoryComponent {
		return if (implementingClass is RepositoryComponentProvider) {
			implementingClass.provideComponent()
		} else {
			throw IllegalStateException("The application context you have passed does not implement ComponentProvider")
		}
	}
}
