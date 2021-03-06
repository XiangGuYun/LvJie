package com.yp.baselib.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * 监听网络状态
 * @property mINetStatusListener INetStatusListener?
 */
class NetStatusReceiver : BroadcastReceiver() {


    private var mINetStatusListener: INetStatusListener? = null
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mobileNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val allNetInfo = cm.activeNetworkInfo
        if (allNetInfo == null) {
            netStatus = if (mobileNetInfo != null && (mobileNetInfo.isConnected || mobileNetInfo.isConnectedOrConnecting)) {
                NETSTATUS_MOBILE
            } else if (wifiNetInfo != null && wifiNetInfo.isConnected || wifiNetInfo!!.isConnectedOrConnecting) {
                NETSTATUS_WIFI
            } else {
                NETSTATUS_INAVAILABLE
            }
        } else {
            netStatus = if (allNetInfo.isConnected || allNetInfo.isConnectedOrConnecting) {
                if (mobileNetInfo!!.isConnected || mobileNetInfo.isConnectedOrConnecting) {
                    NETSTATUS_MOBILE
                } else {
                    NETSTATUS_WIFI
                }
            } else {
                NETSTATUS_INAVAILABLE
            }
        }
        if (mINetStatusListener != null) {
            mINetStatusListener!!.getNetState(netStatus)
        }
    }

    fun setNetStateListener(listener: INetStatusListener?) {
        mINetStatusListener = listener
    }

      fun interface INetStatusListener {
        fun getNetState(state: Int)
    }


    companion object {
        /**
         * 无网络
         */
        const val NETSTATUS_INAVAILABLE = 0

        /**
         * WIFI网络环境
         */
        const val NETSTATUS_WIFI = 1

        /**
         * 数据流量网络环境
         */
        const val NETSTATUS_MOBILE = 2

        /**
         * 当前网络状态
         */
        var netStatus = 0


    }
}