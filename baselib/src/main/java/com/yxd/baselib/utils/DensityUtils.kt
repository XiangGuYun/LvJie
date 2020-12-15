package com.yxd.baselib.utils

import android.content.Context
import com.yxd.baselib.base.BaseApplication

class DensityUtils {
    companion object {
        @JvmStatic
        fun px2dip(pxValue: Number): Int {
            val context = BaseApplication.getInstance().context
            val scale = context.resources.displayMetrics.density
            return (pxValue.toFloat() / scale + 0.5f).toInt()
        }

        @JvmStatic
        fun dp2px(dipValue: Number): Int {
            val context = BaseApplication.getInstance().context
            val scale = context.resources.displayMetrics.density
            return (dipValue.toFloat() * scale + 0.5f).toInt()
        }

        @JvmStatic
        fun px2sp(pxValue: Number): Int {
            val context = BaseApplication.getInstance().context
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue.toFloat() / fontScale + 0.5f).toInt()
        }

        @JvmStatic
        fun sp2px(spValue: Number): Int {
            val context = BaseApplication.getInstance().context
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue.toFloat() * fontScale + 0.5f).toInt()
        }
    }

}