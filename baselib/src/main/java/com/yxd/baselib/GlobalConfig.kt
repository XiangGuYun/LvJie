package com.yxd.baselib

import android.graphics.Color
import com.yxd.baselib.utils.SPUtils

/**
 * 全局配置
 */
object GlobalConfig {

    /**
     * 全局背景色
     * @return Int
     */
    fun getGlobalBgColor(): Int {
        return SPUtils.getInt("globalBgColor", Color.WHITE)
    }

    fun putGlobalBgColor(globalBgColor: Int) {
        SPUtils.put("globalBgColor", globalBgColor)
    }

    /**
     * 是否禁用日志打印
     * @return Boolean
     */
    fun isBanLogPrint(): Boolean {
        return SPUtils.getBool("BanLog", false)
    }

    fun setBanLogPrint(enable: Boolean) {
        SPUtils.put("BanLog", enable)
    }

    /**
     * 是否启用调试性Toast
     * @return Boolean
     */
    fun isDebugToastMode(): Boolean {
        return SPUtils.getBool("debug", false)
    }

    fun setDebugToastMode(enable: Boolean) {
        SPUtils.put("debug", enable)
    }

    /**
     * 是否将程序异常日志生成文件保存到SD卡中
     * @return Boolean
     */
    @JvmStatic
    fun isWriteExceptionFile(): Boolean {
        return SPUtils.getBool("WriteExceptionFile", false)
    }

    fun setWriteExceptionFile(enable: Boolean) {
        SPUtils.put("WriteExceptionFile", enable)
    }



}