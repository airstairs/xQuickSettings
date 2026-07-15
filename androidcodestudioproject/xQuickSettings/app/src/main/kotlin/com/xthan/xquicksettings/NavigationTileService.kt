package com.xthan.xquicksettings

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.TileService
import android.widget.Toast

class NavigationTileService : TileService() {
    override fun onClick() {
        val intent = Intent("com.android.settings.NAVIGATION_MODE_SETTINGS").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this, 101, intent, 
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            try {
                startActivityAndCollapse(pendingIntent)
            } catch (e: Exception) {
                fallbackToMainSettings()
            }
        } else {
            try {
                startActivityAndCollapse(intent)
            } catch (e: Exception) {
                fallbackToMainSettings()
            }
        }
    }

    private fun fallbackToMainSettings() {
        val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingFallback = PendingIntent.getActivity(
                this, 102, fallbackIntent, 
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            startActivityAndCollapse(pendingFallback)
        } else {
            startActivityAndCollapse(fallbackIntent)
        }
        Toast.makeText(this, "Opening Settings. Look for 'Navigation'.", Toast.LENGTH_LONG).show()
    }
}