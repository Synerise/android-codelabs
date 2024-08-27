package com.synerise.integration.app.main.utils

import android.content.Context
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader

fun ReadJSONFromAssets(context: Context, path: String): String {
    val identifier = "[ReadJSON]"
    try {
        val file = context.assets.open("$path")
        Timber.tag(identifier).i("%s.", "Found File: " + file)
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        Timber.tag(identifier).i("%s.", "getJSON stringBuilder: " + stringBuilder)
        val jsonString = stringBuilder.toString()
        Timber.tag(identifier).i("%s.", "JSON as String: " + jsonString)
        return jsonString
    } catch (e: Exception) {
        Timber.tag(identifier).e("%s.", "Error reading JSON: " + e)
        e.printStackTrace()
        return ""
    }
}