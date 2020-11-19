package com.yxd.lvjie.helper

import com.yp.baselib.base.BaseApplication
import com.yp.baselib.utils.SPUtils

object SPHelper {

    fun getToken(): String {
        return SPUtils.getString(BaseApplication.getInstance().context, "token", "")
    }

    fun putToken(token: String) {
        SPUtils.put(BaseApplication.getInstance().context, "token", token)
    }
}