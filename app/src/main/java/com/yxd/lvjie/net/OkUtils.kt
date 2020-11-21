package com.yxd.lvjie.net

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.yp.baselib.utils.LogUtils
import com.yp.baselib.utils.ToastUtils
import com.yp.baselib.utils.http.OkHttpUtils
import com.yp.baselib.utils.http.callback.StringCallback
import com.yxd.lvjie.helper.SPHelper
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

/**
 * OKHTTP工具类
 */
object OkUtils {

    /**
     * 表示所传参数被忽略
     */
    const val OPTIONAL = "optional"

    const val TAG = "OK_Result"

    const val MEDIA_TYPE = "application/json; charset=utf-8"

    /**
     * 是否保存日志到SD卡中
     */
    const val SAVE_LOG = false

    /**
     * Post请求（JSON字符串形式）
     */
    inline fun <reified T> post(
        url: String, //URL
        crossinline onSuccess: (data: T) -> Unit, //成功回调
        vararg pairs: Pair<String, String>//参数
    ) {
        val mapJson = Gson().toJson(HashMap(pairs.toMap()).filterValues { it != OPTIONAL })
        val builder = OkHttpUtils
            .postString()
            .url(url)
            .content(mapJson)
            .mediaType(MEDIA_TYPE.toMediaTypeOrNull())

        builder.build()
            .connTimeOut(6000)
            .readTimeOut(6000)
            .writeTimeOut(6000)
            .execute(object : StringCallback() {

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "                                                               请求失败（POST）                                                       "
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        " URL：$url\nJSON：$mapJson\nMessage：${e?.localizedMessage}"
                    );
                    call?.cancel()
                }

                override fun onResponse(response: String?, id: Int) {
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "                                                               请求成功（POST）                                                       "
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(TAG, " URL：$url\nJSON：$mapJson\nRESPONSE：$response")
                    onSuccess.invoke(Gson().fromJson(response, T::class.java))
                }
            })

    }

    /**
     * Post请求（对象形式）
     * @param url String
     * @param json Any
     * @param onSuccess Function1<[@kotlin.ParameterName] T, Unit>
     */
    inline fun <reified T> post(
        url: String, //URL
        json: Any,
        crossinline onSuccess: (data: T) -> Unit//成功回调
    ) {

        val jsonString = Gson().toJson(json)

        val builder = OkHttpUtils
            .postString()
            .url(url)
            .content(jsonString)
            .mediaType(MEDIA_TYPE.toMediaTypeOrNull())

        builder.addHeader("token", SPHelper.getToken())

        builder.build()
            .connTimeOut(6000)
            .readTimeOut(6000)
            .writeTimeOut(6000)
            .execute(object : StringCallback() {

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "                                                               请求失败（POST）                                                       "
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        " URL：$url\nJSON：$jsonString\nMessage：${e?.localizedMessage}"
                    );
                    call?.cancel()
                }

                override fun onResponse(response: String?, id: Int) {
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "                                                               请求成功（POST）                                                       "
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(TAG, " URL：$url\nJSON：$jsonString\nRESPONSE：$response")
                    val jp = JsonParser()
                    val jo = jp.parse(response) as JsonObject
                    if (jo.get("code").asInt == 0) {
                        onSuccess.invoke(Gson().fromJson(response, T::class.java))
                    } else {
                        ToastUtils.toast(jo.get("message").asString)
                    }
                }
            })

    }

    /**
     * Post请求（表单形式）
     * @param url String
     * @param json Any
     * @param onSuccess Function1<[@kotlin.ParameterName] T, Unit>
     */
    inline fun <reified T> postForm(
        url: String, //URL
        crossinline onSuccess: (data: T) -> Unit, //成功回调
        vararg pairs: Pair<String, String>,
        handleResponseCode: Boolean = true
    ) {


        val builder = OkHttpUtils
            .post()
            .url(URL.BASE_URL + url)

        builder.addHeader("token", SPHelper.getToken())

        pairs.forEach {
            if (it.second != OPTIONAL) {
                builder.addParams(it.first, it.second)
            }
        }

        builder.build()
            .connTimeOut(6000)
            .readTimeOut(6000)
            .writeTimeOut(6000)
            .execute(object : StringCallback() {

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "                                                               请求失败（POST）                                                       "
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        " URL：$url\nPARAMS：${pairs.toList().toTypedArray()
                            .contentToString()}\nMessage：${e?.localizedMessage}"
                    );
                    call?.cancel()
                }

                override fun onResponse(response: String?, id: Int) {
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "                                                               请求成功（POST）                                                       "
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(
                        TAG, " URL：$url\nPARAMS：${
                        pairs.toList().toTypedArray().contentToString()
                        }\nRESPONSE：$response"
                    )
                    if (handleResponseCode) {
                        val jp = JsonParser()
                        val jo = jp.parse(response) as JsonObject
                        if (jo.get("code").asInt == 0) {
                            onSuccess.invoke(Gson().fromJson(response, T::class.java))
                        } else {
                            ToastUtils.toast(jo.get("message").asString)
                        }
                    } else {
                        onSuccess.invoke(Gson().fromJson(response, T::class.java))
                    }
                }
            })

    }

    /**
     * Get请求
     * @param url String
     * @param onSuccess Function1<[@kotlin.ParameterName] T, Unit>
     * @param params Array<out Pair<String, String>>
     */
    inline fun <reified T> get(
        url: String,
        crossinline onSuccess: (data: T) -> Unit,
        vararg params: Pair<String, String>
    ) {
        val req = OkHttpUtils
            .get()
            .url(URL.BASE_URL + url)

        params.forEach {
            req.addParams(it.first, it.second)
        }

        req.addHeader("token", SPHelper.getToken())

        req.build()
            .execute(object : StringCallback() {
                override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "                                                               请求失败（GET）                                                       "
                    )
                    LogUtils.e(
                        TAG,
                        "×                                                                                                                                  ×"
                    )
                    LogUtils.e(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.e(
                        TAG,
                        " URL：$url\nMESSAGE：${e?.localizedMessage}"
                    );
                    call?.cancel()
                }

                override fun onResponse(response: String?, id: Int) {
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "                                                               请求成功（GET）                                                       "
                    )
                    LogUtils.d(
                        TAG,
                        "√                                                                                                                                  √"
                    )
                    LogUtils.d(
                        TAG,
                        "......................................................................................................................................."
                    )
                    LogUtils.d(TAG, " URL：$url\nRESPONSE：$response")
                    val jp = JsonParser()
                    val jo = jp.parse(response) as JsonObject
                    if (jo.get("code").asInt == 0) {
                        onSuccess.invoke(Gson().fromJson(response, T::class.java))
                    } else {
                        ToastUtils.toast(jo.get("message").asString)
                    }
                }
            })
    }

}