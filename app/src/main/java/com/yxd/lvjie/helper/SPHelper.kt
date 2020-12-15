package com.yxd.lvjie.helper

import com.yxd.baselib.utils.SPUtils

object SPHelper {

    fun getToken(): String {
        return SPUtils.getString("token", "")
    }

    fun putToken(token: String) {
        SPUtils.put("token", token)
    }
}