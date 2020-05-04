package com.rwawrzyniak.getby.core.android.broadcast

sealed class MenuItemClickedEvent {
    object SaveClicked : MenuItemClickedEvent()
    object EditClicked : MenuItemClickedEvent()
}
