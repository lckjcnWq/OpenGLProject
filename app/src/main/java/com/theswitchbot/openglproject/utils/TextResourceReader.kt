package com.theswitchbot.openglproject.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object TextResourceReader {
    /**
     * Reads in text from a resource file and returns a String containing the
     * text.
     */
    fun readTextFileFromResource(
        context: Context,
        resourceId: Int
    ): String {
        val body = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String?
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return body.toString()
    }
}