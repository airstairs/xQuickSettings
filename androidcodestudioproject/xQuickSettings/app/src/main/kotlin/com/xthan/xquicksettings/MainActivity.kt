package com.xthan.xquicksettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestRequiredPermissions()
    }

    private fun requestRequiredPermissions() {
        if (!Settings.System.canWrite(this)) {
            Toast.makeText(
                this, 
                "Please enable 'Modify system settings' for rotation control.", 
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }
}