package com.quick.freshfoodgo.util

import android.content.Context
import android.widget.Toast
import com.quick.freshfoodgo.AppContext

object CommonUtil {

    fun showToast(context: Context,text: String,isShort: Boolean = true){
        val context1 = context.applicationContext
        Toast.makeText(context1,text,if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG ).show()
    }
}