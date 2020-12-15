package com.yxd.baselib.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import com.yxd.baselib.base.BaseApplication

/**
 * 网络状态工具类
 */
object NetUtils {

    /**
     * 网络类型
     */
    enum class NetType {
        WIFI, CMNET, CMWAP, NONE
    }

    /**
     * 判断是否有网络连接
     * 需要添加ACCESS_NETWORK_STATE权限
     */
    fun isConnected(): Boolean {
        if(!PermissionUtils.checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)){
            throw Exception("未添加ACCESS_NETWORK_STATE权限！")
        }
        val ctx = BaseApplication.getInstance().context
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = cm.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
        return false
    }

    /**
     * 获取当前的网络状态
     * 需要添加ACCESS_NETWORK_STATE权限
     */
    fun getNetType(): NetType {
        if(!PermissionUtils.checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)){
            throw Exception("未添加ACCESS_NETWORK_STATE权限！")
        }
        val ctx = BaseApplication.getInstance().context
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo ?: return NetType.NONE
        val nType = networkInfo.type

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return if (networkInfo.extraInfo.toLowerCase() == "cmnet") {
                NetType.CMNET
            } else {
                NetType.CMWAP
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI
        }
        return NetType.NONE

    }


}