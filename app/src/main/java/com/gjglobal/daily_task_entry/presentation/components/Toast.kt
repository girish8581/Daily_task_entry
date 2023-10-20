package com.gjglobal.daily_task_entry.presentation.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable

@Composable
fun ToastMessage(context: Context,message:String){

    Toast.makeText(context,message,Toast.LENGTH_LONG).show()

}