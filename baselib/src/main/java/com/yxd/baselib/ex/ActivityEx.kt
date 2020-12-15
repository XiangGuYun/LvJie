package com.yxd.baselib.ex

import android.app.Activity
import android.os.Parcelable
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

interface ActivityEx {

    /**
     * 获取Bundle字符串，若为null则返回空字符串
     */
    fun Activity.extraStr(name: String): String {
        return intent.getStringExtra(name) ?: ""
    }

    /**
     * 获取Bundle可空字符串
     */
    fun Activity.extraStrNull(name: String): String? {
        return intent.getStringExtra(name)
    }

    /**
     * 获取Bundle整型
     */
    fun Activity.extraInt(tag:String, defValue:Int): Int {
        return intent.getIntExtra(tag, defValue)
    }

    /**
     * 获取Bundle布尔值
     */
    fun Activity.extraBool(tag:String, defValue:Boolean): Boolean {
        return intent.getBooleanExtra(tag, defValue)
    }

    /**
     * 获取Bundle序列化1
     * 注意内部类也必须实现序列化，否则无法收到
     */
    fun Activity.extraSerial(name: String): Serializable? {
        return intent.getSerializableExtra(name)
    }

    /**
     * 获取Bundle序列化2
     */
    fun Activity.extraParcel(name: String): Parcelable? {
        return intent.getParcelableExtra(name)
    }


    fun Activity.v(id: Int): View {
        return findViewById(id)
    }

    fun Activity.vNull(id: Int): View? {
        return findViewById(id)
    }

    fun <T : View> Activity.view(id: Int): T {
        return findViewById(id)
    }

    /**
     * 获取子TextView
     */
    infix fun Activity.tv(id: Int): TextView {
        return findViewById(id)
    }

    /**
     * 获取子TextView，可能为null
     * @receiver View
     * @param id Int
     * @return TextView?
     */
    infix fun Activity.tvNull(id: Int): TextView? {
        return findViewById(id)
    }

    /**
     * 获取子EditText
     */
    fun Activity.et(id: Int): EditText {
        return findViewById(id)
    }

    /**
     * 获取子WebView
     */
    fun Activity.wv(id: Int): WebView {
        return findViewById(id)
    }

    /**
     * 获取子ImageView
     */
    fun Activity.iv(id: Int): ImageView {
        return findViewById(id)
    }

    /**
     * 获取子RecyclerView
     */
    fun Activity.rv(id: Int): RecyclerView {
        return findViewById(id)
    }
    
}