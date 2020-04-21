package com.rwawrzyniak.getby.core.ext.date

import java.time.ZoneId
import java.util.Date

fun Date.toLocalDate() = java.time.LocalDate.from(this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
