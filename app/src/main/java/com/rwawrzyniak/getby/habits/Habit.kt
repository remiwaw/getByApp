package com.rwawrzyniak.getby.habits

data class Habit(
    val name: String,
    val description: String,
    val history: List<String> = emptyList()
)