/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.services

import android.app.Application
import android.os.Build
import android.os.Environment
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.CrashHandler
import io.multimoon.colorful.*
import java.io.File


class PxEZApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        val sharedPreferencesServices = SharedPreferencesServices(this)
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        animationEnable = defaultSharedPreferences.getBoolean("animation", true)
        language = defaultSharedPreferences.getString("language", "0")?.toInt() ?: 0
        storepath = defaultSharedPreferences.getString("storepath1", Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz")
                ?: Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz"
        if (defaultSharedPreferences.getBoolean("crashreport", true)) {
            CrashHandler.getInstance().init(this)
        }
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0).language;
        } else {
            resources.configuration.locale.language;
        }
        val list = ArrayList<ThemeColorInterface>().also {
            val myCustomColor1 = CustomThemeColor(
                    this,
                    R.style.bili_primary_color,
                    R.style.bili_primary_dark_color,
                    R.color.pink, // <= use the color you defined in my_custom_primary_color
                    R.color.pink // <= use the color you defined in my_custom_primary_dark_color
            )
            val myCustomColor2 = CustomThemeColor(
                    this,
                    R.style.blue_primary_color,
                    R.style.blue_primary_dark_color,
                    R.color.blue, // <= use the color you defined in my_custom_primary_color
                    R.color.md_blue_400 // <= use the color you defined in my_custom_primary_dark_color
            )
            it += ThemeColor.BLUE
            it += ThemeColor.AMBER
            it += ThemeColor.GREEN
            it += ThemeColor.PINK
            it += ThemeColor.PURPLE
            it += ThemeColor.BLUE_GREY
            it += ThemeColor.ORANGE
            it += ThemeColor.RED
            it += ThemeColor.TEAL
            it += ThemeColor.LIGHT_BLUE
            it += ThemeColor.LIGHT_GREEN
            it += myCustomColor1
            it += myCustomColor2
        }
        var colorNum = 0
        if (sharedPreferencesServices.getInt("colornum").apply { colorNum = this } > (list.size - 1)) {
            sharedPreferencesServices.setInt("colornum", 0)
        }

        val defaults = Defaults(
                primaryColor = list[colorNum],
                accentColor = ThemeColor.PINK,
                useDarkTheme = false,
                translucent = true)
        initColorful(this, defaults)

        WorkManager.getInstance(this).pruneWork()
    }


    companion object {
        @JvmStatic
        var storepath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "PxEz"
        @JvmStatic
        var locale = "zh"
        @JvmStatic
        var language: Int = 0
        @JvmStatic
        var animationEnable: Boolean = false
        lateinit var instance: PxEZApp
        var autochecked = false

    }


}
