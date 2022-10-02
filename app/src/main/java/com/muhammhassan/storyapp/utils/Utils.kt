package com.muhammhassan.storyapp.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import okio.IOException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val timeStamp = SimpleDateFormat(
        "dd-MMM-yyyy",
        Locale.US
    ).format(System.currentTimeMillis())

    fun isEmailValid(text: String?): Boolean =
        (text != null && text.contains('@') && ((text.contains(".com") || (text.contains(".net") || (text.contains(
            ".co.id"
        ))))))

    fun isPasswordValid(text: String?): Boolean = text != null && text.length >= 6

    fun <T> parsingError(response: Response<T>, messageKey: String = "message"): String {
        return try {
            response.errorBody()?.string().runCatching {
                this?.let {
                    JSONObject(it).getString(messageKey)
                }
            }.getOrNull() ?: response.message()
        } catch (e: IOException) {
            e.message.toString()
        }
    }

    fun uriToFile(image: Uri, context :Context): File{
        val contentResolver = context.contentResolver
        val file = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(image) as InputStream
        val outputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while(inputStream.read(buf).also {len = it} >0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return file
    }

    fun createCustomTempFile(context: Context): File{
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }
}