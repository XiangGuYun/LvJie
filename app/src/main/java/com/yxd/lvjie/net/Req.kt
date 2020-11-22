package com.yxd.lvjie.net

import com.yp.baselib.utils.MD5Utils
import com.yp.baselib.utils.OK
import com.yxd.lvjie.bean.*
import com.yxd.lvjie.helper.SPHelper

object Req {

    /**
     * 登录
     * @param username String
     * @param password String
     * @param callback Function1<LoginBean, Unit>
     */
    fun login(username: String, password: String, callback: (LoginBean) -> Unit) {
        OkUtils.postForm<LoginBean>(
            URL.LOGIN,
            {
                it.data?.let {
                    SPHelper.putToken(it.token ?: "")
                }
                callback.invoke(it)
            }, "username" to username, "password" to MD5Utils.md5(password)
        )
    }

    /**
     * 密码验证
     * @param password String
     * @param callback Function1<DeviceMarkEditBean, Unit>
     */
    fun verifyPassword(password: String, callback: (DeviceMarkEditBean) -> Unit) {
        OkUtils.postForm<DeviceMarkEditBean>(
            URL.PWD_VERIFY,
            {
                callback.invoke(it)
            }, "password" to password, handleResponseCode = false
        )
    }

    /**
     * 设备编辑
     * @param callback Function1<DeviceEditBean, Unit>
     */
    fun editDevice(callback: (DeviceEditBean) -> Unit) {
        OkUtils.postForm<DeviceEditBean>(URL.DEVICE_EDIT, {
            callback.invoke(it)
        })
    }

    /**
     * 获取设备详情
     * @param id String
     * @param callback Function1<DeviceDetailBean, Unit>
     */
    fun getDeviceDetail(id: String, callback: (DeviceDetailBean) -> Unit) {
        OkUtils.get<DeviceDetailBean>(
            URL.DEVICE_DETAIL,
            {
                callback.invoke(it)
            }, "id" to id
        )
    }

    /**
     * 设备列表
     * @param installPattern String
     */
    fun getDeviceList(installPattern: String, callback: (DeviceListBean) -> Unit) {
        OkUtils.get<DeviceListBean>(URL.DEVICE_LIST, {
            callback.invoke(it)
        }, "installPattern" to installPattern)
    }

    /**
     * 历史数据查询
     * @param id String
     * @param startTime String
     * @param endTime String
     * @param callback Function1<HistoryDataBean, Unit>
     */
    fun getHistoryData(
        id: String,
        startTime: String = OK.OPTIONAL,
        endTime: String = OK.OPTIONAL,
        callback: (HistoryDataBean) -> Unit
    ) {
        OkUtils.get<HistoryDataBean>(URL.HISTORY_DATA_QUERY, {
            callback.invoke(it)
        }, "id" to id, "startTime" to startTime, "endTime" to endTime)
    }

    /**
     * 设备标定列表
     * @param equipId String
     * @param callback Function1<DeviceMarkListBean, Unit>
     */
    fun getDeviceMarkList(equipId: String, callback: (DeviceMarkListBean) -> Unit) {
        OkUtils.get<DeviceMarkListBean>(
            URL.DEVICE_MARK_LIST,
            {
                callback.invoke(it)
            }, "equipId" to equipId
        )
    }

    /**
     * 设备标定编辑
     * @param json DeviceMarkEditJson
     * @param callback Function1<DeviceMarkEditBean, Unit>
     */
    fun editDeviceMark(json: DeviceMarkEditJson, callback: (DeviceMarkEditBean) -> Unit) {
        OkUtils.post<DeviceMarkEditBean>(
            URL.DEVICE_MARK_EDIT, json
        ) {
            callback.invoke(it)
        }
    }

    /**
     * 获取APK版本信息
     * @param callback Function1<ApkUpdateBean, Unit>
     */
    fun getApkVersionInfo(callback: (ApkUpdateBean) -> Unit){
        OkUtils.get<ApkUpdateBean>(URL.APK_UPDATE, {
            callback.invoke(it)
        })
    }


}