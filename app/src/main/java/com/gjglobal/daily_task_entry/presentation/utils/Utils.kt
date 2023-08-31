package com.gjglobal.daily_task_entry.presentation.utils

import android.annotation.SuppressLint
import android.content.*
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
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