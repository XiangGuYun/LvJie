package com.yxd.lvjie.bean

data class DeviceListBean(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null
) {
    data class Data(
        val list: List<Device>? = null,
        val total: Int? = null
    ) {
        data class Device(
            val dataUpdateTime: Long? = null,
            val equipNo: String? = null,
            val id: Int? = null,
            val installPattern: Int? = null,
            val installTime: Long? = null,
            val latitude: String? = null,
            val longitude: String? = null,
            val power: Int? = null
        )
    }
}