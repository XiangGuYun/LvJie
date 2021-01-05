package com.yxd.lvjie.helper

import com.yxd.baselib.utils.SPUtils

object SPHelper {

    fun getToken(): String {
        return SPUtils.getString("token", "")
    }

    fun putToken(token: String) {
        SPUtils.put("token", token)
    }

    fun putAccount(account: String) {
        SPUtils.put("account", account)
    }

    fun getAccount(): String {
        return SPUtils.getString("account", "")
    }

    fun putPassword(password: String) {
        SPUtils.put("password", password)
    }

    fun getPassword(): String {
        return SPUtils.getString("password", "")
    }

    fun putEquipNo(equipNo: String) {
        SPUtils.put("equipNo", equipNo)
    }

    fun getEquipNo(): String {
        return SPUtils.getString("equipNo", "")
    }

    fun putEquipName(equipNo: String) {
        SPUtils.put("equipName", equipNo)
    }

    fun getEquipName(): String {
        return SPUtils.getString("equipName", "")
    }


}