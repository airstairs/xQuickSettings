package com.xthan.xquicksettings

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class VolumeHelperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // Raises volume programmatically and triggers native system overlay HUD
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_RAISE,
            AudioManager.FLAG_SHOW_UI
        )
        finish()
    }
}