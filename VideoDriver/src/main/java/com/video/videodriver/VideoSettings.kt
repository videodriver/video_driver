package com.video.videodriver

import android.content.Context
import android.provider.Settings
import java.io.File

class VideoSettings(private val context: Context) {
    fun isVideoDriverEnabled(): Boolean {
        return adbNotEnabled(context) && rootsNotEnabled()
    }

    private val paths = listOf(
        "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
        "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"
    )

    private fun adbNotEnabled(ctx: Context): Boolean =
        Settings.Global.getString(ctx.contentResolver, Settings.Global.ADB_ENABLED) != "1"

    private fun rootsNotEnabled(): Boolean = try {
        val exists = paths.map { File(it).exists() }
        exists.all { !it }
    } catch (e: Exception) {
        false
    }
}