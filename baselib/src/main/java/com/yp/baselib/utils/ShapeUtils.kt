package com.yp.baselib.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View

object ShapeUtils {

    /**
     * 在代码中给View设置shape的背景
     */
    fun set(
            targetView: View,
            cornerRadius: Float,
            color: Int = Color.WHITE,
            shapeType: Int = GradientDrawable.RECTANGLE
    ) {
        val drawable = GradientDrawable()
        drawable.cornerRadius = cornerRadius
        drawable.setColor(color)
        drawable.shape = shapeType
        targetView.background = drawable
    }

}