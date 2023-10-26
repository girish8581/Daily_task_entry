package com.gjglobal.daily_task_entry.presentation.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.os.Build
import androidx.annotation.RequiresApi
import com.gjglobal.daily_task_entry.presentation.dashboard.home.home.HomeScreenViewModel
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


var showLastRcpt :Boolean =false
@SuppressLint("SimpleDateFormat")
fun currentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(Date())
}
@SuppressLint("SimpleDateFormat")
fun currentDateApi(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}
@SuppressLint("SimpleDateFormat")
fun currentDateApiReport(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    return sdf.format(Date())
}
@SuppressLint("SimpleDateFormat")
fun currentTime(): String {
    val stf = SimpleDateFormat("hh:mm:ss a", Locale.US)
    return stf.format(Date())
}


@SuppressLint("SimpleDateFormat")
fun currentTime24(): String {
    val stf = SimpleDateFormat("HH:mm:ss", Locale.UK)
    return stf.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun currentTime24HHMM(): String {
    val stf = SimpleDateFormat("HH:mm", Locale.UK)
    return stf.format(Date())
}


@SuppressLint("SimpleDateFormat")
fun currentTimeHHMM(): String {
    val stf = SimpleDateFormat("hh:mm a", Locale.US)
    return stf.format(Date())
}
@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

private fun getSpace(firstStr: String, SecondStr: String): String {
    val strLength = (SecondStr.length + firstStr.length)
    val sb = StringBuilder().apply {
        repeat(32 - strLength) {
            append(" ")
        }
    }
    return sb.toString()
}
// Function to format a LocalTime to "HH:mm" format
@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeToHHmm(time: String): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return time.format(formatter)
}

fun formatDate(date: String): String {
    val possibleFormats = arrayOf("yyyy/MM/dd", "yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    for (format in possibleFormats) {
        try {
            val inputFormat = SimpleDateFormat(format, Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            if (parsedDate != null) {
                return outputFormat.format(parsedDate)
            }
        } catch (e: ParseException) {
            // Parsing failed, try the next format
        }
    }
    // None of the formats matched, handle the error here (e.g., return an empty string)
    return ""
}

fun formatDateApi(date: String): String {
    val possibleFormats = arrayOf("dd-MM-yyyy", "dd/MM/yyyy")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (format in possibleFormats) {
        try {
            val inputFormat = SimpleDateFormat(format, Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            if (parsedDate != null) {
                return outputFormat.format(parsedDate)
            }
        } catch (e: ParseException) {
            // Parsing failed, try the next format
        }
    }
    // None of the formats matched, handle the error here (e.g., return an empty string)
    return ""
}

fun saveFile(body: ResponseBody?, activity: Activity): File? {
    if (body == null)
        return null
    var input: InputStream? = null
    try {
        input = body.byteStream()
        val file = File(activity.cacheDir, "dp.jpg")
        val fos = FileOutputStream(file)
        fos.use { output ->
            val buffer = ByteArray(4 * 1024) // or other buffer size
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
        HomeScreenViewModel.profilePic = file
        return file
    } catch (_: Exception) {

    } finally {
        input?.close()
    }
    return null
}