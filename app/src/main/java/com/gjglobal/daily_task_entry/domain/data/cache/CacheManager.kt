package com.gjglobal.daily_task_entry.domain.data.cache

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.gjglobal.daily_task_entry.core.Constants.KEY_AUTH
import com.gjglobal.daily_task_entry.core.Constants.LAST_CHECKED_DATE
import com.gjglobal.daily_task_entry.core.Constants.LOGIN_UNAME_PSWD
import com.gjglobal.daily_task_entry.core.Constants.READ_NOTIFICATION
import com.gjglobal.daily_task_entry.core.Constants.STAFF_LIST
import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginRequest
import com.gjglobal.daily_task_entry.domain.domain.model.staff.StaffData
import com.gjglobal.daily_task_entry.presentation.dashboard.notification.ReadNotificationIndexModel
import com.gjglobal.daily_task_entry.presentation.utils.currentDateApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class CacheManager(applicationContext: Context) {
    private var sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("com.gjglobal.hms_gj", Context.MODE_PRIVATE)

//    private var sharedPreferences: SharedPreferences
//    init {
//        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//        sharedPreferences = EncryptedSharedPreferences.create(
//            "com.gjglobal.parkezy_gj",
//            masterKeyAlias,
//            applicationContext,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//    }

    //function used to save auth response
    fun saveAuthResponse(authResponse: Authorization) {
        val jsonString = Gson().toJson(authResponse)
        with(sharedPreferences.edit()) {
            putString(KEY_AUTH, jsonString)
            commit()
        }
    }

    fun getAuthResponse(): Authorization? {
        val jsonString = sharedPreferences.getString(KEY_AUTH, "")
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        return Gson().fromJson(jsonString, Authorization::class.java)
    }


    fun saveStaffData(staffList: List<StaffData>) {
        val jsonString = Gson().toJson(staffList)
        with(sharedPreferences.edit()) {
            putString(STAFF_LIST, jsonString)
            commit()
        }
    }

    fun getStaffData(): List<StaffData>? {
        val jsonString = sharedPreferences.getString(STAFF_LIST, "")
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<List<StaffData>>() {}.type
        return Gson().fromJson(jsonString, type)
    }


    fun getQAStaffDataOrg(): List<StaffData> {
        val qaStaffList: List<StaffData> = getStaffData()!!.filter { staff ->
            staff.userType == "QA"
        }
        return qaStaffList
    }

    fun getQAStaffData(): List<StaffData> {
        val staffList: List<StaffData?>? = getStaffData()
        val qaStaffList: List<StaffData> = staffList
            ?.filterNotNull()
            ?.filter { staff ->
                staff.userType == "QA"
            } ?: emptyList()

        return qaStaffList
    }

//    fun getQAStaffData(): List<StaffData> {
//        return getStaffData()
//            ?.filter { staff ->
//                staff.userType == "QA"
//            } ?: emptyList()
//    }



    fun saveUsernamePassword(unamePwd: LoginRequest) {
        Log.e("RELOGINPSWUNAME:::", unamePwd.toString())
        val jsonString = Gson().toJson(unamePwd)
        with(sharedPreferences.edit()) {
            putString(LOGIN_UNAME_PSWD, jsonString)
            commit()
        }
    }

    fun getUsernamePassword(): LoginRequest? {
        val jsonString = sharedPreferences.getString(LOGIN_UNAME_PSWD, "")
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        return Gson().fromJson(jsonString, LoginRequest::class.java)
    }

    fun appendNotificationStatus(newItem: ReadNotificationIndexModel) {
        // Retrieve the existing list from SharedPreferences
        val jsonString = sharedPreferences.getString(READ_NOTIFICATION, null)
        val type = object : TypeToken<MutableList<ReadNotificationIndexModel>>() {}.type
        val existingList = Gson().fromJson<MutableList<ReadNotificationIndexModel>>(jsonString, type) ?: mutableListOf()
        // Add the new item to the list
        existingList.add(newItem)
        // Convert the updated list back to a JSON string
        val updatedJsonString = Gson().toJson(existingList)
        // Save the updated list back to SharedPreferences
        with(sharedPreferences.edit()) {
            putString(READ_NOTIFICATION, updatedJsonString)
            commit()
        }
    }

    fun getNotificationStatus(): List<ReadNotificationIndexModel> {
        // Retrieve the JSON string from SharedPreferences
        val jsonString = sharedPreferences.getString(READ_NOTIFICATION, null)

        // Check if the JSON string is not null
        if (jsonString != null) {
            // Deserialize the JSON string into a list of ReadNotificationIndexModel objects
            val type = object : TypeToken<List<ReadNotificationIndexModel>>() {}.type
            return Gson().fromJson(jsonString, type)
        }
        // Return an empty list if the JSON string is null or couldn't be deserialized
        return emptyList()
    }

    fun clearNotificationStatusIfDateChanged() {
        val currentDate = currentDateApi() // Implement this function to get the current date

        val lastCheckedDate = sharedPreferences.getString(LAST_CHECKED_DATE, null)

        if (lastCheckedDate != currentDate) {
            clearNotificationStatus()
            // Update the last checked date in SharedPreferences
            sharedPreferences.edit().putString(LAST_CHECKED_DATE, currentDate).apply()
        }
    }

    private fun clearNotificationStatus() {
        sharedPreferences.edit().remove(READ_NOTIFICATION).apply()
    }


    @Composable
    fun ClearSharedPreferences() {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        // Function to clear shared preferences
        fun clearSharedPreferences() {
            coroutineScope.launch {
                val sharedPreferences = context.getSharedPreferences("com.gjglobal.hms_gj", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
            }
        }

        // Call the function to clear shared preferences
        clearSharedPreferences()
    }

}