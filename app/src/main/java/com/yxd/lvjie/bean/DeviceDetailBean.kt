package com.yxd.lvjie.bean

data class DeviceDetailBean(
    var code: Int? = null,
    var `data`: Data?,
    var message: String? = null
) {
    data class Data(
        var company: String? = null,
        var createTime: Long? = null,
        var dataUpdateTime: Long? = null,
        var equipModel: String? = null,
        var equipNo: String? = null,
        var id: Int? = null,
        var installMode: Int? = null,
        var installPattern: Int? = null,
        var installPerson: String? = null,
        var installTime: Long? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var pipeCaliber: String? = null,
        var pipeMaterial: String? = null,
        var power: Double? = null,
        var valveLocation: String? = null,
        var valveNo: String? = null
    )
}