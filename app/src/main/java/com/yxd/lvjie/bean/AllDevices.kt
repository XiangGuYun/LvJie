package com.yxd.lvjie.bean

data class AllDevices(
    val code: Int? = null,
    val `data`: List<Data?>? = null,
    val message: String? = null
) {
    data class Data(
        val dataUpdateTime: Long? = null,
        val equipNo: String? = null,
        val id: Int? = null,
        val installPattern: Int? = null,
        val installTime: Long? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val power: Int? = null
    )
}