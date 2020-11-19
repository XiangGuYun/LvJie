package com.yxd.lvjie.bean

data class DeviceMarkListBean(
    val code: Int? = null,
    val `data`: List<Data?>? = null,
    val message: String? = null
) {
    data class Data(
        val equipId: Int? = null,
        val frequency: Int? = null,
        val number: Int? = null,
        val ratio: Int? = null,
        val strength: Int? = null
    )
}