package com.xthan.xquicksettings

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService

class VolumeTileService : TileService() {
    override fun onClick() {
        val intent = Intent(this, VolumeHelperActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34+
            val pendingIntent = PendingIntent.getActivity(
                this, 100, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            startActivityAndCollapse(intent)
        }
    }
}