package com.yxd.baselib.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.yxd.baselib.R

object SnackBarUtils {

    fun show(
        activity: Activity,
        str: String,
        action: Pair<String, ((View) -> Unit)?>? = null,
        duration: Int = BaseTransientBottomBar.LENGTH_SHORT,
        colorActionText: Int? = null,
        backgroundTint: Int? = null
    ) {
        val sb = Snackbar
            .make(
                activity.findViewById<ViewGroup>(android.R.id.content),
                "", duration
            )
        if (action != null) {
            sb.setAction(action.first) {
                action.second?.invoke(it)
            }
        }
        if (colorActionText != null) {
            sb.setActionTextColor(colorActionText)
        }
        if (backgroundTint != null) {
            sb.setBackgroundTint(backgroundTint)
        }
        sb.view.findViewById<TextView>(R.id.snackbar_text).apply {
            maxLines = 100
            text = str
        }
        sb.show()
    }

}