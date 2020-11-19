package com.yp.baselib.utils.view

import android.graphics.drawable.GradientDrawable

interface DrawableEx {

    fun GradientDrawable.setLineGradientColor(
        colors: IntArray,
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
    ): GradientDrawable {
        this.colors = colors
        this.gradientType = GradientDrawable.LINEAR_GRADIENT
        this.orientation = orientation
        return this
    }

}