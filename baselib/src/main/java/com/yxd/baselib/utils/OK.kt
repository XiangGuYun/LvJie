package com.yxd.baselib.utils

import com.google.gson.Gson
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.http.OkHttpUtils
import com.yxd.baselib.utils.http.callback.FileCallBack
import com.yxd.baselib.utils.http.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

/**
 * OKHTTP工具类
 */
object OK {

    /**
     * 表示所传参数被忽略
     */
    const val OPTIONAL = "optional"

    const val TAG = "YXD_OK"

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

        BaseActivity.getStackTopActivity().showLoading()

        val mapJson = Gson().toJson(HashMap(pairs.toMap()).filterValues { it != OPTIONAL })
        val builder = OkHttpUtils
            .postString()
            .url(url)
            .content(mapJson)
            .mediaType(MEDIA_TYPE.toMediaTypeOrNull())

        val mapJsonTest = Gson().toJson(HashMap(pairs.toMap()).filterKeys { it != "faceUrl" })

        builder.build()
            .connTimeOut(6000)
            .readTimeOut(6000)
            .writeTimeOut(6000)
            .execute(object : StringCallback() {

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    BaseActivity.getStackTopActivity().hideLoading()
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
                    BaseActivity.getStackTopActivity().hideLoading()
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
                    LogUtils.printLongLog(TAG, " URL：$url\nJSON：$mapJsonTest\nRESPONSE：$response")
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
        crossinline onSuccess: (data: T) -> Unit, //成功回调
    ) {

        val jsonString = Gson().toJson(json)

        val builder = OkHttpUtils
            .postString()
            .url(url)
            .content(jsonString)
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
                    LogUtils.printLongLog(TAG, " URL：$url\nJSON：$jsonString\nRESPONSE：$response")
                    onSuccess.invoke(Gson().fromJson(response, T::class.java))
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
        vararg pairs: Pair<String, String>
    ) {


        val builder = OkHttpUtils
            .post()
            .url(url)

//        builder.addHeader("", "")

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
                        " URL：$url\nPARAMS：$pairs\nMessage：${e?.localizedMessage}"
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
                    LogUtils.d(TAG, " URL：$url\nPARAMS：$pairs\nRESPONSE：$response")
                    onSuccess.invoke(Gson().fromJson(response, T::class.java))
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
        vararg params: Pair<String, Any>
    ) {
        val req = OkHttpUtils
            .get()
            .url(url)

        params.forEach {
            if (it.second != OK.OPTIONAL)
                req.addParams(it.first, it.second.toString())
        }

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
                    onSuccess.invoke(Gson().fromJson(response, T::class.java))
                }
            })
    }

    /**
     * 下载文件
     * @param fileUrl String
     * @param destFileDir String
     * @param fileName String
     * @param getFile Function1<File, Unit>
     * @param getProgress Function1<Float, Unit>?
     */
    fun downloadFile(
        fileUrl: String,
        destFileDir: String,
        fileName: String,
        getFile: (File) -> Unit,
        getProgress: ((Float) -> Unit)? = null) {
        OkHttpUtils.get().url(fileUrl).build()
            .execute(object : FileCallBack(destFileDir, fileName) {
                override fun onResponse(response: File?, id: Int) {
                    response?.let {
                        getFile.invoke(it)
                    }
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    ToastUtils.toast("下载失败：${e?.localizedMessage}")
                }

                override fun inProgress(progress: Float, total: Long, id: Int) {
                    getProgress?.invoke(progress)
                }
            })
    }

}