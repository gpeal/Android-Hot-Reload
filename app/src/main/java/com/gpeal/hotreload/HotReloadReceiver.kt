package com.gpeal.hotreload

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * This can be used to create a lambda that can be called via adb with arbitrary parameters.
 * To use, it just create an instance of this anywhere you want to run a code block like this:
 * HotReloadReceiver(context) { intent -> }
 *
 * You can pass arguments using intent extras.
 *
 * This is meant for debugging only. Production broadcast receivers should be properly registered and unregistered.
 *
 * @see https://developer.android.com/studio/command-line/adb#IntentSpec
 */
class HotReloadReceiver(
    context: Context,
    private val intentAction: String = "debug",
    private val receiver: (Intent) -> Unit
) : BroadcastReceiver() {

    init {
        context.registerReceiver(this, IntentFilter(intentAction))
    }

    override fun onReceive(context: Context, intent: Intent) {
        receiver(intent)
    }
}