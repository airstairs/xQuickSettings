package com.xthan.xquicksettings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast

class RotationTileService : TileService() {

    companion object {
        const val ROTATION_FREE = -1
        const val ROTATION_UP = Surface.ROTATION_0
        const val ROTATION_DOWN = Surface.ROTATION_180
        const val ROTATION_LEFT = Surface.ROTATION_90
        const val ROTATION_RIGHT = Surface.ROTATION_270
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        if (checkWriteSettingsPermission()) {
            showRotationDialog()
        }
    }

    private fun setForcedRotation(context: Context, rotationValue: Int) {
        try {
            if (rotationValue == ROTATION_FREE) {
                Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 1)
            } else {
                Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0)
                Settings.System.putInt(context.contentResolver, Settings.System.USER_ROTATION, rotationValue)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to apply system rotation settings.", Toast.LENGTH_SHORT).show()
        }
        updateTileState()
    }

    private fun showRotationDialog() {
        val options = arrayOf(
            getString(R.string.rotation_up),
            getString(R.string.rotation_down),
            getString(R.string.rotation_left),
            getString(R.string.rotation_right),
            getString(R.string.rotation_free)
        )

        val builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
        builder.setTitle(getString(R.string.tile_rotation_label))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> setForcedRotation(this, ROTATION_UP)
                1 -> setForcedRotation(this, ROTATION_DOWN)
                2 -> setForcedRotation(this, ROTATION_LEFT)
                3 -> setForcedRotation(this, ROTATION_RIGHT)
                4 -> setForcedRotation(this, ROTATION_FREE)
            }
        }
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.window?.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG)
        showDialog(dialog)
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val context = this
        val isAutoRotate = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1

        if (isAutoRotate) {
            tile.state = Tile.STATE_INACTIVE
            tile.label = getString(R.string.tile_rotation_label)
            tile.subtitle = getString(R.string.tile_rotation_subtitle_auto)
            tile.icon = Icon.createWithResource(context, R.drawable.ic_rotation_free)
        } else {
            val currentForcedRotation = Settings.System.getInt(contentResolver, Settings.System.USER_ROTATION, ROTATION_UP)
            tile.state = Tile.STATE_ACTIVE
            tile.label = getString(R.string.tile_rotation_label)

            val forcedLabel = when (currentForcedRotation) {
                ROTATION_DOWN -> getString(R.string.rotation_down)
                ROTATION_LEFT -> getString(R.string.rotation_left)
                ROTATION_RIGHT -> getString(R.string.rotation_right)
                else -> getString(R.string.rotation_up)
            }

            tile.subtitle = getString(R.string.tile_rotation_subtitle_forced, forcedLabel)
            tile.icon = Icon.createWithResource(context, R.drawable.ic_rotation_locked)
        }
        tile.updateTile()
    }

    private fun checkWriteSettingsPermission(): Boolean {
        if (!Settings.System.canWrite(this)) {
            Toast.makeText(this, "System Settings permission is required.", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivityAndCollapse(intent)
            return false
        }
        return true
    }
}