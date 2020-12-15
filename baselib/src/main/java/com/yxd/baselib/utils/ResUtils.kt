package com.kotlinlib.common

import android.content.Context
import com.yxd.baselib.base.BaseApplication

/**
 * 反射获取res资源工具类
 */
object ResUtils {

    /**
     * 获取资源文件的id
     *
     * @param resName
     * @return
     */
    fun getId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "id", context.packageName)
    }

    /**
     * 获取资源文件中string的id
     *
     * @param resName
     * @return
     */
    fun getStringId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "string", context.packageName)
    }


    /**
     * 获取资源文件drawable的id
     *
     * @param resName
     * @return
     */
    fun getDrawableId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }

    /**
     * 获取资源文件mipmap的id
     */
    fun getMipmapId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "mipmap", context.packageName)
    }


    /**
     * 获取资源文件layout的id
     *
     * @param resName
     * @return
     */
    fun getLayoutId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "layout", context.packageName)
    }


    /**
     * 获取资源文件style的id
     *
     * @param resName
     * @return
     */
    fun getStyleId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "style", context.packageName)
    }

    /**
     * 获取资源文件color的id
     *
     * @param resName
     * @return
     */
    fun getColorId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "color", context.packageName)
    }

    /**
     * 获取资源文件dimen的id
     *
     * @param resName
     * @return
     */
    fun getDimenId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "dimen", context.packageName)
    }

    /**
     * 获取资源文件anim的id
     *
     * @param context
     * @param resName
     * @return
     */
    fun getAnimId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "anim", context.packageName)
    }

    /**
     * 获取资源文件menu的id
     */
    fun getMenuId(resName: String): Int {
        val context = BaseApplication.getInstance().context
        return context.resources.getIdentifier(resName, "menu", context.packageName)
    }


}
