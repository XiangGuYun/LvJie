package com.yxd.lvjie.net

import com.yxd.baselib.utils.MD5Utils
import com.yxd.baselib.utils.OK
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
    fun editDevice(bean: DeviceEditBean, callback: (DeviceMarkEditBean) -> Unit) {
        OkUtils.post<DeviceMarkEditBean>(URL.DEVICE_EDIT, bean) {
            callback.invoke(it)
        }
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
     * @param installPattern String 安装模式 0-固定点 1-流动点 2-观察点 全部则不传
     */
    fun getDeviceList(
        pageNum: Int,
        installPattern: String = OK.OPTIONAL,
        callback: (DeviceListBean) -> Unit
    ) {
        OkUtils.get<DeviceListBean>(URL.DEVICE_LIST, {
            callback.invoke(it)
        }, "installPattern" to installPattern, "pageNum" to pageNum.toString())
    }

    /**
     * 历史数据查询
     * @param id String
     * @param startTime String
     * @param endTime String
     * @param callback Function1<HistoryDataBean, Unit>
     */
    fun getHistoryData(
        equipNo: String,
        pageNum: Int,
        startTime: String = OK.OPTIONAL,
        endTime: String = OK.OPTIONAL,
        callback: (HistoryDataBean) -> Unit
    ) {
        OkUtils.get<HistoryDataBean>(
            URL.HISTORY_DATA_QUERY, {
                callback.invoke(it)
            }, "equipNo" to equipNo,
            "startTime" to if (startTime == "0") OK.OPTIONAL else startTime,
            "endTime" to if (endTime == "0") OK.OPTIONAL else endTime,
            "pageNum" to pageNum.toString(),
            "pageSize" to "20"
        )
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
    fun getApkVersionInfo(callback: (ApkUpdateBean) -> Unit) {
        OkUtils.get<ApkUpdateBean>(URL.APK_UPDATE, {
            callback.invoke(it)
        })
    }

    fun getAllDevices(callback: (AllDevices) -> Unit) {
        OkUtils.get<AllDevices>(URL.ALL_DEVICES, {
            callback.invoke(it)
        })
    }

    fun syncHistoryData(dataList: List<HistoryData>, callback: (DeviceMarkEditBean) -> Unit) {
        OkUtils.post<DeviceMarkEditBean>("/system/history/app/add", dataList) {
            callback.invoke(it)
        }
    }


}