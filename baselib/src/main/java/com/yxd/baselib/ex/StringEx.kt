@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.yxd.baselib.ex

import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.base.BaseApplication
import com.yxd.baselib.utils.LogUtils
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * String扩展类
 */
interface StringEx {

    /**
     * 方便弹出Toast
     */
    fun Any.toast(isLong: Boolean = false) {
        Toast.makeText(
            BaseApplication.getInstance().context, "$this",
            if (!isLong) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).apply {
            setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }

    /**
     * 加密手机号码
     */
    fun String.encryptPhone(): String {
        return if (length == 11)
            this.replace(this.subSequence(3, 7).toString(), "****")
        else
            this
    }

    /**
     * 加密身份证号码
     */
    fun String.encryptIDNumber(): String {
        return if (length == 18 || length == 15)
            this.replace(this.subSequence(6, 14).toString(), "********")
        else
            this
    }

    /**
     * 判断字符串是否为空
     */
    fun String?.isEmpty(): Boolean {
        return TextUtils.isEmpty(this)
    }

    fun String?.e(): Boolean {
        return TextUtils.isEmpty(this)
    }


    /**
     * 打印日志
     */
    fun Any.logD(tag: Any = "YXD_", pre: String = "") {
        LogUtils.d(tag.toString(), pre + this.toString())
    }

    /**
     * 打印日志
     */
    fun Any.logI(tag: Any = "YXD_", pre: String = "") {
        LogUtils.i(tag.toString(), pre + this.toString())
    }

    /**
     * 打印日志
     */
    fun Any.logE(tag: Any = "YXD_", pre: String = "") {
        LogUtils.e(tag.toString(), pre + this.toString())
    }

    /**
     * 获取颜色
     */
    fun String.color(): Int {
        return Color.parseColor(this)
    }

    /**
     * 获取颜色
     */
    val String.color get() = Color.parseColor(this)

    /**
     * 将时间长整型转换为特定格式的时间
     * @receiver Long
     * @param fmt String 如yyyy-MM-dd
     * @return String 如2008-10-01
     */
    fun Long.fmtDate(fmt: String = "yyyy-MM-dd HH:mm:ss"): String {
        return SimpleDateFormat(fmt, Locale.CHINA).format(Date(this))
    }

    /**
     * 将特定格式的时间转换为时间长整型
     */
    fun String.reverseFmtDate(fmt: String = "yyyy-MM-dd HH:mm:ss"): Long {
        return SimpleDateFormat(fmt, Locale.CHINA).parse(this).time
    }

    /**
     * 将时间字符串转换为特定格式的时间
     * @receiver Long
     * @param fmt String 如yyyy-MM-dd
     * @return String 如2008-10-01
     */
    fun String.fmtDate(fmt: String = "yyyy-MM-dd HH:mm:ss"): String {
        return SimpleDateFormat(fmt, Locale.CHINA).format(Date(this.toLong()))
    }

    /**
     * 避免空字符串带来的困扰
     * @receiver String?
     * @return String
     */
    fun String?.safe(): String {
        return if (!this.isNullOrEmpty())
            this.toString()
        else
            ""
    }

    /**
     * 判断是否只有数字
     */
    fun String.isDigitsOnly(): Boolean {
        return TextUtils.isDigitsOnly(this)
    }

    /**
     * 转为UTF-8
     */
    fun String.utf8(): String {
        try {
            return URLEncoder.encode(this, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("UnsupportedEncodingException occurred. ", e)
        }
    }

    /**
     * 将某段文本进行另类颜色处理
     * @receiver String
     * @param color String
     * @return String
     */
    fun String.htmlColor(color: String): String {
        return "<font color='$color'>$this</font>"
    }

    /**
     * 将对象集合的某一字符串字段进行拼接
     * @param list ArrayList<T> 对象集合
     * @param regex String 分隔符
     * @param func (Int)->String 根据集合索引来获取字符串
     * @return String
     */
    fun <T, R:List<T>> appendStr(list: R, regex: String, func: (Int) -> String): String {
        val build = StringBuilder()
        for (i in list.indices) {
            if (i != list.size - 1) {
                build.append(func.invoke(i)).append(regex)
            } else {
                build.append(func.invoke(i))
            }
        }
        return build.toString()
    }


    /**
     * 获取随机颜色
     */
    fun getRandomColor(): Int {
        var r = Integer.toHexString(Random().nextInt(256)).toUpperCase(Locale.ROOT)
        var g = Integer.toHexString(Random().nextInt(256)).toUpperCase(Locale.ROOT)
        var b = Integer.toHexString(Random().nextInt(256)).toUpperCase(Locale.ROOT)

        r = if (r.length == 1) "0$r" else r
        g = if (g.length == 1) "0$g" else g
        b = if (b.length == 1) "0$b" else b

        return Color.parseColor("#${r + g + b}")
    }


    fun String?.delZero(): String {
        return this?.replace(".00", "") ?: ""
    }

    /**
     * 给金额添加逗号断点
     * @param value
     * @return
     */
    fun String.addBreaksForMoney(): String {
        return if (this.contains(".")) {
            valueFormatWithTwo(this)
        } else {
            valueFormat(this)
        }
    }

    private fun valueFormatWithTwo(value: String): String {
        if (TextUtils.isEmpty(value)) {
            return "0.00"
        }
        val bd = BigDecimal(value)
        val df = DecimalFormat("##,###,##0.00")//小数点点不够两位补0，例如："0" --> 0.00（个位数补成0因为传入的是0则会显示成：.00，所以各位也补0；）
        return df.format(bd.setScale(2, BigDecimal.ROUND_DOWN))
    }

    private fun valueFormat(value: String): String {
        if (TextUtils.isEmpty(value)) {
            return "0.00"
        }
        val bd = BigDecimal(value)
        val df = DecimalFormat("##,###,##0")//小数点点不够两位补0，例如："0" --> 0.00（个位数补成0因为传入的是0则会显示成：.00，所以各位也补0；）
        return df.format(bd.setScale(2, BigDecimal.ROUND_DOWN))
    }

    /**
     * 拼接完整的本地HTML路径
     */
    fun String.assetsUrl(): String {
        return "file:////android_asset/$this.html"
    }

    fun String.addZero(): String {
        return if(this.length == 1) "0$this" else this
    }

}