package com.rwawrzyniak.getby.core.android.broadcast

sealed class GlobalEvent {
    object DateChanged : GlobalEvent()
}
