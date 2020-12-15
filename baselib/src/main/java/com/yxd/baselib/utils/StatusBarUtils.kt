package com.yxd.baselib.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.githang.statusbar.StatusBarCompat
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.base.BaseApplication

object StatusBarUtils {

    /**
     * 获取状态栏高度，单位像素
     */
    fun getStatusBarHeight(): Int {
        val resources = BaseApplication.getInstance().context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 状态栏颜色
     * @param activity Activity
     * @param color Int
     */
    fun setStatusBarColor(activity: Activity, color: Int){
        StatusBarCompat.setStatusBarColor(activity, color)
    }

    /**
     * 隐藏或显示状态栏
     */
    fun hideStatusBar(activity: Activity = BaseActivity.getStackTopActivity(), isHide: Boolean = true) {
        if (!isHide) { //显示状态栏
            val lp = activity.window.attributes
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            activity.window.attributes = lp
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else { //隐藏状态栏
            val lp = activity.window.attributes
            lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            activity.window.attributes = lp
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    /**
     * 设置状态栏颜色为黑色，仅对6.0以上版本有效
     */
    fun setStatusBarTextBlack(activity: Activity, isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = activity.window.decorView
            if (isDark) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)//设置绘画模式
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//设置半透明模式
                }
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)//清除绘画模式
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//清除半透明模式
                }
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }

    /**
     * 设置背景变灰
     * @param alpha Float
     */
    fun setWindowAlpha(activity: Activity, alpha: Float = 0.4f) {
        val attr = activity.window.attributes
        attr.alpha = alpha
        activity.window.attributes = attr
    }

    /**
     * 设置底部导航栏是是否显示
     */
    fun setNavigationBar(activity: Activity, visible: Boolean) {
        if (!visible) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

}