package com.yxd.baselib.utils

import android.view.Gravity
import android.widget.Toast
import com.yxd.baselib.GlobalConfig
import com.yxd.baselib.base.BaseApplication

object ToastUtils {

    /**
     * 调试性Toast，受setDebugMode配置，只在调试模式中显示
     * @param str String
     */
    @JvmStatic
    fun toastDebug(str: String) {
        if (GlobalConfig.isDebugToastMode()) {
            if (Thread.currentThread().name == "main") {
                Toast.makeText(
                    BaseApplication.getInstance().context, str,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                throw Exception("未在主线程中使用Toast！")
            }

        }
    }


    @JvmStatic
    fun toast(str: Any) {
        if (Thread.currentThread().name == "main") {
            Toast.makeText(
                BaseApplication.getInstance().context, str.toString(),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            throw Exception("未在主线程中使用Toast！")
        }

    }

    @JvmStatic
    fun toast(
        str: String,
        isLong: Boolean = false,
        gravity: Int = Gravity.BOTTOM,
        xOffSet: Int = 0,
        yOffset: Int = 0
    ) {
        if (Thread.currentThread().name == "main") {
            Toast.makeText(
                BaseApplication.getInstance().context, str,
                if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            )
                .apply {
                    setGravity(gravity, xOffSet, yOffset)
                }.show()
        } else {
            throw Exception("未在主线程中使用Toast！")
        }

    }

}