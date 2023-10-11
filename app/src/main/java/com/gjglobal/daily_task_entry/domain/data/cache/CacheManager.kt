package com.gjglobal.daily_task_entry.domain.data.cache

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.gjglobal.daily_task_entry.core.Constants.KEY_AUTH
import com.gjglobal.daily_task_entry.core.Constants.LOGIN_UNAME_PSWD
import com.gjglobal.daily_task_entry.domain.domain.model.login.Authorization
import com.gjglobal.daily_task_entry.domain.domain.model.login.LoginRequest
import com.google.gson.Gson
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