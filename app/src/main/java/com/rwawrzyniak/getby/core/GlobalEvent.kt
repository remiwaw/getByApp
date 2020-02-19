package com.rwawrzyniak.getby.core

sealed class GlobalEvent {
    object DateChanged : GlobalEvent()
}
