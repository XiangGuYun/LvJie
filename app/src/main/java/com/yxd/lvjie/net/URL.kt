package com.yxd.lvjie.net

object URL {

    const val BASE_URL = "http://119.3.66.45/api"

    const val LOGIN = "/user/admin/app/login"

    /**
     * 【高级设置】密码验证
     */
    const val PWD_VERIFY = "/system/config/app/checkPassword"

    /**
     * 【设备】编辑
     */
    const val DEVICE_EDIT = "/system/equip/app/edit"

    /**
     * 【设备】详情
     */
    const val DEVICE_DETAIL = "/system/equip/app/info"

    /**
     * 【设备】列表
     */
    const val DEVICE_LIST = "/system/equip/app/paging"

    /**
     * 【历史数据查询】
     */
    const val HISTORY_DATA_QUERY = "/system/equip/app/history"

    /**
     * 【曲线图】
     */
    const val CURVE_GRAPH = "/system/equip/app/chart"

    /**
     * 【设备标定】列表
     */
    const val DEVICE_MARK_LIST = "/system/equip-mark/list"

    /**
     * 【设备标定】编辑
     */
    const val DEVICE_MARK_EDIT = "/system/equip-mark/app/edit"
}