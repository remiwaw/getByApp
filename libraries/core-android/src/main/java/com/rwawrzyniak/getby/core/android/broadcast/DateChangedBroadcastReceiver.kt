package com.rwawrzyniak.getby.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class DateChangedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("Date or timezone was changed")
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw UnsupportedOperationException("Not yet implemented")
    }
}

class DateChangedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
		Timber.i("Date or timezone was changed")
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw UnsupportedOperationException("Not yet implemented")
    }
}
