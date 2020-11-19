package com.yxd.lvjie.bean

data class DeviceDetailBean(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null
) {
    data class Data(
        val company: String? = null,
        val createTime: Long? = null,
        val dataUpdateTime: Long? = null,
        val equipModel: String? = null,
        val equipNo: String? = null,
        val id: Int? = null,
        val installMode: Int? = null,
        val installPattern: Int? = null,
        val installPerson: String? = null,
        val installTime: Long? = null,
        val latitude: Int? = null,
        val longitude: Int? = null,
        val pipeCaliber: String? = null,
        val pipeMaterial: String? = null,
        val power: Double? = null,
        val valveLocation: String? = null,
        val valveNo: String? = null
    )
}