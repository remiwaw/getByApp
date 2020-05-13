package com.rwawrzyniak.getby.converters.di

object ConvertersInjectHelper {
	fun provideComponent(implementingClass: Any): ConvertersComponent {
		return if (implementingClass is ConvertersComponent) {
			implementingClass.getComponent()
		} else {
			throw IllegalStateException("The application context you have passed does not implement ComponentProvider")
		}
	}
}
