package com.quick.freshfoodgo.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.quick.freshfoodgo.AppContext

object KeyBoardUtil {
    fun hideKeyboard(context: Context,view: View) {
        val imm = getSystemService(context,InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}