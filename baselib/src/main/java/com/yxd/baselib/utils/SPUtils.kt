package com.yxd.baselib.utils

import android.content.Context
import androidx.core.content.edit
import com.yxd.baselib.base.BaseApplication

/**
 * SharedPreferences工具
 */
object SPUtils {

    /**
     * 进行配置读写的默认文件名
     */
    private const val FILE_NAME = "default"

    /**
     * 获取配置文件中某个Key对应的Value
     */
    fun get(key: String, defaultObject: Any, fileName: String = FILE_NAME): Any {
        val sp = BaseApplication.getInstance().context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
        return when (defaultObject) {
            is String -> sp.getString(key, defaultObject.toString()) ?: ""
            is Int -> sp.getInt(key, defaultObject.toInt())
            is Boolean -> sp.getBoolean(key, defaultObject)
            is Float -> sp.getFloat(key, defaultObject.toFloat())
            is Long -> sp.getLong(key, defaultObject.toLong())
            else -> Any()
        }
    }

    /**
     * 将某个配置键值对写入到配置文件中
     */
    fun put(key: String, obj: Any, fileName: String = FILE_NAME) {
        BaseApplication.getInstance().context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            .edit {
                when (obj) {
                    is String -> putString(key, obj)
                    is Int -> putInt(key, obj)
                    is Boolean -> putBoolean(key, obj)
                    is Float -> putFloat(key, obj)
                    is Long -> putLong(key, obj)
                    else -> putString(key, obj.toString())
                }
            }
    }

    /**
     * 清空配置文件中的数据
     */
    fun clear(fileName: String = FILE_NAME) {
        val sp = BaseApplication.getInstance().context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * 获取配置文件中某个Key对应的Int型Value
     */
    fun getInt(key: String, defaultObject: Int = -1, fileName: String = FILE_NAME): Int {
        val sp = BaseApplication.getInstance().context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
        return sp.getInt(key, defaultObject)
    }

    /**
     * 获取配置文件中某个Key对应的String型Value
     */
    fun getString(key: String, defaultObject: String = "", fileName: String = FILE_NAME): String {
        val sp = BaseApplication.getInstance().context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
        return sp.getString(key, defaultObject).toString()
    }

    /**
     * 获取配置文件中某个Key对应的Bool型Value
     */
    fun getBool(
        key: String,
        defaultObject: Boolean = false,
        fileName: String = FILE_NAME
    ): Boolean {
        val sp = BaseApplication.getInstance().context.getSharedPreferences(
            fileName,
            Context.MODE_PRIVATE
        )
        return sp.getBoolean(key, defaultObject)
    }


}