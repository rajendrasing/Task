package com.rr.task.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {

    fun createCustomDirectory(directory: String,context: Context): File {
        val file = File(context.getExternalFilesDir("/")?.absolutePath, directory)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    fun isFileExists(directory: File, fileName: String): Boolean {
        val file = File(directory, fileName)
        return file.exists()
    }

    fun downloadVideo(url: String, fileName: String, directory: String,context: Context) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri)
            .setTitle(fileName)
            .setDestinationInExternalFilesDir(context, directory, fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadId = downloadManager.enqueue(request)
    }
}